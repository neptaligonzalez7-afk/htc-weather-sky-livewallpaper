package com.htc.android.wallpaper.weather;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

/** Background Open-Meteo refresh for standalone sky wallpaper. */
public final class LwpWeatherSync {
    private static final String TAG = "WeatherVideoLwp";
    private static final long MIN_INTERVAL_MS = 10L * 60L * 1000L;
    private static volatile boolean sRunning;
    private static volatile long sLastAttemptMs;

    private LwpWeatherSync() {}

    public static void request(Context context, boolean force) {
        if (context == null) {
            return;
        }
        long now = System.currentTimeMillis();
        if (!force && now - sLastAttemptMs < MIN_INTERVAL_MS) {
            return;
        }
        sLastAttemptMs = now;
        final Context app = context.getApplicationContext();
        if (sRunning) {
            return;
        }
        sRunning = true;
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    runSync(app);
                } finally {
                    sRunning = false;
                }
            }
        }, "LwpWeatherSync");
        t.setDaemon(true);
        t.start();
    }

    private static void runSync(Context context) {
        try {
            SharedPreferencesHelper h = new SharedPreferencesHelper(context);
            if (h.mode() == LwpWeatherPrefs.MODE_MANUAL) {
                String city = h.manualCity();
                if (city != null && city.trim().length() > 0 && !h.hasSavedCoords()) {
                    GeocodePlace g = LwpOpenMeteo.geocodeFirst(city.trim());
                    LwpWeatherPrefs.savePlace(context, g.lat, g.lon, g.label);
                }
            }
            LwpLocationResolver.Place place = LwpLocationResolver.resolve(context);
            if (place == null) {
                LwpWeatherPrefs.setStatusMessage(context, "No location — pick a city or allow GPS.");
                Log.w(TAG, "no location; grant location or set city in wallpaper settings");
                sendUpdated(context);
                return;
            }
            LwpWeatherPrefs.savePlace(context, place.lat, place.lon, place.label);
            LwpOpenMeteo.Result r = LwpOpenMeteo.fetchCurrent(place.lat, place.lon, place.label);
            LwpWeatherPrefs.saveSnapshot(context, r.conditionId, r.isDay);
            Log.i(TAG, "sync ok cond=" + r.conditionId + " day=" + r.isDay + " " + place.label);
            LwpWeatherPrefs.setStatusMessage(context,
                    "Updated for " + place.label + " (condition " + r.conditionId + ").");
            sendUpdated(context);
        } catch (Throwable t) {
            Log.w(TAG, "sync failed", t);
            LwpWeatherPrefs.setStatusMessage(context, "Sync failed: " + t.getMessage());
            sendUpdated(context);
        }
    }

    private static void sendUpdated(Context context) {
        Intent intent = new Intent(LwpWeatherPrefs.ACTION_UPDATED);
        intent.setPackage(context.getPackageName());
        context.sendBroadcast(intent);
    }

    /** Thin wrapper so sync code can read prefs without extra imports in one place. */
    private static final class SharedPreferencesHelper {
        private final android.content.SharedPreferences p;

        SharedPreferencesHelper(Context context) {
            p = LwpWeatherPrefs.prefs(context);
        }

        int mode() {
            return p.getInt(LwpWeatherPrefs.KEY_MODE, LwpWeatherPrefs.MODE_AUTO);
        }

        String manualCity() {
            return p.getString(LwpWeatherPrefs.KEY_MANUAL_CITY, "");
        }

        boolean hasSavedCoords() {
            double lat = Double.longBitsToDouble(p.getLong(LwpWeatherPrefs.KEY_LAT_BITS, 0L));
            double lon = Double.longBitsToDouble(p.getLong(LwpWeatherPrefs.KEY_LON_BITS, 0L));
            return Math.abs(lat) > 0.01 || Math.abs(lon) > 0.01;
        }
    }
}
