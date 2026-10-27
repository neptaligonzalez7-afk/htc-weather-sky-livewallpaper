package com.htc.android.wallpaper.weather;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/** Resolves lat/lon for standalone sync: saved coords → GPS → IP → last good. */
final class LwpLocationResolver {
    private static final String TAG = "WeatherVideoLwp";

    private LwpLocationResolver() {}

    static final class Place {
        final double lat;
        final double lon;
        final String label;

        Place(double lat, double lon, String label) {
            this.lat = lat;
            this.lon = lon;
            this.label = label != null ? label : "";
        }
    }

    static Place resolve(Context context) {
        SharedPreferences p = LwpWeatherPrefs.prefs(context);
        int mode = p.getInt(LwpWeatherPrefs.KEY_MODE, LwpWeatherPrefs.MODE_AUTO);
        if (mode == LwpWeatherPrefs.MODE_MANUAL) {
            double lat = Double.longBitsToDouble(p.getLong(LwpWeatherPrefs.KEY_LAT_BITS, 0L));
            double lon = Double.longBitsToDouble(p.getLong(LwpWeatherPrefs.KEY_LON_BITS, 0L));
            String label = p.getString(LwpWeatherPrefs.KEY_LABEL, "");
            if (Math.abs(lat) > 0.01 || Math.abs(lon) > 0.01) {
                return new Place(lat, lon, label);
            }
            return null;
        }
        Place gps = tryGps(context);
        if (gps != null) {
            return gps;
        }
        Place ip = tryIp();
        if (ip != null) {
            return ip;
        }
        double lat = Double.longBitsToDouble(p.getLong(LwpWeatherPrefs.KEY_LAT_BITS, 0L));
        double lon = Double.longBitsToDouble(p.getLong(LwpWeatherPrefs.KEY_LON_BITS, 0L));
        String label = p.getString(LwpWeatherPrefs.KEY_LABEL, "Last known");
        if (Math.abs(lat) > 0.01 || Math.abs(lon) > 0.01) {
            return new Place(lat, lon, label);
        }
        return null;
    }

    private static Place tryGps(Context context) {
        if (context.checkSelfPermission("android.permission.ACCESS_COARSE_LOCATION") != 0
                && context.checkSelfPermission("android.permission.ACCESS_FINE_LOCATION") != 0) {
            return null;
        }
        try {
            LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            if (lm == null) {
                return null;
            }
            Location best = pickCached(lm);
            if (best != null && ageMs(best) < 90L * 60L * 1000L) {
                return new Place(best.getLatitude(), best.getLongitude(), "Current location");
            }
            Location fresh = requestFresh(lm, 10000L);
            if (fresh != null) {
                return new Place(fresh.getLatitude(), fresh.getLongitude(), "Current location");
            }
            if (best != null) {
                return new Place(best.getLatitude(), best.getLongitude(), "Current location");
            }
        } catch (Throwable t) {
            Log.w(TAG, "gps failed", t);
        }
        return null;
    }

    private static long ageMs(Location loc) {
        return System.currentTimeMillis() - loc.getTime();
    }

    private static Location pickCached(LocationManager lm) {
        Location best = null;
        String[] names = new String[] {
                "fused",
                LocationManager.NETWORK_PROVIDER,
                LocationManager.PASSIVE_PROVIDER,
                LocationManager.GPS_PROVIDER
        };
        for (int i = 0; i < names.length; i++) {
            try {
                Location loc = lm.getLastKnownLocation(names[i]);
                if (better(loc, best)) {
                    best = loc;
                }
            } catch (Throwable ignored) {
            }
        }
        return best;
    }

    private static Location requestFresh(LocationManager lm, long timeoutMs) {
        final Location[] box = new Location[1];
        final Object lock = new Object();
        LocationListener listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                synchronized (lock) {
                    if (better(location, box[0])) {
                        box[0] = location;
                    }
                    lock.notifyAll();
                }
            }
        };
        String[] providers = new String[] { "fused", LocationManager.NETWORK_PROVIDER, LocationManager.GPS_PROVIDER };
        for (int i = 0; i < providers.length; i++) {
            try {
                if (lm.isProviderEnabled(providers[i])) {
                    lm.requestSingleUpdate(providers[i], listener, null);
                }
            } catch (Throwable ignored) {
            }
        }
        synchronized (lock) {
            long deadline = System.currentTimeMillis() + timeoutMs;
            while (box[0] == null && System.currentTimeMillis() < deadline) {
                try {
                    lock.wait(500L);
                } catch (InterruptedException ignored) {
                    break;
                }
            }
        }
        try {
            lm.removeUpdates(listener);
        } catch (Throwable ignored) {
        }
        return box[0];
    }

    private static boolean better(Location cand, Location best) {
        if (cand == null) {
            return false;
        }
        if (best == null) {
            return true;
        }
        return cand.getTime() > best.getTime();
    }

    private static Place tryIp() {
        double[] a = ipCoords("https://ipwho.is/");
        if (a == null) {
            a = ipCoords("https://ipapi.co/json/");
        }
        if (a == null) {
            return null;
        }
        return new Place(a[0], a[1], "Approximate location");
    }

    private static double[] ipCoords(String spec) {
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(spec).openConnection();
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(12000);
            conn.setRequestProperty("User-Agent", "HTC-Weather-Sky-LWP/1.1");
            InputStream in = conn.getInputStream();
            byte[] buf = new byte[4096];
            StringBuilder sb = new StringBuilder();
            int n;
            while ((n = in.read(buf)) >= 0) {
                sb.append(new String(buf, 0, n, StandardCharsets.UTF_8));
            }
            in.close();
            conn.disconnect();
            JSONObject root = new JSONObject(sb.toString());
            if (root.has("latitude") && root.has("longitude")) {
                return new double[] { root.getDouble("latitude"), root.getDouble("longitude") };
            }
        } catch (Throwable ignored) {
        }
        return null;
    }
}
