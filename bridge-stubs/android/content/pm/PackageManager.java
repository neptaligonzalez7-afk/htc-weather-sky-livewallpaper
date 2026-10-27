package android.content.pm;

import android.content.Intent;
import android.content.res.Resources;

public class PackageManager {
    public static final int PERMISSION_GRANTED = 0;
    public static final int PERMISSION_DENIED = -1;
    public Intent getLaunchIntentForPackage(String packageName) { return null; }
    public Resources getResourcesForApplication(String packageName) throws NameNotFoundException {
        return null;
    }
    public static class NameNotFoundException extends Exception {}
}
