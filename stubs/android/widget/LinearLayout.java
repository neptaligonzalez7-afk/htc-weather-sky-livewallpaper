package android.widget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

public class LinearLayout extends ViewGroup {
    public static final int VERTICAL = 1;
    public static final int HORIZONTAL = 0;

    public LinearLayout(Context context) {}

    public void setOrientation(int orientation) {}

    public static class LayoutParams extends ViewGroup.LayoutParams {
        public static final int MATCH_PARENT = -1;
        public static final int WRAP_CONTENT = -2;

        public LayoutParams(int width, int height) {
            super(width, height);
        }
    }
}
