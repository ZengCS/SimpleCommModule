package cn.sxw.android.lib.camera.util;

import android.util.Log;


public class LogUtil {
    private static boolean DEBUG = false;
    private static final String DEFAULT_TAG = "ZCamera";
    private static boolean enableLogcat = false;// 允许使用logcat

    public static void i(String tag, String msg) {
        if (enableLogcat)
            Log.i(tag, msg);
    }

    public static void v(String tag, String msg) {
        if (DEBUG && enableLogcat)
            Log.v(tag, msg);
    }

    public static void d(String tag, String msg) {
        if (DEBUG && enableLogcat)
            Log.w(tag, msg);
    }

    public static void w(String tag, String msg) {
        if (DEBUG && enableLogcat)
            Log.w(tag, msg);
    }

    public static void e(String tag, String msg) {
        if (DEBUG && enableLogcat)
            Log.e(tag, msg);
    }

    public static void i(String msg) {
        i(DEFAULT_TAG, msg);
    }

    public static void v(String msg) {
        v(DEFAULT_TAG, msg);
    }

    public static void d(String msg) {
        d(DEFAULT_TAG, msg);
    }

    public static void e(String msg) {
        e(DEFAULT_TAG, msg);
    }
}
