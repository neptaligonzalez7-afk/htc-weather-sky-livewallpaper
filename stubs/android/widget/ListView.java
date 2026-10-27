package android.widget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

public class ListView extends AdapterView {
    public ListView(Context context) {
        super(context);
    }

    public void setAdapter(ListAdapter adapter) {}

    public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {}

    public static class LayoutParams extends ViewGroup.LayoutParams {
        public LayoutParams(int w, int h) {
            super(w, h);
        }
    }
}
