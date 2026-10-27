package android.content;

import android.os.Parcelable;

public class Intent {
    public static final String ACTION_TIME_TICK = "android.intent.action.TIME_TICK";
    public static final String ACTION_TIME_CHANGED = "android.intent.action.TIME_SET";
    public static final String ACTION_TIMEZONE_CHANGED = "android.intent.action.TIMEZONE_CHANGED";
    public static final String ACTION_BOOT_COMPLETED = "android.intent.action.BOOT_COMPLETED";
    public static final String ACTION_MY_PACKAGE_REPLACED = "android.intent.action.MY_PACKAGE_REPLACED";
    public static final String ACTION_SCREEN_ON = "android.intent.action.SCREEN_ON";
    public static final String ACTION_SCREEN_OFF = "android.intent.action.SCREEN_OFF";
    public static final String ACTION_USER_PRESENT = "android.intent.action.USER_PRESENT";
    public static final String ACTION_USER_UNLOCKED = "android.intent.action.USER_UNLOCKED";
    public static final String ACTION_LOCALE_CHANGED = "android.intent.action.LOCALE_CHANGED";
    public static final String ACTION_VIEW = "android.intent.action.VIEW";
    public static final int FLAG_ACTIVITY_NEW_TASK = 0x10000000;
    public Intent() {}
    public Intent(String action) {}
    public Intent(Context context, Class<?> cls) {}
    public Intent setClassName(String pkg, String cls) { return this; }
    public Intent setPackage(String pkg) { return this; }
    public Intent setAction(String action) { return this; }
    public Intent setData(android.net.Uri data) { return this; }
    public Intent addFlags(int flags) { return this; }
    public Intent addCategory(String category) { return this; }
    public Intent putExtra(String name, int value) { return this; }
    public Intent putExtra(String name, long value) { return this; }
    public Intent putExtra(String name, String value) { return this; }
    public Intent putExtra(String name, int[] value) { return this; }
    public Intent putExtra(String name, Parcelable[] value) { return this; }
    public String getAction() { return null; }
    public String getStringExtra(String name) { return null; }
    public <T> T getParcelableExtra(String name) { return null; }
    public int getIntExtra(String name, int def) { return def; }
    public long getLongExtra(String name, long def) { return def; }
    public Parcelable[] getParcelableArrayExtra(String name) { return null; }
}
