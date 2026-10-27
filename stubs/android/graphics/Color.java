package android.graphics;

public class Color {
    public static int argb(int a, int r, int g, int b) {
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    public static int rgb(int r, int g, int b) {
        return argb(255, r, g, b);
    }
}
