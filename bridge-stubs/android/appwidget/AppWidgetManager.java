package android.appwidget;

import android.content.ComponentName;
import android.content.Context;

public class AppWidgetManager {
    public static final String ACTION_APPWIDGET_UPDATE = "android.appwidget.action.APPWIDGET_UPDATE";
    public static final String EXTRA_APPWIDGET_IDS = "appWidgetIds";

    public static AppWidgetManager getInstance(Context context) {
        return new AppWidgetManager();
    }

    public int[] getAppWidgetIds(ComponentName provider) {
        return new int[0];
    }

    public void updateAppWidget(int appWidgetId, android.widget.RemoteViews views) {}
    public void partiallyUpdateAppWidget(int appWidgetId, android.widget.RemoteViews views) {}
}
