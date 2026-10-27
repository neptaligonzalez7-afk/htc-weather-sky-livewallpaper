package android.database;

public class MatrixCursor implements Cursor {
    public MatrixCursor(String[] columns) {}
    public void addRow(Object[] columnValues) {}
    public boolean moveToNext() { return false; }
    public int getCount() { return 0; }
    public int getColumnIndex(String columnName) { return -1; }
    public String getString(int columnIndex) { return null; }
    public long getLong(int columnIndex) { return 0L; }
    public int getInt(int columnIndex) { return 0; }
    public void close() {}
}
