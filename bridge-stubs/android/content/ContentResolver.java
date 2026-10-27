package android.content;

import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;

public class ContentResolver {
    public void notifyChange(Uri uri, ContentObserver observer) {}
    public Bundle call(Uri uri, String method, String arg, Bundle extras) { return null; }
    public android.database.Cursor query(Uri uri, String[] projection, String selection,
            String[] selectionArgs, String sortOrder) { return null; }
    public void registerContentObserver(Uri uri, boolean notifyForDescendants,
            ContentObserver observer) {}
    public void unregisterContentObserver(ContentObserver observer) {}
    public Uri insert(Uri uri, ContentValues values) { return uri; }
}
