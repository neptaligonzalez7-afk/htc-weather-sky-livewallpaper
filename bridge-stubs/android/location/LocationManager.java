package android.location;

public class LocationManager {
    public static final String NETWORK_PROVIDER = "network";
    public static final String GPS_PROVIDER = "gps";
    public static final String PASSIVE_PROVIDER = "passive";
    public static final String KEY_LOCATION_CHANGED = "location";
    public Location getLastKnownLocation(String provider) { return null; }
    public java.util.List getProviders(boolean enabledOnly) { return null; }
    public void requestSingleUpdate(String provider, LocationListener listener, android.os.Looper looper) {}
    public void requestLocationUpdates(String provider, long minTimeMs, float minDistanceM,
            LocationListener listener) {}
    public void requestLocationUpdates(String provider, long minTime, float minDistance,
            android.app.PendingIntent intent) {}
    public void removeUpdates(LocationListener listener) {}
    public void removeUpdates(android.app.PendingIntent intent) {}
    public boolean isProviderEnabled(String provider) { return true; }
}
