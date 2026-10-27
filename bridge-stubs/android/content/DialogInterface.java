package android.content;

public interface DialogInterface {
    void dismiss();
    interface OnClickListener {
        void onClick(DialogInterface dialog, int which);
    }
    interface OnCancelListener {
        void onCancel(DialogInterface dialog);
    }
}
