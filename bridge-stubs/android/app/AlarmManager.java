package android.app;

public class AlarmManager {
    public static final int RTC_WAKEUP = 0;
    public static final int RTC = 1;
    public void set(int type, long triggerAtMillis, PendingIntent operation) {}
    public void setRepeating(int type, long triggerAtMillis, long intervalMillis, PendingIntent operation) {}
    public void setWindow(int type, long windowStartMillis, long windowLengthMillis, PendingIntent operation) {}
    public void setExact(int type, long triggerAtMillis, PendingIntent operation) {}
    public void setExactAndAllowWhileIdle(int type, long triggerAtMillis, PendingIntent operation) {}
    public void setAndAllowWhileIdle(int type, long triggerAtMillis, PendingIntent operation) {}
    public void setAlarmClock(AlarmClockInfo info, PendingIntent operation) {}
    public boolean canScheduleExactAlarms() { return true; }
    public void cancel(PendingIntent operation) {}

    public static class AlarmClockInfo {
        public AlarmClockInfo(long triggerTime, PendingIntent showIntent) {}
    }
}
