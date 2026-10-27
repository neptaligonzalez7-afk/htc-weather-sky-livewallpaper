package android.widget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

public abstract class AdapterView extends ViewGroup {
    public AdapterView(Context context) {}

    public interface OnItemClickListener {
        void onItemClick(AdapterView parent, View view, int position, long id);
    }
}
