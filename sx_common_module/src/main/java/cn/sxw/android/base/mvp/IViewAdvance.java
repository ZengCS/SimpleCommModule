package cn.sxw.android.base.mvp;

import android.app.Activity;
import android.content.Context;

/**
 * Created by ZengCS on 2017/7/20.
 * E-mail:zcs@sxw.cn
 * Add:成都市天府软件园E3-3F
 */

public interface IViewAdvance extends IView {
    int STATUS_DISCONNECT = 0;// 未连接
    int STATUS_NORMAL = 1;// 正常
    int STATUS_TEACHING = 3;// 上课中

    /**
     * 显示加载
     */
    void showLoading(String msg);

    /**
     * 强制显示加载框
     */
    void forceShowLoading(String msg);

    /**
     * 显示信息
     */
    void showToast(String message);

    /**
     * 显示快捷菜单
     */
    void showShortcutMenu();

    /**
     * 隐藏快捷菜单
     */
    void hideShortcutMenu();

    /**
     * 隐藏软键盘
     */
    void hideSoftInput();

    Context getAttachedContext();

    Activity getActivity();
}
