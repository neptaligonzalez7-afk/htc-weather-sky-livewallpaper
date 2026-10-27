package android.graphics;

public class Paint {
    public static final int ANTI_ALIAS_FLAG = 1;
    public static final int FILTER_BITMAP_FLAG = 2;
    public Paint() {}
    public Paint(int flags) {}
    public void setFilterBitmap(boolean filter) {}
    public void setAntiAlias(boolean aa) {}
    public void setAlpha(int a) {}
    public void setColor(int color) {}
    public Shader setShader(Shader shader) { return shader; }
    public Xfermode setXfermode(Xfermode xfermode) { return xfermode; }
    public ColorFilter setColorFilter(ColorFilter filter) { return filter; }
}
