package cn.sxw.android.base.cache;

import android.content.Context;
import android.content.SharedPreferences;

import cn.sxw.android.base.ui.BaseApplication;
import cn.sxw.android.base.utils.LogUtil;

/**
 * SharedPreferences的一个工具类，调用setParam就能保存String, Integer, Boolean, Float,
 * Long类型的参数,同样调用getParam就能获取到保存在手机里面的数据
 *
 * @author ZengCS
 * @since 2014年10月15日
 */
public class SharedPreferencesUtil {
    /**
     * 保存在手机里面的文件名
     */
    private static final String FILE_NAME = "SXW_LAUNCHER_V1";

    public static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
    }

    public static String getCustomKey(String key, String accountId) {
        return key + "_" + accountId;
    }

    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param key
     * @param object
     */
    public static void setParam(String key, Object object) {
        try {
            if (object == null)
                return;
            Context context = BaseApplication.getContext();
            String type = object.getClass().getSimpleName();
            SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();

            if ("String".equals(type)) {
                editor.putString(key, (String) object);
            } else if ("Integer".equals(type)) {
                editor.putInt(key, (Integer) object);
            } else if ("Boolean".equals(type)) {
                editor.putBoolean(key, (Boolean) object);
            } else if ("Float".equals(type)) {
                editor.putFloat(key, (Float) object);
            } else if ("Long".equals(type)) {
                editor.putLong(key, (Long) object);
            }

            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e(e);
        }
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param key
     * @param defaultObject
     * @return
     */
    public static Object getParam(String key, Object defaultObject) {
        Context context = BaseApplication.getContext();

        String type = defaultObject.getClass().getSimpleName();
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);

        if ("String".equals(type)) {
            return sp.getString(key, (String) defaultObject);
        } else if ("Integer".equals(type)) {
            return sp.getInt(key, (Integer) defaultObject);
        } else if ("Boolean".equals(type)) {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if ("Float".equals(type)) {
            return sp.getFloat(key, (Float) defaultObject);
        } else if ("Long".equals(type)) {
            return sp.getLong(key, (Long) defaultObject);
        }
        return null;
    }

    public static Boolean getBoolean(String key, Object defaultObject) {
        return (Boolean) getParam(key, defaultObject);
    }

    public static String getString(String key, Object defaultObject) {
        return (String) getParam(key, defaultObject);
    }

    public static int getInt(String key, Object defaultObject) {
        return (int) getParam(key, defaultObject);
    }

}
