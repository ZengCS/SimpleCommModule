package cn.sxw.android.lib.camera.core;

import android.content.Context;

import java.lang.ref.SoftReference;

/**
 * Created by ZengCS on 2019/5/13.
 * E-mail:zcs@sxw.cn
 * Add:成都市天府软件园E3-3F
 */
public class CameraApp {
    private static SoftReference<Context> ctx;

    public static void init(Context context) {
        ctx = new SoftReference<>(context);
    }

    public static Context getApp() {
        return ctx.get();
    }
}
