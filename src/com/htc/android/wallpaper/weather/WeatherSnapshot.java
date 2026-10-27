package com.htc.android.wallpaper.weather;

/** Current sky condition for video selection. */
final class WeatherSnapshot {
    final int conditionId;
    final boolean isDay;
    final long updatedMs;

    WeatherSnapshot(int conditionId, boolean isDay, long updatedMs) {
        this.conditionId = conditionId;
        this.isDay = isDay;
        this.updatedMs = updatedMs;
    }
}
