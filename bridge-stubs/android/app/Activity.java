package android.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

public class Activity extends Context {
    public android.content.res.Resources.Theme getTheme() { return null; }
    protected void onCreate(Bundle savedInstanceState) {}
    protected void onDestroy() {}
    public void setContentView(int layoutResID) {}
    public void setContentView(android.view.View view) {}
    public void runOnUiThread(Runnable action) {}
    public void finish() {}
    public void requestPermissions(String[] permissions, int requestCode) {}
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {}
    public Intent getIntent() { return null; }
    public LayoutInflater getLayoutInflater() { return null; }
    public View findViewById(int id) { return null; }
    public CharSequence getText(int id) { return ""; }
    public String getString(int id) { return ""; }
}
