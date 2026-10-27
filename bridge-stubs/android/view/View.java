package android.view;

public class View {
    public static final int GONE = 8;
    public void setVisibility(int visibility) {}
    public int getWidth() { return 0; }
    public int getHeight() { return 0; }
    public void setTranslationY(float y) {}
    public void setScaleX(float scale) {}
    public void setScaleY(float scale) {}
    public void setPivotX(float pivot) {}
    public void setPivotY(float pivot) {}
    public ViewGroup.LayoutParams getLayoutParams() { return null; }
    public void setLayoutParams(ViewGroup.LayoutParams params) {}
    public boolean post(Runnable action) { return false; }
    public void setPadding(int left, int top, int right, int bottom) {}
    public void setBackgroundColor(int color) {}
    public void setFitsSystemWindows(boolean fits) {}
    public void setClickable(boolean clickable) {}
    public void setEnabled(boolean enabled) {}
    public void setOnClickListener(OnClickListener listener) {}
    public View findViewById(int id) { return null; }
    public void setTag(Object tag) {}
    public Object getTag() { return null; }
    public View findViewWithTag(Object tag) { return null; }
    public interface OnClickListener {
        void onClick(View v);
    }
}
