package com.htc.android.wallpaper.weather;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/** Standalone Open-Meteo fetch for the live wallpaper (no HTC Weather / widget). */
final class LwpOpenMeteo {
    private LwpOpenMeteo() {}

    static final class Result {
        final int conditionId;
        final boolean isDay;
        final double lat;
        final double lon;
        final String label;

        Result(int conditionId, boolean isDay, double lat, double lon, String label) {
            this.conditionId = conditionId;
            this.isDay = isDay;
            this.lat = lat;
            this.lon = lon;
            this.label = label != null ? label : "";
        }
    }

    static Result fetchCurrent(double lat, double lon, String label) throws Exception {
        String url = "https://api.open-meteo.com/v1/forecast?latitude=" + lat + "&longitude=" + lon
                + "&current=weather_code,is_day"
                + "&timezone=auto&forecast_days=1";
        JSONObject root = getJson(url);
        JSONObject cur = root.getJSONObject("current");
        boolean day = cur.optInt("is_day", 1) == 1;
        int wmo = cur.optInt("weather_code", 0);
        int cond = WmoToHtcCondition.map(wmo, day);
        return new Result(cond, day, lat, lon, label);
    }

    static GeocodePlace[] searchCities(String query, int max) throws Exception {
        String q = URLEncoder.encode(query.trim(), "UTF-8");
        int count = max < 1 ? 8 : Math.min(max, 15);
        String url = "https://geocoding-api.open-meteo.com/v1/search?name=" + q
                + "&count=" + count + "&language=en&format=json";
        JSONObject root = getJson(url);
        JSONArray results = root.optJSONArray("results");
        if (results == null || results.length() == 0) {
            return new GeocodePlace[0];
        }
        GeocodePlace[] out = new GeocodePlace[results.length()];
        for (int i = 0; i < results.length(); i++) {
            JSONObject row = results.getJSONObject(i);
            out[i] = parsePlace(row);
        }
        return out;
    }

    static GeocodePlace geocodeFirst(String query) throws Exception {
        GeocodePlace[] list = searchCities(query, 1);
        if (list.length == 0) {
            throw new IllegalArgumentException("City not found");
        }
        return list[0];
    }

    private static GeocodePlace parsePlace(JSONObject row) {
        String name = row.optString("name", "");
        String admin = row.optString("admin1", "");
        String country = row.optString("country", "");
        String label = name;
        if (admin.length() > 0) {
            label = name + ", " + admin;
        }
        String subtitle = country;
        if (row.has("latitude") && row.has("longitude")) {
            try {
                double lat = row.getDouble("latitude");
                double lon = row.getDouble("longitude");
                return new GeocodePlace(lat, lon, label, subtitle);
            } catch (Throwable ignored) {
            }
        }
        return new GeocodePlace(0, 0, label, subtitle);
    }

    private static JSONObject getJson(String spec) throws Exception {
        HttpURLConnection conn = (HttpURLConnection) new URL(spec).openConnection();
        conn.setConnectTimeout(15000);
        conn.setReadTimeout(20000);
        conn.setRequestProperty("User-Agent", "HTC-Weather-Sky-LWP/1.1");
        try {
            InputStream in = conn.getInputStream();
            byte[] buf = new byte[8192];
            StringBuilder sb = new StringBuilder();
            int n;
            while ((n = in.read(buf)) >= 0) {
                sb.append(new String(buf, 0, n, StandardCharsets.UTF_8));
            }
            in.close();
            return new JSONObject(sb.toString());
        } finally {
            conn.disconnect();
        }
    }
}
