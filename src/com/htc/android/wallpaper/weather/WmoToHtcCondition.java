package com.htc.android.wallpaper.weather;

/** Open-Meteo WMO weather_code → HTC / AccuWeather icon id (for sky MP4 mapping). */
final class WmoToHtcCondition {
    private WmoToHtcCondition() {}

    static int map(int wmo, boolean day) {
        if (wmo == 0) {
            return day ? 2 : 33;
        }
        if (wmo == 1) {
            return day ? 3 : 35;
        }
        if (wmo == 2) {
            return day ? 3 : 35;
        }
        if (wmo == 3) {
            return 7;
        }
        if (wmo == 45 || wmo == 48) {
            return 11;
        }
        if (wmo >= 51 && wmo <= 55) {
            return day ? 12 : 39;
        }
        if (wmo >= 56 && wmo <= 57) {
            return 26;
        }
        if (wmo >= 61 && wmo <= 65) {
            return day ? 18 : 40;
        }
        if (wmo >= 66 && wmo <= 67) {
            return 26;
        }
        if (wmo >= 71 && wmo <= 77) {
            return 22;
        }
        if (wmo >= 80 && wmo <= 82) {
            return day ? 12 : 39;
        }
        if (wmo >= 85 && wmo <= 86) {
            return 22;
        }
        if (wmo == 95) {
            return day ? 15 : 42;
        }
        if (wmo >= 96) {
            return 16;
        }
        return day ? 1 : 33;
    }
}
