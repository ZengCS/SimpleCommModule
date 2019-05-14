package cn.sxw.android.lib.camera.core;

import android.content.Context;

/**
 * Created by ZengCS on 2019/5/13.
 * E-mail:zcs@sxw.cn
 * Add:成都市天府软件园E3-3F
 */
public class CameraApp {
    private static Context app;

    public static void init(Context context) {
        app = context.getApplicationContext();
    }

    public static Context getApp() {
        return app;
    }
}
