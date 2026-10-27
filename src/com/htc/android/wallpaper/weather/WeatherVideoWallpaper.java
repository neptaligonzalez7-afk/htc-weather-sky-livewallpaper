package com.htc.android.wallpaper.weather;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.SurfaceHolder;

/**
 * Live wallpaper: loops bundled HTC sky MP4s for the current condition from
 * built-in Open-Meteo (no HTC Weather app or flip clock required).
 */
public class WeatherVideoWallpaper extends WallpaperService {
    private static final String TAG = "WeatherVideoLwp";
    private static final long REFRESH_MS = 15L * 60L * 1000L;
    @Override
    public Engine onCreateEngine() {
        return new VideoEngine();
    }

    private final class VideoEngine extends Engine {
        private final Handler mHandler = new Handler(Looper.getMainLooper());
        private final LwpGlVideo mGlVideo = new LwpGlVideo();
        private int mPlayingIndex = -1;
        private long mLastSnapshotMs;
        private boolean mSurfaceReady;
        private int mSurfaceW;
        private int mSurfaceH;
        private boolean mUsingLandscapeAssets;
        private BroadcastReceiver mUpdateReceiver;
        private final Runnable mRefresh = new Runnable() {
            @Override
            public void run() {
                LwpWeatherSync.request(getApplicationContext(), false);
                applyWeather(false);
                if (isVisible()) {
                    mHandler.postDelayed(this, REFRESH_MS);
                }
            }
        };

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            setOffsetNotificationsEnabled(false);
            mUpdateReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    applyWeather(true);
                }
            };
            try {
                IntentFilter filter = new IntentFilter(LwpWeatherPrefs.ACTION_UPDATED);
                getApplicationContext().registerReceiver(mUpdateReceiver, filter, Context.RECEIVER_NOT_EXPORTED);
            } catch (Throwable t) {
                Log.w(TAG, "update receiver register failed", t);
            }
            LwpWeatherSync.request(getApplicationContext(), false);
        }

        @Override
        public void onDestroy() {
            mHandler.removeCallbacks(mRefresh);
            mGlVideo.stop();
            if (mUpdateReceiver != null) {
                try {
                    getApplicationContext().unregisterReceiver(mUpdateReceiver);
                } catch (Throwable ignored) {
                }
            }
            super.onDestroy();
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            mGlVideo.setVisible(visible);
            if (visible) {
                LwpDisplayRefresh.enable(getSurfaceHolder(), getApplicationContext());
                LwpWeatherSync.request(getApplicationContext(), false);
                applyWeather(true);
                mHandler.removeCallbacks(mRefresh);
                mHandler.postDelayed(mRefresh, REFRESH_MS);
            } else {
                mHandler.removeCallbacks(mRefresh);
                LwpDisplayRefresh.disable(getSurfaceHolder());
            }
        }

        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            super.onSurfaceCreated(holder);
            mSurfaceReady = true;
            mGlVideo.start(holder, mSurfaceW, mSurfaceH);
            applyWeather(true);
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);
            mSurfaceW = width;
            mSurfaceH = height;
            if (isVisible() || isPreview()) {
                LwpDisplayRefresh.enable(holder, getApplicationContext());
            }
            boolean landscape = WeatherVideoAssets.useLandscapeAssets(width, height);
            if (landscape != mUsingLandscapeAssets && mPlayingIndex >= 0) {
                mUsingLandscapeAssets = landscape;
                applyWeather(true);
                return;
            }
            mUsingLandscapeAssets = landscape;
            mGlVideo.setSize(width, height);
            mGlVideo.start(holder, width, height);
            if (mSurfaceReady && mPlayingIndex < 0) {
                applyWeather(true);
            }
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            mSurfaceReady = false;
            LwpDisplayRefresh.disable(holder);
            mGlVideo.stop();
            super.onSurfaceDestroyed(holder);
        }

        private void applyWeather(boolean force) {
            if (!mSurfaceReady) {
                return;
            }
            if (!isVisible() && !isPreview()) {
                return;
            }
            WeatherSnapshot snap = WeatherConditions.read(getApplicationContext());
            if (snap == null) {
                Log.i(TAG, "no weather yet; syncing Open-Meteo, default sunny preview");
                LwpWeatherSync.request(getApplicationContext(), false);
                if (isPreview() || mPlayingIndex < 0) {
                    playIndex(0, true);
                }
                return;
            }
            if (!force && snap.updatedMs > 0 && snap.updatedMs == mLastSnapshotMs) {
                return;
            }
            mLastSnapshotMs = snap.updatedMs;
            int index = WeatherVideoAssets.videoIndexForCondition(snap.conditionId, snap.isDay);
            if (index == mPlayingIndex && mGlVideo.isPlayingIndexReady()) {
                return;
            }
            playIndex(index, false);
        }

        private void playIndex(int index, boolean previewDefault) {
            Uri uri = WeatherVideoAssets.videoUri(
                    getApplicationContext(), index, mSurfaceW, mSurfaceH);
            mUsingLandscapeAssets = WeatherVideoAssets.useLandscapeAssets(mSurfaceW, mSurfaceH);
            if (uri == null) {
                Log.w(TAG, "no video uri; rebuild APK with bundled sky MP4s (res/raw)");
                return;
            }
            startVideo(uri, index, previewDefault);
        }

        private void startVideo(final Uri uri, final int index, boolean previewDefault) {
            mPlayingIndex = index;
            mGlVideo.start(getSurfaceHolder(), mSurfaceW, mSurfaceH);
            mGlVideo.play(getApplicationContext(), uri);
            Log.i(TAG, "playing index " + index + " " + uri);
        }

    }
}

