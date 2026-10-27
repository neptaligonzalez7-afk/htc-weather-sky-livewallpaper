package com.htc.android.wallpaper.weather;

import android.content.Context;
import android.content.SharedPreferences;

final class LwpWeatherPrefs {
    static final String PREFS = "lwp_openmeteo";
    static final String KEY_MODE = "mode";
    static final String KEY_LAT_BITS = "lat_bits";
    static final String KEY_LON_BITS = "lon_bits";
    static final String KEY_LABEL = "label";
    static final String KEY_MANUAL_CITY = "manual_city";
    static final String KEY_COND = "cur_condition";
    static final String KEY_DAY = "is_day";
    static final String KEY_UPDATED = "updated_ms";
    static final String KEY_STATUS_MSG = "status_msg";

    static final int MODE_AUTO = 0;
    static final int MODE_MANUAL = 1;

    static final String ACTION_UPDATED = "com.htc.android.wallpaper.weather.WEATHER_UPDATED";

    private LwpWeatherPrefs() {}

    static SharedPreferences prefs(Context context) {
        return context.getApplicationContext().getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    static void savePlace(Context context, double lat, double lon, String label) {
        prefs(context).edit()
                .putLong(KEY_LAT_BITS, Double.doubleToRawLongBits(lat))
                .putLong(KEY_LON_BITS, Double.doubleToRawLongBits(lon))
                .putString(KEY_LABEL, label != null ? label : "")
                .apply();
    }

    static void saveSnapshot(Context context, int conditionId, boolean isDay) {
        prefs(context).edit()
                .putInt(KEY_COND, conditionId)
                .putBoolean(KEY_DAY, isDay)
                .putLong(KEY_UPDATED, System.currentTimeMillis())
                .apply();
    }

    static void setStatusMessage(Context context, String msg) {
        prefs(context).edit().putString(KEY_STATUS_MSG, msg != null ? msg : "").apply();
    }

    static String getStatusMessage(Context context) {
        return prefs(context).getString(KEY_STATUS_MSG, "");
    }

    static WeatherSnapshot readSnapshot(Context context) {
        SharedPreferences p = prefs(context);
        int cond = p.getInt(KEY_COND, 0);
        if (cond <= 0) {
            return null;
        }
        boolean day = p.getBoolean(KEY_DAY, true);
        long updated = p.getLong(KEY_UPDATED, 0L);
        return new WeatherSnapshot(cond, day, updated);
    }
}
