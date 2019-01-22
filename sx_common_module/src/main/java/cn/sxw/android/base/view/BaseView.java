package cn.sxw.android.base.view;

/**
 * Created by Alex.Tang on 2017-03-13.
 */

public interface BaseView {

    void showLoading();

    void hideLoading();

    void hideKeyboard();

    boolean isNetworkConnected();

}
