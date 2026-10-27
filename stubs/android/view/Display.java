package android.view;

public class Display {
    public Mode getMode() {
        return null;
    }

    public Mode[] getSupportedModes() {
        return null;
    }

    public static final class Mode {
        public float getRefreshRate() {
            return 60f;
        }
    }
}
