package cn.sxw.android.base.net.bean;

import cn.sxw.android.base.cache.SharedPreferencesUtil;

/**
 * Created by ZengCS on 2018/5/24.
 * E-mail:zcs@sxw.cn
 * Add:成都市天府软件园E3-3F
 */

public class LocalTokenCache {
    private static String CURR_TIME_TOKEN = "KEY_CURR_TIME_TOKEN";
    private static String CURR_TIME_REFRESH_TOKEN = "KEY_CURR_TIME_REFRESH_TOKEN";

    public static String getLocalCacheToken() {
        return SharedPreferencesUtil.getString(CURR_TIME_TOKEN, "");
    }

    public static void setLocalCacheToken(String token) {
        SharedPreferencesUtil.setParam(CURR_TIME_TOKEN, token);
    }

    public static String getLocalCacheRefreshToken() {
        return SharedPreferencesUtil.getString(CURR_TIME_REFRESH_TOKEN, "");
    }

    public static void setLocalCacheRefreshToken(String refreshToken) {
        SharedPreferencesUtil.setParam(CURR_TIME_REFRESH_TOKEN, refreshToken);
    }

    public static void cleanTokenCache() {
        setLocalCacheToken("");
        setLocalCacheRefreshToken("");
    }
}
