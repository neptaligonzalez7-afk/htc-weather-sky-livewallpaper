package android.opengl;

public class EGL14 {
    public static final int EGL_DEFAULT_DISPLAY = 0;
    public static final EGLContext EGL_NO_CONTEXT = new EGLContext();
    public static final EGLSurface EGL_NO_SURFACE = new EGLSurface();
    public static final EGLDisplay EGL_NO_DISPLAY = new EGLDisplay();
    public static final int EGL_NONE = 0x3038;
    public static final int EGL_RENDERABLE_TYPE = 0x3040;
    public static final int EGL_OPENGL_ES2_BIT = 4;
    public static final int EGL_RED_SIZE = 0x3024;
    public static final int EGL_GREEN_SIZE = 0x3023;
    public static final int EGL_BLUE_SIZE = 0x3022;
    public static final int EGL_ALPHA_SIZE = 0x3021;
    public static final int EGL_SURFACE_TYPE = 0x3033;
    public static final int EGL_WINDOW_BIT = 4;
    public static final int EGL_CONTEXT_CLIENT_VERSION = 0x3098;
    public static final int EGL_SUCCESS = 0x3000;

    public static EGLDisplay eglGetDisplay(int displayId) {
        return new EGLDisplay();
    }

    public static boolean eglInitialize(EGLDisplay dpy, int[] major, int majorOffset, int[] minor, int minorOffset) {
        return true;
    }

    public static boolean eglChooseConfig(EGLDisplay dpy, int[] attrib_list, int attrib_listOffset,
            EGLConfig[] configs, int configsOffset, int config_size, int[] num_config, int num_configOffset) {
        return true;
    }

    public static EGLContext eglCreateContext(EGLDisplay dpy, EGLConfig config, EGLContext share_context,
            int[] attrib_list, int offset) {
        return new EGLContext();
    }

    public static EGLSurface eglCreateWindowSurface(EGLDisplay dpy, EGLConfig config, Object win,
            int[] attrib_list, int offset) {
        return new EGLSurface();
    }

    public static boolean eglMakeCurrent(EGLDisplay dpy, EGLSurface draw, EGLSurface read, EGLContext ctx) {
        return true;
    }

    public static boolean eglSwapBuffers(EGLDisplay dpy, EGLSurface surface) {
        return true;
    }

    public static boolean eglDestroySurface(EGLDisplay dpy, EGLSurface surface) {
        return true;
    }

    public static boolean eglDestroyContext(EGLDisplay dpy, EGLContext ctx) {
        return true;
    }

    public static boolean eglTerminate(EGLDisplay dpy) {
        return true;
    }

    public static int eglGetError() {
        return EGL_SUCCESS;
    }
}
