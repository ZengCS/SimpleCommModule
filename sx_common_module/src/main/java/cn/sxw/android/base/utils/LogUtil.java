package cn.sxw.android.base.utils;

import android.util.Log;

import org.apache.log4j.Logger;

import cn.sxw.android.BuildConfig;

/**
 * Created by ZengCS on 2017/9/5.
 * E-mail:zcs@sxw.cn
 * Add:成都市天府软件园E3-3F
 */
public abstract class LogUtil {
    public static final int TYPE_V = 0;
    public static final int TYPE_I = 1;
    public static final int TYPE_D = 2;
    public static final int TYPE_W = 3;
    public static final int TYPE_E = 4;

    protected static final String TAG = "Debug/SXJY";
    protected static final String TAG_HTTP = "Debug/Http";
    private static Logger log = Logger.getLogger(TAG);
    private static boolean isDebug = true;
    private static boolean useLog4j = !BuildConfig.DEBUG;

    protected abstract void showFloatLog(int type, String tag, String msg);

    public static void e(String tag, String msg) {
        Log.e(tag, msg);
        if (useLog4j)
            log.error(msg);
    }

    public static void e(Throwable e) {
        log.error("------------------------------------------------------------");
        log.error(Log.getStackTraceString(e));
        log.error("------------------------------------------------------------");
    }

    public static void e(String tag, String msg, Throwable e) {
        Log.e(tag, msg, e);
        if (useLog4j)
            if (e != null)
                e(e);
            else
                log.error(msg);
    }

    public static void w(String tag, String msg) {
        Log.w(tag, msg);
        if (useLog4j)
            log.warn(msg);
    }

    public static void i(String tag, String msg) {
        if (isDebug)
            Log.i(tag, msg);
        if (useLog4j)
            log.info(msg);
    }

    public static void d(String tag, String msg) {
        if (isDebug)
            Log.w(tag, msg);
        if (useLog4j)
            log.info(msg);
    }

    public static void v(String tag, String msg) {
        if (isDebug)
            Log.v(tag, msg);
        if (useLog4j)
            log.info(msg);
    }

    // 无TAG方法
    public static void e(String msg) {
        e(TAG, msg);
    }

    public static void e(String msg, Throwable e) {
        e(TAG, msg, e);
    }

    public static void w(String msg) {
        w(TAG, msg);
    }

    public static void i(String msg) {
        i(TAG, msg);
    }

    public static void d(String msg) {
        d(TAG, msg);
    }

    public static void v(String msg) {
        v(TAG, msg);
    }

    public static void methodStart(String msg) {
        w(TAG, "******************** Start " + msg + " ********************");
    }

    public static void methodStep(String msg) {
        w(TAG, "* --> " + msg);
    }


    public static void methodStartHttp(String msg) {
        w(TAG_HTTP, "******************** Start " + msg + " ********************");
    }

    public static void methodStepHttp(String msg) {
        w(TAG_HTTP, "* --> " + msg);
    }

    public static void methodStepError(String msg) {
        e(TAG, "* !!! " + msg);
    }

    public static void methodEnd(String msg) {
        w(TAG, "******************** End " + msg + " ********************");
    }

    public static void onReceiveEvent(String msg) {
        w(TAG, "【EventBus-Receive】" + msg);
    }

    public static void postEvent(String msg) {
        w(TAG, "【EventBus-Post】" + msg);
    }
}
