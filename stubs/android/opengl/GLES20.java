package android.opengl;

import java.nio.Buffer;

public class GLES20 {
    public static final int GL_COLOR_BUFFER_BIT = 0x4000;
    public static final int GL_BLEND = 0x0BE2;
    public static final int GL_SRC_ALPHA = 0x0302;
    public static final int GL_ONE = 1;
    public static final int GL_ZERO = 0;
    public static final int GL_ONE_MINUS_SRC_ALPHA = 0x0303;
    public static final int GL_TEXTURE_2D = 0x0DE1;
    public static final int GL_TEXTURE0 = 0x84C0;
    public static final int GL_TEXTURE_MIN_FILTER = 0x2801;
    public static final int GL_TEXTURE_MAG_FILTER = 0x2800;
    public static final int GL_LINEAR = 0x2601;
    public static final int GL_CLAMP_TO_EDGE = 0x812F;
    public static final int GL_TEXTURE_WRAP_S = 0x2802;
    public static final int GL_TEXTURE_WRAP_T = 0x2803;
    public static final int GL_VERTEX_SHADER = 0x8B31;
    public static final int GL_FRAGMENT_SHADER = 0x8B30;
    public static final int GL_COMPILE_STATUS = 0x8B81;
    public static final int GL_LINK_STATUS = 0x8B82;
    public static final int GL_TRUE = 1;
    public static final int GL_FALSE = 0;
    public static final int GL_FLOAT = 0x1406;
    public static final int GL_UNSIGNED_BYTE = 0x1401;
    public static final int GL_TRIANGLE_STRIP = 5;
    public static final int GL_RGBA = 0x1908;
    public static final int GL_ARRAY_BUFFER = 0x8892;
    public static final int GL_STATIC_DRAW = 0x88E4;
    public static final int GL_FRAMEBUFFER = 0x8D40;

    public static void glViewport(int x, int y, int width, int height) {}
    public static void glClearColor(float r, float g, float b, float a) {}
    public static void glClear(int mask) {}
    public static void glEnable(int cap) {}
    public static void glBlendFunc(int sfactor, int dfactor) {}
    public static void glDisable(int cap) {}
    public static int glCreateShader(int type) { return 1; }
    public static void glShaderSource(int shader, String source) {}
    public static void glCompileShader(int shader) {}
    public static void glGetShaderiv(int shader, int pname, int[] params, int offset) { params[offset] = GL_TRUE; }
    public static String glGetShaderInfoLog(int shader) { return ""; }
    public static void glDeleteShader(int shader) {}
    public static int glCreateProgram() { return 1; }
    public static void glAttachShader(int program, int shader) {}
    public static void glLinkProgram(int program) {}
    public static void glGetProgramiv(int program, int pname, int[] params, int offset) { params[offset] = GL_TRUE; }
    public static String glGetProgramInfoLog(int program) { return ""; }
    public static void glDeleteProgram(int program) {}
    public static void glUseProgram(int program) {}
    public static int glGetAttribLocation(int program, String name) { return 0; }
    public static int glGetUniformLocation(int program, String name) { return 0; }
    public static void glEnableVertexAttribArray(int index) {}
    public static void glVertexAttribPointer(int indx, int size, int type, boolean normalized, int stride, Buffer ptr) {}
    public static void glUniform2f(int location, float x, float y) {}
    public static void glUniform1f(int location, float x) {}
    public static void glUniform4f(int location, float x, float y, float z, float w) {}
    public static void glUniform1i(int location, int x) {}
    public static void glUniformMatrix4fv(int location, int count, boolean transpose, float[] value, int offset) {}
    public static void glDrawArrays(int mode, int first, int count) {}
    public static void glGenTextures(int n, int[] textures, int offset) { textures[offset] = 1; }
    public static void glBindTexture(int target, int texture) {}
    public static void glTexParameteri(int target, int pname, int param) {}
    public static void glTexImage2D(int target, int level, int internalformat, int width, int height, int border,
            int format, int type, Buffer pixels) {}
    public static void glActiveTexture(int texture) {}
    public static void glDeleteTextures(int n, int[] textures, int offset) {}
}
