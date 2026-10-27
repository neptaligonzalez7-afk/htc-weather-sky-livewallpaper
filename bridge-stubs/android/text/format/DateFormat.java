package android.text.format;

import android.content.Context;

public class DateFormat {
    public static boolean is24HourFormat(Context context) {
        return false;
    }

    public static String getBestDateTimePattern(java.util.Locale locale, String skeleton) {
        return "MM/dd";
    }
}
