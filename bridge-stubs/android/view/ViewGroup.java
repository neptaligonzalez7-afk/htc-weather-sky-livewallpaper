package android.view;

public class ViewGroup extends View {
    public void addView(View child) {}
    public static class LayoutParams {
        public int width;
        public int height;
        public LayoutParams(int w, int h) {
            width = w;
            height = h;
        }
    }
}
