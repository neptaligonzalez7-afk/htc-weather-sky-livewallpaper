package android.graphics;

public class Bitmap {
    public static final class Config {
        public static final Config ARGB_8888 = new Config();
    }
    public static Bitmap createBitmap(int width, int height, Config config) {
        return new Bitmap();
    }
    public int getWidth() { return 1; }
    public int getHeight() { return 1; }
    public void recycle() {}
}
