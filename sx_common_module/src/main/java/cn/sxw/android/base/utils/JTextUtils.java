package cn.sxw.android.base.utils;

import android.text.TextUtils;

/**
 * Created by ZengCS on 2019/1/9.
 * E-mail:zcs@sxw.cn
 * Add:成都市天府软件园E3-3F
 */

public class JTextUtils {

    public static boolean isJsonString(String s) {
        if (TextUtils.isEmpty(s))
            return false;
        return s.startsWith("{") && s.endsWith("}") || s.startsWith("[") && s.endsWith("]");
    }

    public static boolean isJsonObject(String s) {
        if (TextUtils.isEmpty(s))
            return false;
        return s.startsWith("{") && s.endsWith("}");
    }

    public static boolean isJsonList(String s) {
        if (TextUtils.isEmpty(s))
            return false;
        return s.startsWith("[") && s.endsWith("]");
    }

    public static boolean isBoolean(String s) {
        return "true".equalsIgnoreCase(s) || "false".equalsIgnoreCase(s);
    }
}
