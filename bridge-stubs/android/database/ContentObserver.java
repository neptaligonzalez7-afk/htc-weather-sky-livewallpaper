package android.database;

import android.net.Uri;
import android.os.Handler;

public class ContentObserver {
    public ContentObserver(Handler handler) {}
    public void onChange(boolean selfChange) {}
    public void onChange(boolean selfChange, Uri uri) {}
}
