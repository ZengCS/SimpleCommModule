package cn.sxw.android.base.mvp;

import android.content.Intent;

public interface IView {

    /**
     * 显示加载
     */
    void showLoading();

    /**
     * 隐藏加载
     */
    void hideLoading();

    /**
     * 显示信息
     */
    void showMessage(String message);

    /**
     * 跳转activity
     */
    void launchActivity(Intent intent);

    /**
     * 跳转activity
     */
    void launchActivity(Class clz);

    /**
     * 杀死自己
     */
    void killMyself();

    /**
     * 判断网络连接
     *
     * @return
     */
    boolean isNetworkConnected();
}
