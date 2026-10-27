package android.content;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

public class ContentProvider {
    private Context mContext;

    public boolean onCreate() { return true; }
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) { return null; }
    public String getType(Uri uri) { return null; }
    public Uri insert(Uri uri, ContentValues values) { return null; }
    public int delete(Uri uri, String selection, String[] selectionArgs) { return 0; }
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) { return 0; }
    public Bundle call(String method, String arg, Bundle extras) { return null; }
    public Context getContext() { return mContext; }
    public final void attachInfo(Context context, Object info) { mContext = context; onCreate(); }
}
