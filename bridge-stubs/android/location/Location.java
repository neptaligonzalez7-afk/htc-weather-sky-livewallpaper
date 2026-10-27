package android.location;

public class Location {
    public double getLatitude() { return 0; }
    public double getLongitude() { return 0; }
    public long getTime() { return 0; }
    public boolean hasAccuracy() { return false; }
    public float getAccuracy() { return 0f; }
    public String getProvider() { return ""; }
    public float distanceTo(Location dest) { return 0f; }
}
