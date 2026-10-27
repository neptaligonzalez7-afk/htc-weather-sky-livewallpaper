package android.app;

import android.content.Context;

public class Notification {
    public static class Builder {
        public Builder(Context context) {}
        public Builder(Context context, String channelId) {}
        public Builder setContentTitle(CharSequence title) { return this; }
        public Builder setContentText(CharSequence text) { return this; }
        public Builder setSmallIcon(int icon) { return this; }
        public Builder setOngoing(boolean ongoing) { return this; }
        public Builder setSilent(boolean silent) { return this; }
        public Builder setPriority(int pri) { return this; }
        public Builder setCategory(String category) { return this; }
        public Builder setContentIntent(android.app.PendingIntent intent) { return this; }
        public Notification build() { return new Notification(); }
    }
}
