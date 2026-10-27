package com.htc.android.wallpaper.weather;

import android.view.SurfaceHolder;

/** Legacy helper; sky video uses {@link LwpGlVideo} for uniform center-crop. */
final class LwpVideoFit {
    private LwpVideoFit() {}

    static void prepareSurface(SurfaceHolder holder, int width, int height) {
        // Do not call setFixedSize — it caused non-uniform stretch on some devices.
    }
}
