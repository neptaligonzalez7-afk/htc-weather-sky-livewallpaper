package android.content;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class ContentValues {
    private final HashMap<String, Object> m = new HashMap<String, Object>();
    public void put(String key, String value) { m.put(key, value); }
    public void put(String key, Integer value) { m.put(key, value); }
    public String getAsString(String key) { Object v = m.get(key); return v == null ? null : String.valueOf(v); }
    public Integer getAsInteger(String key) {
        Object v = m.get(key);
        if (v instanceof Integer) return (Integer) v;
        try { return v == null ? null : Integer.valueOf(String.valueOf(v)); } catch (Exception e) { return null; }
    }
    public Object get(String key) { return m.get(key); }
    public Set<String> keySet() { return m.keySet(); }
}
