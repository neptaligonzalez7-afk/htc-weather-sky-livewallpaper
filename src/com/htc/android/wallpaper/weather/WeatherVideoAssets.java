package com.htc.android.wallpaper.weather;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.util.Log;

/** Maps HTC / AccuWeather condition id to sky MP4 in {@code com.htc.htcweatherassets}. */
public final class WeatherVideoAssets {
    private static final String TAG = "WeatherVideoLwp";
    private static final String ASSETS_PKG = "com.htc.htcweatherassets";

    private static final String[] PATH = {
            "sunny", "clear", "thunderstorm_day", "thunderstorm_night",
            "cloudy_day", "cloudy_night", "fog_day", "fog_night", "hot",
            "partly_cloud_day", "partly_sunny", "rain_day", "rain_night",
            "snow_day", "snow_night", "windy_day", "windy_night", "partly_cloud_night"
    };

    private static final int[] ICON_TO_VIDEO = {
            0, 0, 10, 9, 0, 4, 4, 4, 0, 0, 6, 11, 11, 11, 2, 2, 2, 11,
            13, 13, 13, 13, 13, 13, 13, 11, 0, 0, 11, 8, 1, 15, 1, 1,
            17, 17, 7, 5, 12, 12, 3, 3, 14, 14, 0, 0, 0, 0, 0, 0, 3, 6, 6, 3
    };

    private WeatherVideoAssets() {}

    public static int videoIndexForCondition(int conditionId, boolean isDay) {
        int idx = 0;
        int key = conditionId - 1;
        if (key >= 0 && key < ICON_TO_VIDEO.length) {
            idx = ICON_TO_VIDEO[key];
        }
        if (!isDay) {
            switch (idx) {
                case 2:
                    return 3;
                case 4:
                    return 5;
                case 6:
                    return 7;
                case 11:
                    return 12;
                case 13:
                    return 14;
                case 15:
                    return 16;
                default:
                    break;
            }
        }
        if (idx < 0 || idx >= PATH.length) {
            return 0;
        }
        return idx;
    }

    public static Uri videoUri(Context context, int videoIndex, int surfaceW, int surfaceH) {
        String path = videoUriString(context, videoIndex, surfaceW, surfaceH);
        if (path == null || path.length() == 0) {
            return null;
        }
        return Uri.parse(path);
    }

    /** True when the wallpaper surface is wider than tall (use {@code weather_l_*} clips). */
    static boolean useLandscapeAssets(int surfaceW, int surfaceH) {
        if (surfaceW > 0 && surfaceH > 0) {
            return surfaceW > surfaceH;
        }
        return false;
    }

    public static String videoUriString(Context context, int videoIndex, int surfaceW, int surfaceH) {
        if (videoIndex < 0 || videoIndex >= PATH.length) {
            videoIndex = 0;
        }
        String stem = PATH[videoIndex];
        boolean landscape = useLandscapeAssets(surfaceW, surfaceH);
        try {
            Resources res = context.getPackageManager()
                    .getResourcesForApplication(ASSETS_PKG);
            String primary = (landscape ? "weather_l_" : "weather_") + stem;
            String fallback = (landscape ? "weather_" : "weather_l_") + stem;
            int id = res.getIdentifier(primary, "raw", ASSETS_PKG);
            if (id <= 0) {
                id = res.getIdentifier(fallback, "raw", ASSETS_PKG);
            }
            if (id > 0) {
                return "android.resource://" + res.getResourcePackageName(id) + "/" + id;
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.w(TAG, "install com.htc.htcweatherassets for sky videos", e);
        } catch (Throwable t) {
            Log.w(TAG, "video uri failed", t);
        }
        return null;
    }
}
