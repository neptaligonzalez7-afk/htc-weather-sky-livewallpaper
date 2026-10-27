package android.graphics;

public class Canvas {
    public Canvas() {}
    public Canvas(Bitmap bitmap) {}
    public int save() { return 0; }
    public void restore() {}
    public void translate(float dx, float dy) {}
    public void scale(float sx, float sy) {}
    public void drawBitmap(Bitmap bitmap, float left, float top, Paint paint) {}
    public void drawBitmap(Bitmap bitmap, Rect src, RectF dst, Paint paint) {}
    public void drawColor(int color) {}
    public void drawCircle(float cx, float cy, float radius, Paint paint) {}
    public void drawRect(float left, float top, float right, float bottom, Paint paint) {}
    public int getWidth() { return 1; }
    public int getHeight() { return 1; }
}
