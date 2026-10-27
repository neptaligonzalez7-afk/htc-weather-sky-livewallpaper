package android.media;

import android.content.Context;
import android.net.Uri;
import android.view.Surface;

public class MediaPlayer {
    public interface OnPreparedListener {
        void onPrepared(MediaPlayer mp);
    }

    public interface OnErrorListener {
        boolean onError(MediaPlayer mp, int what, int extra);
    }

    public interface OnVideoSizeChangedListener {
        void onVideoSizeChanged(MediaPlayer mp, int width, int height);
    }

    public static final int VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING = 2;

    public void setLooping(boolean looping) {}
    public void setVideoScalingMode(int mode) {}
    public void setOnVideoSizeChangedListener(OnVideoSizeChangedListener listener) {}
    public int getVideoWidth() { return 0; }
    public int getVideoHeight() { return 0; }
    public void setSurface(Surface surface) {}
    public void setDataSource(Context context, Uri uri) throws Exception {}
    public void setOnPreparedListener(OnPreparedListener listener) {}
    public void setOnErrorListener(OnErrorListener listener) {}
    public void prepareAsync() {}
    public void start() {}
    public void pause() {}
    public void stop() {}
    public void release() {}
    public boolean isPlaying() { return false; }
}
