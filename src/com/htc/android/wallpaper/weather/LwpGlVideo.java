package com.htc.android.wallpaper.weather;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.net.Uri;
import android.opengl.EGL14;
import android.opengl.EGLConfig;
import android.opengl.EGLContext;
import android.opengl.EGLDisplay;
import android.opengl.EGLSurface;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.os.SystemClock;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Draws sky MP4 with uniform center-crop (fixes stretched MediaPlayer on direct SurfaceHolder).
 */
final class LwpGlVideo implements Runnable {
    private static final String TAG = "WeatherVideoLwp";

    private static final String VERT =
            "attribute vec2 aPos;"
                    + "attribute vec2 aTex;"
                    + "uniform mat4 uTexMatrix;"
                    + "uniform vec2 uCropScale;"
                    + "uniform vec2 uCropOffset;"
                    + "varying vec2 vTex;"
                    + "void main(){"
                    + "  gl_Position=vec4(aPos,0.0,1.0);"
                    + "  vec2 tc=uCropOffset+aTex*uCropScale;"
                    + "  vTex=(uTexMatrix*vec4(tc,0.0,1.0)).xy;"
                    + "}";

    private static final String FRAG =
            "#extension GL_OES_EGL_image_external : require\n"
                    + "precision mediump float;"
                    + "varying vec2 vTex;"
                    + "uniform samplerExternalOES sVideo;"
                    + "void main(){"
                    + "  gl_FragColor=texture2D(sVideo,vTex);"
                    + "}";

    private final Object mLock = new Object();
    private SurfaceHolder mHolder;
    private Thread mThread;
    private volatile boolean mRunning;
    private volatile boolean mVisible = true;
    private volatile int mWidth = 1;
    private volatile int mHeight = 1;

    private volatile Uri mPlayUri;
    private volatile Context mDataContext;
    private volatile boolean mRestartPlayer;

    private EGLDisplay mDpy;
    private EGLContext mCtx;
    private EGLSurface mEglSurface;
    private EGLConfig mConfig;

    private int mProgram;
    private int mAPos;
    private int mATex;
    private int mUTexMatrix;
    private int mUCropScale;
    private int mUCropOffset;
    private int mUSampler;
    private FloatBuffer mQuad;

    private SurfaceTexture mSurfaceTexture;
    private Surface mPlayerSurface;
    private int mOesTexId;
    private MediaPlayer mPlayer;
    private int mVideoW;
    private int mVideoH;
    private final float[] mTexMatrix = new float[16];
    private final float[] mCropScale = new float[] { 1f, 1f };
    private final float[] mCropOffset = new float[] { 0f, 0f };

    void start(SurfaceHolder holder, int width, int height) {
        synchronized (mLock) {
            mHolder = holder;
            if (width > 0) {
                mWidth = width;
            }
            if (height > 0) {
                mHeight = height;
            }
            if (mThread == null || !mThread.isAlive()) {
                mRunning = true;
                mThread = new Thread(this, "WeatherSkyGL");
                mThread.start();
            }
        }
    }

    void setSize(int width, int height) {
        if (width > 0) {
            mWidth = width;
        }
        if (height > 0) {
            mHeight = height;
        }
        updateCrop();
    }

    void setVisible(boolean visible) {
        mVisible = visible;
        if (!visible) {
            pausePlayer();
        } else {
            resumePlayer();
        }
    }

    void play(Context dataContext, Uri uri) {
        mDataContext = dataContext;
        mPlayUri = uri;
        mRestartPlayer = true;
    }

    void stop() {
        mRunning = false;
        mPlayUri = null;
        mRestartPlayer = true;
        Thread t;
        synchronized (mLock) {
            t = mThread;
            mThread = null;
        }
        if (t != null) {
            try {
                t.join(1500);
            } catch (InterruptedException ignored) {
            }
        }
    }

    void releasePlayerOnly() {
        mPlayUri = null;
        mRestartPlayer = true;
    }

    boolean isPlayingIndexReady() {
        return mPlayer != null;
    }

    @Override
    public void run() {
        try {
            if (!initEgl()) {
                Log.e(TAG, "weather GL egl init failed " + EGL14.eglGetError());
                return;
            }
            if (!initVideoSurface()) {
                Log.e(TAG, "weather GL video surface init failed");
                return;
            }
            buildProgram();
            mQuad = quad();
            updateCrop();
            long next = SystemClock.uptimeMillis();
            while (mRunning) {
                if (mRestartPlayer) {
                    mRestartPlayer = false;
                    releasePlayerOnGl();
                    Uri uri = mPlayUri;
                    Context dataCtx = mDataContext;
                    if (uri != null && dataCtx != null) {
                        startPlayerOnGl(dataCtx, uri);
                    }
                }
                int w = mWidth;
                int h = mHeight;
                GLES20.glViewport(0, 0, w, h);
                GLES20.glClearColor(0f, 0f, 0f, 1f);
                GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
                if (mPlayer != null && mVisible) {
                    try {
                        mSurfaceTexture.updateTexImage();
                        mSurfaceTexture.getTransformMatrix(mTexMatrix);
                    } catch (Throwable t) {
                        Log.w(TAG, "updateTexImage failed", t);
                    }
                    drawFrame();
                }
                EGL14.eglSwapBuffers(mDpy, mEglSurface);
                next += 8;
                long now = SystemClock.uptimeMillis();
                if (next > now) {
                    try {
                        Thread.sleep(next - now);
                    } catch (InterruptedException ignored) {
                    }
                } else {
                    next = now;
                }
            }
        } catch (Throwable t) {
            Log.e(TAG, "weather GL loop failed", t);
        } finally {
            releasePlayerOnGl();
            releaseVideoSurface();
            releaseEgl();
        }
    }

    private void drawFrame() {
        GLES20.glUseProgram(mProgram);
        mQuad.position(0);
        GLES20.glVertexAttribPointer(mAPos, 2, GLES20.GL_FLOAT, false, 16, mQuad);
        GLES20.glEnableVertexAttribArray(mAPos);
        mQuad.position(2);
        GLES20.glVertexAttribPointer(mATex, 2, GLES20.GL_FLOAT, false, 16, mQuad);
        GLES20.glEnableVertexAttribArray(mATex);
        GLES20.glUniformMatrix4fv(mUTexMatrix, 1, false, mTexMatrix, 0);
        GLES20.glUniform2f(mUCropScale, mCropScale[0], mCropScale[1]);
        GLES20.glUniform2f(mUCropOffset, mCropOffset[0], mCropOffset[1]);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, mOesTexId);
        GLES20.glUniform1i(mUSampler, 0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
    }

    private void updateCrop() {
        int vw = mVideoW;
        int vh = mVideoH;
        int sw = mWidth;
        int sh = mHeight;
        if (vw <= 0 || vh <= 0 || sw <= 0 || sh <= 0) {
            mCropScale[0] = 1f;
            mCropScale[1] = 1f;
            mCropOffset[0] = 0f;
            mCropOffset[1] = 0f;
            return;
        }
        float videoAspect = (float) vw / (float) vh;
        float viewAspect = (float) sw / (float) sh;
        if (videoAspect > viewAspect) {
            mCropScale[0] = viewAspect / videoAspect;
            mCropScale[1] = 1f;
        } else {
            mCropScale[0] = 1f;
            mCropScale[1] = videoAspect / viewAspect;
        }
        mCropOffset[0] = (1f - mCropScale[0]) * 0.5f;
        mCropOffset[1] = (1f - mCropScale[1]) * 0.5f;
        Log.i(TAG, "video crop " + vw + "x" + vh + " surface " + sw + "x" + sh
                + " scale " + mCropScale[0] + "," + mCropScale[1]);
    }

    private void startPlayerOnGl(Context dataContext, Uri uri) {
        try {
            mSurfaceTexture.setDefaultBufferSize(mWidth, mHeight);
            MediaPlayer player = new MediaPlayer();
            player.setLooping(true);
            player.setSurface(mPlayerSurface);
            player.setDataSource(dataContext, uri);
            player.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                @Override
                public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                    mVideoW = width;
                    mVideoH = height;
                    updateCrop();
                }
            });
            player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    try {
                        mVideoW = mp.getVideoWidth();
                        mVideoH = mp.getVideoHeight();
                        updateCrop();
                        mp.start();
                    } catch (Throwable t) {
                        Log.w(TAG, "GL player start failed", t);
                    }
                }
            });
            player.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    Log.w(TAG, "GL media error what=" + what + " extra=" + extra);
                    mRestartPlayer = true;
                    mPlayUri = null;
                    return true;
                }
            });
            player.prepareAsync();
            mPlayer = player;
            Log.i(TAG, "GL playing " + uri);
        } catch (Throwable t) {
            Log.w(TAG, "GL player setup failed", t);
            releasePlayerOnGl();
        }
    }

    private void releasePlayerOnGl() {
        if (mPlayer == null) {
            mVideoW = 0;
            mVideoH = 0;
            return;
        }
        try {
            mPlayer.stop();
        } catch (Throwable ignored) {
        }
        try {
            mPlayer.release();
        } catch (Throwable ignored) {
        }
        mPlayer = null;
        mVideoW = 0;
        mVideoH = 0;
    }

    private void pausePlayer() {
        if (mPlayer == null) {
            return;
        }
        try {
            if (mPlayer.isPlaying()) {
                mPlayer.pause();
            }
        } catch (Throwable ignored) {
        }
    }

    private void resumePlayer() {
        if (mPlayer == null || !mVisible) {
            return;
        }
        try {
            mPlayer.start();
        } catch (Throwable ignored) {
        }
    }

    private boolean initVideoSurface() {
        int[] ids = new int[1];
        GLES20.glGenTextures(1, ids, 0);
        mOesTexId = ids[0];
        if (mOesTexId == 0) {
            return false;
        }
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, mOesTexId);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
        mSurfaceTexture = new SurfaceTexture(mOesTexId);
        mPlayerSurface = new Surface(mSurfaceTexture);
        return true;
    }

    private void releaseVideoSurface() {
        releasePlayerOnGl();
        if (mPlayerSurface != null) {
            try {
                mPlayerSurface.release();
            } catch (Throwable ignored) {
            }
            mPlayerSurface = null;
        }
        if (mSurfaceTexture != null) {
            try {
                mSurfaceTexture.release();
            } catch (Throwable ignored) {
            }
            mSurfaceTexture = null;
        }
        if (mOesTexId != 0) {
            GLES20.glDeleteTextures(1, new int[] { mOesTexId }, 0);
            mOesTexId = 0;
        }
    }

    private boolean initEgl() {
        mDpy = EGL14.eglGetDisplay(EGL14.EGL_DEFAULT_DISPLAY);
        if (mDpy == null || mDpy == EGL14.EGL_NO_DISPLAY) {
            return false;
        }
        int[] ver = new int[2];
        if (!EGL14.eglInitialize(mDpy, ver, 0, ver, 1)) {
            return false;
        }
        int[] attrib = {
                EGL14.EGL_RENDERABLE_TYPE, EGL14.EGL_OPENGL_ES2_BIT,
                EGL14.EGL_RED_SIZE, 8,
                EGL14.EGL_GREEN_SIZE, 8,
                EGL14.EGL_BLUE_SIZE, 8,
                EGL14.EGL_ALPHA_SIZE, 8,
                EGL14.EGL_NONE
        };
        EGLConfig[] configs = new EGLConfig[1];
        int[] num = new int[1];
        if (!EGL14.eglChooseConfig(mDpy, attrib, 0, configs, 0, 1, num, 0) || num[0] <= 0) {
            return false;
        }
        mConfig = configs[0];
        int[] ctxAttr = { EGL14.EGL_CONTEXT_CLIENT_VERSION, 2, EGL14.EGL_NONE };
        mCtx = EGL14.eglCreateContext(mDpy, mConfig, EGL14.EGL_NO_CONTEXT, ctxAttr, 0);
        Object win;
        synchronized (mLock) {
            win = mHolder != null ? mHolder.getSurface() : null;
        }
        mEglSurface = EGL14.eglCreateWindowSurface(mDpy, mConfig, win, new int[] { EGL14.EGL_NONE }, 0);
        return EGL14.eglMakeCurrent(mDpy, mEglSurface, mEglSurface, mCtx);
    }

    private void buildProgram() {
        int vs = compile(GLES20.GL_VERTEX_SHADER, VERT);
        int fs = compile(GLES20.GL_FRAGMENT_SHADER, FRAG);
        mProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(mProgram, vs);
        GLES20.glAttachShader(mProgram, fs);
        GLES20.glLinkProgram(mProgram);
        int[] linked = new int[1];
        GLES20.glGetProgramiv(mProgram, GLES20.GL_LINK_STATUS, linked, 0);
        if (linked[0] == 0) {
            Log.e(TAG, "weather GL program link failed: " + GLES20.glGetProgramInfoLog(mProgram));
        }
        mAPos = GLES20.glGetAttribLocation(mProgram, "aPos");
        mATex = GLES20.glGetAttribLocation(mProgram, "aTex");
        mUTexMatrix = GLES20.glGetUniformLocation(mProgram, "uTexMatrix");
        mUCropScale = GLES20.glGetUniformLocation(mProgram, "uCropScale");
        mUCropOffset = GLES20.glGetUniformLocation(mProgram, "uCropOffset");
        mUSampler = GLES20.glGetUniformLocation(mProgram, "sVideo");
    }

    private int compile(int type, String src) {
        int s = GLES20.glCreateShader(type);
        GLES20.glShaderSource(s, src);
        GLES20.glCompileShader(s);
        int[] ok = new int[1];
        GLES20.glGetShaderiv(s, GLES20.GL_COMPILE_STATUS, ok, 0);
        if (ok[0] == 0) {
            Log.e(TAG, "weather GL shader compile failed: " + GLES20.glGetShaderInfoLog(s));
        }
        return s;
    }

    private static FloatBuffer quad() {
        float[] v = {
                -1f, -1f, 0f, 0f,
                1f, -1f, 1f, 0f,
                -1f, 1f, 0f, 1f,
                1f, 1f, 1f, 1f
        };
        ByteBuffer bb = ByteBuffer.allocateDirect(v.length * 4);
        bb.order(ByteOrder.nativeOrder());
        FloatBuffer fb = bb.asFloatBuffer();
        fb.put(v);
        fb.position(0);
        return fb;
    }

    private void releaseEgl() {
        if (mProgram != 0) {
            GLES20.glDeleteProgram(mProgram);
            mProgram = 0;
        }
        if (mDpy != null) {
            EGL14.eglMakeCurrent(mDpy, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_CONTEXT);
            if (mEglSurface != null) {
                EGL14.eglDestroySurface(mDpy, mEglSurface);
            }
            if (mCtx != null) {
                EGL14.eglDestroyContext(mDpy, mCtx);
            }
            EGL14.eglTerminate(mDpy);
        }
        mEglSurface = null;
        mCtx = null;
        mDpy = null;
    }
}
