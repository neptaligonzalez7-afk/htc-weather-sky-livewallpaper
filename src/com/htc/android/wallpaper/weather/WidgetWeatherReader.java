package com.htc.android.wallpaper.weather;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

/** Optional read from flip clock / HTC Weather if installed (not required). */
final class WidgetWeatherReader {
    private static final String TAG = "WeatherVideoLwp";
    static final Uri WIDGET_DATA = Uri.parse("content://com.htc.widget.weatherclock.sync/data");
    private static final Uri WEATHER_APP_DATA = Uri.parse(
            "content://com.htc.Weather.com.htc.sync.provider.weather/data");

    private WidgetWeatherReader() {}

    static WeatherSnapshot readOptional(Context context) {
        WeatherSnapshot s = query(context, WIDGET_DATA);
        if (s != null) {
            return s;
        }
        return query(context, WEATHER_APP_DATA);
    }

    private static WeatherSnapshot query(Context context, Uri uri) {
        Cursor c = null;
        try {
            c = context.getContentResolver().query(uri, null, "type=?", new String[]{"1"}, null);
            if (c == null) {
                return null;
            }
            int bestCond = 0;
            boolean day = true;
            long updated = 0L;
            while (c.moveToNext()) {
                int cond = columnInt(c, "curConditionId");
                if (cond <= 0) {
                    continue;
                }
                bestCond = cond;
                day = parseDay(c);
                updated = columnLong(c, "lastUpdate");
            }
            if (bestCond <= 0) {
                return null;
            }
            return new WeatherSnapshot(bestCond, day, updated);
        } catch (Throwable t) {
            Log.w(TAG, "weather read failed " + uri, t);
            return null;
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }

    private static boolean parseDay(Cursor c) {
        String flag = columnString(c, "dayLightFlag");
        if ("false".equalsIgnoreCase(flag)) {
            return false;
        }
        if ("true".equalsIgnoreCase(flag)) {
            return true;
        }
        return true;
    }

    private static int columnInt(Cursor c, String name) {
        int idx = c.getColumnIndex(name);
        if (idx < 0) {
            return 0;
        }
        try {
            String s = c.getString(idx);
            if (s != null && s.length() > 0) {
                return Integer.parseInt(s);
            }
        } catch (Throwable ignored) {
        }
        return 0;
    }

    private static long columnLong(Cursor c, String name) {
        int idx = c.getColumnIndex(name);
        if (idx < 0) {
            return 0L;
        }
        try {
            String s = c.getString(idx);
            if (s != null && s.length() > 0) {
                return Long.parseLong(s);
            }
        } catch (Throwable ignored) {
        }
        return 0L;
    }

    private static String columnString(Cursor c, String name) {
        int idx = c.getColumnIndex(name);
        if (idx < 0) {
            return "";
        }
        String s = c.getString(idx);
        return s != null ? s : "";
    }
}
