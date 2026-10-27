package android.app;

import android.content.Context;
import android.content.DialogInterface;

public class AlertDialog implements DialogInterface {
    public void show() {}
    public void dismiss() {}
    public void setCanceledOnTouchOutside(boolean cancel) {}
    public static class Builder {
        public Builder(Context context) {}
        public Builder setTitle(int titleId) { return this; }
        public Builder setTitle(CharSequence title) { return this; }
        public Builder setSingleChoiceItems(CharSequence[] items, int checkedItem,
                DialogInterface.OnClickListener listener) { return this; }
        public Builder setOnCancelListener(DialogInterface.OnCancelListener listener) { return this; }
        public AlertDialog create() { return new AlertDialog(); }
    }
}
