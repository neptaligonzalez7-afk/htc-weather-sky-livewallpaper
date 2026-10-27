package com.htc.android.wallpaper.weather;

/** One Open-Meteo geocoding result for the city picker. */
final class GeocodePlace {
    final double lat;
    final double lon;
    final String label;
    final String subtitle;

    GeocodePlace(double lat, double lon, String label, String subtitle) {
        this.lat = lat;
        this.lon = lon;
        this.label = label != null ? label : "";
        this.subtitle = subtitle != null ? subtitle : "";
    }

    String line() {
        if (subtitle.length() > 0) {
            return label + " — " + subtitle;
        }
        return label;
    }
}
