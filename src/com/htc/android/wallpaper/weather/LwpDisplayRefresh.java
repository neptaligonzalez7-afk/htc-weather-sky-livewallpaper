package com.htc.android.wallpaper.weather;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.WindowManager;

/**
 * Keeps the wallpaper surface on the display's high refresh rate (90/120 Hz+) where supported.
 * Sky MP4s are ~30 fps; this avoids OEMs throttling the whole home layer to 60 Hz.
 */
final class LwpDisplayRefresh {
    private static final String TAG = "WeatherVideoLwp";
    /** {@link Surface#FRAME_RATE_COMPATIBILITY_DEFAULT} */
    private static final int FRAME_RATE_DEFAULT = 0;
    /** {@link Surface#FRAME_RATE_COMPATIBILITY_AT_LEAST} (API 31+). */
    private static final int FRAME_RATE_AT_LEAST = 2;

    private LwpDisplayRefresh() {}

    static void enable(SurfaceHolder holder, Context context) {
        if (holder == null || Build.VERSION.SDK_INT < 30) {
            return;
        }
        Surface surface = holder.getSurface();
        if (surface == null || !surface.isValid()) {
            return;
        }
        float peakHz = peakRefreshRate(resolveDisplay(context));
        try {
            if (Build.VERSION.SDK_INT >= 31 && peakHz > 60f) {
                surface.setFrameRate(peakHz, FRAME_RATE_AT_LEAST);
                Log.i(TAG, "surface frame rate at-least " + peakHz + " Hz");
            } else {
                surface.setFrameRate(0f, FRAME_RATE_DEFAULT);
            }
        } catch (Throwable t) {
            Log.w(TAG, "setFrameRate enable failed", t);
        }
    }

    static void disable(SurfaceHolder holder) {
        if (holder == null || Build.VERSION.SDK_INT < 30) {
            return;
        }
        try {
            Surface surface = holder.getSurface();
            if (surface != null && surface.isValid()) {
                surface.setFrameRate(0f, FRAME_RATE_DEFAULT);
            }
        } catch (Throwable t) {
            Log.w(TAG, "setFrameRate disable failed", t);
        }
    }

    private static float peakRefreshRate(Display display) {
        float peak = 60f;
        if (display == null) {
            return peak;
        }
        try {
            Display.Mode current = display.getMode();
            if (current != null) {
                peak = Math.max(peak, current.getRefreshRate());
            }
        } catch (Throwable ignored) {
        }
        try {
            Display.Mode[] modes = display.getSupportedModes();
            if (modes != null) {
                for (int i = 0; i < modes.length; i++) {
                    if (modes[i] != null) {
                        peak = Math.max(peak, modes[i].getRefreshRate());
                    }
                }
            }
        } catch (Throwable ignored) {
        }
        return peak;
    }

    private static Display resolveDisplay(Context context) {
        if (context == null) {
            return null;
        }
        try {
            if (Build.VERSION.SDK_INT >= 30) {
                Display d = context.getDisplay();
                if (d != null) {
                    return d;
                }
            }
        } catch (Throwable t) {
            Log.w(TAG, "context.getDisplay failed", t);
        }
        try {
            Object svc = context.getSystemService(Context.WINDOW_SERVICE);
            if (svc instanceof WindowManager) {
                return ((WindowManager) svc).getDefaultDisplay();
            }
        } catch (Throwable t) {
            Log.w(TAG, "WindowManager display failed", t);
        }
        return null;
    }
}
