package android.widget;

public class RemoteViews {
    public RemoteViews(String packageName, int layoutId) {}
    public void setImageViewResource(int viewId, int srcId) {}
    public void setImageViewBitmap(int viewId, android.graphics.Bitmap bitmap) {}
    public void setTextViewText(int viewId, CharSequence text) {}
    public void setTextViewTextSize(int viewId, int units, float size) {}
    public void setTextColor(int viewId, int color) {}
    public void setOnClickPendingIntent(int viewId, android.app.PendingIntent pendingIntent) {}
    public void setViewVisibility(int viewId, int visibility) {}
    public void showNext(int viewId) {}
    public void setInt(int viewId, String methodName, int value) {}
    public void setFloat(int viewId, String methodName, float value) {}
    public void setBoolean(int viewId, String methodName, boolean value) {}
    public void setViewLayoutHeight(int viewId, float height, int units) {}
    public void setViewLayoutMargin(int viewId, int type, float value, int units) {}
    public static final int MARGIN_LEFT = 0;
    public static final int MARGIN_TOP = 1;
    public static final int MARGIN_RIGHT = 2;
    public static final int MARGIN_BOTTOM = 3;
}
