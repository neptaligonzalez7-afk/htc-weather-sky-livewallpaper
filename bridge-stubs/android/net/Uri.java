package android.net;

public class Uri {
    public static Uri parse(String uriString) { return new Uri(); }
    public String getPath() { return ""; }
    public String getAuthority() { return ""; }
    public Builder buildUpon() { return new Builder(); }
    public static class Builder {
        public Builder appendPath(String path) { return this; }
        public Uri build() { return new Uri(); }
    }
}
