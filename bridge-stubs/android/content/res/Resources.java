package android.content.res;

import android.util.DisplayMetrics;
import android.util.TypedValue;

public class Resources {
    public Configuration getConfiguration() { return null; }
    public DisplayMetrics getDisplayMetrics() { return null; }
    public int getIdentifier(String name, String defType, String defPackage) { return 0; }
    public int getDimensionPixelSize(int id) { return 0; }
    public String getResourceEntryName(int resid) { return ""; }
    public String getResourcePackageName(int resid) { return "com.htc.htcweatherassets"; }
    public static class Theme {
        public boolean resolveAttribute(int resid, TypedValue outValue, boolean resolveRefs) { return false; }
    }
}
