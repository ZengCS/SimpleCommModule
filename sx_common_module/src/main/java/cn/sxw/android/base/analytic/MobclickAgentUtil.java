package cn.sxw.android.base.analytic;

import android.content.Context;

import com.umeng.analytics.MobclickAgent;

/**
 * Created by ZengCS on 2017/11/28.
 * E-mail:zcs@sxw.cn
 * Add:成都市天府软件园E3-3F
 */
public class MobclickAgentUtil {
    /**
     * 用户事件监听
     *
     * @param context   上下文
     * @param key   事件ID
     */
    public static void onEvent(Context context, String key) {
        try {
            MobclickAgent.onEvent(context, key);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 用户手动上报移除到Umeng
     *
     * @param context   上下文
     * @param e 异常对象
     */
    public static void reportError(Context context, Throwable e) {
        try {
            MobclickAgent.reportError(context, e);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    /**
     * 用户手动上报移除到Umeng
     *
     * @param context   上下文
     * @param message   异常信息
     */
    public static void reportError(Context context, String message) {
        try {
            MobclickAgent.reportError(context, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
