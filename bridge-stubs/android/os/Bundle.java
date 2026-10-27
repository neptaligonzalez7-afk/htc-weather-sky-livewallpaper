package android.os;

import java.util.ArrayList;

public class Bundle {
    public void putBoolean(String key, boolean value) {}
    public void putInt(String key, int value) {}
    public void putString(String key, String value) {}
    public boolean containsKey(String key) { return false; }
    public Object get(String key) { return null; }
    public String getString(String key) { return null; }
    public Bundle getBundle(String key) { return null; }
    public Parcelable getParcelable(String key) { return null; }
    public <T extends Parcelable> T getParcelable(String key, Class<T> clazz) { return null; }
    public ArrayList getParcelableArrayList(String key) { return null; }
    public <T> ArrayList<T> getParcelableArrayList(String key, Class<T> clazz) { return null; }
}
