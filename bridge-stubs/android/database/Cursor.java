package android.database;

public interface Cursor {
    boolean moveToNext();
    int getCount();
    int getColumnIndex(String columnName);
    String getString(int columnIndex);
    long getLong(int columnIndex);
    int getInt(int columnIndex);
    void close();
}
