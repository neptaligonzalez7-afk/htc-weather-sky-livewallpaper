package android.view;

import android.graphics.SurfaceTexture;

public class Surface {
    public Surface(SurfaceTexture surfaceTexture) {}
    public static final int FRAME_RATE_COMPATIBILITY_DEFAULT = 0;
    public static final int FRAME_RATE_COMPATIBILITY_AT_LEAST = 2;

    public boolean isValid() {
        return true;
    }

    public void setFrameRate(float frameRate, int compatibility) {}

    public void release() {}
}
