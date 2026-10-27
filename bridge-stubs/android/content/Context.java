package android.content;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.net.Uri;
import android.view.Display;

public class Context {
    public static final int MODE_PRIVATE = 0;
    public static final int RECEIVER_EXPORTED = 0x2;
    public static final int RECEIVER_NOT_EXPORTED = 0x4;
    public static final String SENSOR_SERVICE = "sensor";
    public static final String LOCATION_SERVICE = "location";
    public static final String WINDOW_SERVICE = "window";
    public SharedPreferences getSharedPreferences(String name, int mode) { return null; }
    public ContentResolver getContentResolver() { return null; }
    public Object getSystemService(String name) { return null; }
    public void sendBroadcast(Intent intent) {}

    public void sendBroadcast(Intent intent, String receiverPermission) {}
    public String getPackageName() { return ""; }
    public Resources getResources() { return null; }
    public AssetManager getAssets() { return null; }
    public Context getApplicationContext() { return this; }
    public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter) { return null; }
    public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter, int flags) { return null; }
    public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter, String permission, android.os.Handler scheduler) { return null; }
    public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter, String permission, android.os.Handler scheduler, int flags) { return null; }
    public void unregisterReceiver(BroadcastReceiver receiver) {}
    public ComponentName startService(Intent service) { return null; }
    public ComponentName startForegroundService(Intent service) { return null; }
    public int checkSelfPermission(String permission) { return 0; }
    public void startActivity(Intent intent) {}
    public android.content.pm.PackageManager getPackageManager() { return null; }
    public Context createPackageContext(String packageName, int flags) throws android.content.pm.PackageManager.NameNotFoundException {
        return this;
    }

    public Display getDisplay() {
        return null;
    }
}
