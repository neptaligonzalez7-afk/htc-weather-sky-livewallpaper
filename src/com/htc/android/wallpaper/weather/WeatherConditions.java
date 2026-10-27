package com.htc.android.wallpaper.weather;

import android.content.Context;

/** Current condition: built-in Open-Meteo cache first; optional HTC apps if installed. */
final class WeatherConditions {
    private WeatherConditions() {}

    static WeatherSnapshot read(Context context) {
        WeatherSnapshot own = LwpWeatherPrefs.readSnapshot(context);
        if (own != null) {
            return own;
        }
        return WidgetWeatherReader.readOptional(context);
    }
}
