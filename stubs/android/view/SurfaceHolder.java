package android.view;

import android.graphics.Canvas;

public interface SurfaceHolder {
    Canvas lockCanvas();
    Canvas lockHardwareCanvas();
    void unlockCanvasAndPost(Canvas canvas);
    Surface getSurface();
    void setFixedSize(int width, int height);

    interface Callback {
        void surfaceCreated(SurfaceHolder holder);
        void surfaceChanged(SurfaceHolder holder, int format, int width, int height);
        void surfaceDestroyed(SurfaceHolder holder);
    }
}
