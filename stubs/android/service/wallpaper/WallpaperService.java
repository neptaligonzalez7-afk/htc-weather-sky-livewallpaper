package android.service.wallpaper;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

public class WallpaperService extends Service {
    public Engine onCreateEngine() {
        return null;
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    public class Engine {
        public Engine() {}

        public SurfaceHolder getSurfaceHolder() {
            return null;
        }

        public void setOffsetNotificationsEnabled(boolean enabled) {}

        public void setTouchEventsEnabled(boolean enabled) {}

        public boolean isPreview() {
            return false;
        }

        public boolean isVisible() {
            return false;
        }

        public void onCreate(SurfaceHolder surfaceHolder) {}

        public void onDestroy() {}

        public void onVisibilityChanged(boolean visible) {}

        public void onSurfaceCreated(SurfaceHolder holder) {}

        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

        public void onSurfaceDestroyed(SurfaceHolder holder) {}

        public void onOffsetsChanged(float xOffset, float yOffset, float xOffsetStep,
                float yOffsetStep, int xPixelOffset, int yPixelOffset) {}

        public void onTouchEvent(MotionEvent event) {}

        public void onDesiredSizeChanged(int desiredWidth, int desiredHeight) {}
    }
}
