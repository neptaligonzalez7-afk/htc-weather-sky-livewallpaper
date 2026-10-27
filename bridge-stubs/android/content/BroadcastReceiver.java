package android.content;

public class BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {}
    public PendingResult goAsync() { return new PendingResult(); }
    public static class PendingResult {
        public void finish() {}
    }
}
