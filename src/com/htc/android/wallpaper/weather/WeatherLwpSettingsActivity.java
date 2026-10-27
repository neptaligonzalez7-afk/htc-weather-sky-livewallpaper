package com.htc.android.wallpaper.weather;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Location for standalone sky wallpaper: auto (GPS / approximate IP) or manual city search.
 */
public class WeatherLwpSettingsActivity extends Activity {
    private static final int REQ_LOCATION = 9001;

    private TextView mStatus;
    private EditText mCity;
    private Button mAutoBtn;
    private Button mManualBtn;
    private Button mSearchBtn;
    private ListView mCityList;
    private GeocodePlace[] mSearchResults = new GeocodePlace[0];
    private BroadcastReceiver mWeatherReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ScrollView scroll = new ScrollView(this);
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(48, 48, 48, 48);
        root.setBackgroundColor(0xFF1A1A2E);
        scroll.addView(root);

        TextView title = new TextView(this);
        title.setText("HTC Weather Sky — Location");
        title.setTextColor(0xFFFFFFFF);
        title.setTextSize(20f);
        root.addView(title);

        mStatus = new TextView(this);
        mStatus.setTextColor(0xFFCCCCCC);
        mStatus.setTextSize(14f);
        mStatus.setPadding(0, 24, 0, 24);
        root.addView(mStatus);

        mAutoBtn = new Button(this);
        mAutoBtn.setText("Automatic (GPS / network)");
        root.addView(mAutoBtn);

        mManualBtn = new Button(this);
        mManualBtn.setText("Manual city search");
        root.addView(mManualBtn);

        TextView hint = new TextView(this);
        hint.setText("Search Open-Meteo, then tap the correct city:");
        hint.setTextColor(0xFFD3D3D3);
        hint.setPadding(0, 32, 0, 8);
        root.addView(hint);

        mCity = new EditText(this);
        mCity.setHint("e.g. Gainesville");
        mCity.setTextColor(0xFFFFFFFF);
        mCity.setHintTextColor(0xFF888888);
        root.addView(mCity);

        mSearchBtn = new Button(this);
        mSearchBtn.setText("Search cities");
        root.addView(mSearchBtn);

        mCityList = new ListView(this);
        mCityList.setPadding(0, 16, 0, 16);
        LinearLayout.LayoutParams listLp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                720);
        mCityList.setLayoutParams(listLp);
        root.addView(mCityList);

        setContentView(scroll);

        SharedPreferences p = LwpWeatherPrefs.prefs(this);
        mCity.setText(p.getString(LwpWeatherPrefs.KEY_MANUAL_CITY, ""));
        refreshStatus();

        mAutoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LwpWeatherPrefs.prefs(WeatherLwpSettingsActivity.this).edit()
                        .putInt(LwpWeatherPrefs.KEY_MODE, LwpWeatherPrefs.MODE_AUTO)
                        .apply();
                toast("Automatic location selected — syncing…");
                setStatusLine("Syncing GPS / network location…");
                requestLocationIfNeeded();
                refreshStatus();
                LwpWeatherSync.request(WeatherLwpSettingsActivity.this, true);
            }
        });
        mManualBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LwpWeatherPrefs.prefs(WeatherLwpSettingsActivity.this).edit()
                        .putInt(LwpWeatherPrefs.KEY_MODE, LwpWeatherPrefs.MODE_MANUAL)
                        .apply();
                toast("Manual mode — search and tap a city");
                refreshStatus();
            }
        });
        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runCitySearch();
            }
        });
        mCityList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                if (position < 0 || position >= mSearchResults.length) {
                    return;
                }
                applyPlace(mSearchResults[position]);
            }
        });

        mWeatherReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                refreshStatus();
            }
        };
        registerReceiver(mWeatherReceiver,
                new IntentFilter(LwpWeatherPrefs.ACTION_UPDATED),
                Context.RECEIVER_NOT_EXPORTED);

        requestLocationIfNeeded();
        LwpWeatherSync.request(this, false);
    }

    @Override
    protected void onDestroy() {
        if (mWeatherReceiver != null) {
            try {
                unregisterReceiver(mWeatherReceiver);
            } catch (Throwable ignored) {
            }
        }
        super.onDestroy();
    }

    private void runCitySearch() {
        android.text.Editable e = mCity.getText();
        final String query = e != null ? e.toString().trim() : "";
        if (query.length() == 0) {
            toast("Type a city name first");
            return;
        }
        LwpWeatherPrefs.prefs(this).edit()
                .putInt(LwpWeatherPrefs.KEY_MODE, LwpWeatherPrefs.MODE_MANUAL)
                .putString(LwpWeatherPrefs.KEY_MANUAL_CITY, query)
                .apply();
        setStatusLine("Searching for \"" + query + "\"…");
        mSearchBtn.setEnabled(false);
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final GeocodePlace[] found = LwpOpenMeteo.searchCities(query, 10);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mSearchBtn.setEnabled(true);
                            showSearchResults(found);
                        }
                    });
                } catch (final Throwable err) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mSearchBtn.setEnabled(true);
                            setStatusLine("Search failed: " + err.getMessage());
                            toast("Search failed");
                        }
                    });
                }
            }
        }, "CitySearch");
        t.setDaemon(true);
        t.start();
    }

    private void showSearchResults(GeocodePlace[] found) {
        mSearchResults = found != null ? found : new GeocodePlace[0];
        if (mSearchResults.length == 0) {
            setStatusLine("No cities found. Try a different spelling.");
            toast("No matches");
            mCityList.setAdapter(new ArrayAdapter<String>(this, LwpR.layout.city_list_item, new String[0]));
            return;
        }
        String[] lines = new String[mSearchResults.length];
        for (int i = 0; i < mSearchResults.length; i++) {
            lines[i] = mSearchResults[i].line();
        }
        mCityList.setAdapter(new ArrayAdapter<String>(this, LwpR.layout.city_list_item, lines));
        setStatusLine("Found " + mSearchResults.length + " — tap one to use it:");
        toast("Tap a city in the list");
    }

    private void applyPlace(GeocodePlace place) {
        if (place == null) {
            return;
        }
        LwpWeatherPrefs.prefs(this).edit()
                .putInt(LwpWeatherPrefs.KEY_MODE, LwpWeatherPrefs.MODE_MANUAL)
                .putString(LwpWeatherPrefs.KEY_MANUAL_CITY, place.label)
                .apply();
        LwpWeatherPrefs.savePlace(this, place.lat, place.lon, place.label);
        mCity.setText(place.label);
        toast("Selected: " + place.label);
        setStatusLine("Refreshing sky for " + place.label + "…");
        refreshStatus();
        LwpWeatherSync.request(this, true);
    }

    private void refreshStatus() {
        SharedPreferences p = LwpWeatherPrefs.prefs(this);
        int mode = p.getInt(LwpWeatherPrefs.KEY_MODE, LwpWeatherPrefs.MODE_AUTO);
        String label = p.getString(LwpWeatherPrefs.KEY_LABEL, "");
        String msg = LwpWeatherPrefs.getStatusMessage(this);
        WeatherSnapshot snap = LwpWeatherPrefs.readSnapshot(this);
        StringBuilder sb = new StringBuilder();
        sb.append(mode == LwpWeatherPrefs.MODE_AUTO
                ? "Mode: AUTOMATIC ✓\n"
                : "Mode: MANUAL city ✓\n");
        mAutoBtn.setText(mode == LwpWeatherPrefs.MODE_AUTO
                ? "Automatic (GPS / network) ✓"
                : "Automatic (GPS / network)");
        mManualBtn.setText(mode == LwpWeatherPrefs.MODE_MANUAL
                ? "Manual city search ✓"
                : "Manual city search");
        if (label.length() > 0) {
            sb.append("Active place: ").append(label).append('\n');
        }
        if (msg != null && msg.length() > 0) {
            sb.append(msg).append('\n');
        }
        if (snap != null) {
            sb.append("Sky: condition ").append(snap.conditionId);
            sb.append(snap.isDay ? " (day)" : " (night)");
        } else if (msg == null || msg.length() == 0) {
            sb.append("Waiting for Open-Meteo…");
        }
        mStatus.setText(sb.toString());
    }

    private void setStatusLine(String line) {
        LwpWeatherPrefs.setStatusMessage(this, line);
        refreshStatus();
    }

    private void toast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    private void requestLocationIfNeeded() {
        if (checkSelfPermission("android.permission.ACCESS_FINE_LOCATION") != 0) {
            requestPermissions(new String[] {
                    "android.permission.ACCESS_COARSE_LOCATION",
                    "android.permission.ACCESS_FINE_LOCATION"
            }, REQ_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQ_LOCATION) {
            toast("Location permission updated — syncing");
            LwpWeatherSync.request(this, true);
            refreshStatus();
        }
    }
}
