package cn.sxw.android.lib.mvp.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.Toast;

import cn.sxw.android.base.mvp.IPresenter;
import cn.sxw.android.base.mvp.IViewAdvance;
import cn.sxw.android.base.ui.BaseActivity;
import cn.sxw.android.lib.R;

/**
 * Created by ZengCS on 2019/1/8.
 * E-mail:zcs@sxw.cn
 * Add:成都市天府软件园E3-3F
 */
public abstract class BaseActivityAdv<P extends IPresenter> extends BaseActivity<P> implements IViewAdvance, SwipeRefreshLayout.OnRefreshListener {
    protected static final int PAGE_SIZE = 15;
    protected int currPage = 1;
    protected boolean hasMoreData = false;
    protected View notDataView, errorView, mLoadingView;

    @Nullable
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        notDataView = getLayoutInflater().inflate(R.layout.empty_view, null, false);
        notDataView.setOnClickListener(v -> onRefresh());
        errorView = getLayoutInflater().inflate(R.layout.error_view, null, false);
        errorView.setOnClickListener(v -> onRefresh());
        mLoadingView = getLayoutInflater().inflate(R.layout.loading_view, null, false);
    }

    protected SwipeRefreshLayout mSwipeRefreshLayout;

    protected void addSwipeRefreshAbility() {
        mSwipeRefreshLayout = findViewById(R.id.swipe_refresh_layout_common);
        if (mSwipeRefreshLayout == null) return;
        mSwipeRefreshLayout.setColorSchemeColors(Color.parseColor("#49b271"));
        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public boolean useEventBus() {
        return false;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {
        if (mSwipeRefreshLayout != null)
            mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void launchActivity(Intent intent) {

    }

    @Override
    public void launchActivity(Class clz) {

    }

    @Override
    public void killMyself() {

    }

    @Override
    public boolean isNetworkConnected() {
        return false;
    }

    @Override
    public void showLoading(String msg) {

    }

    @Override
    public void forceShowLoading(String msg) {

    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showShortcutMenu() {

    }

    @Override
    public void hideShortcutMenu() {

    }

    @Override
    public void hideSoftInput() {

    }

    @Override
    public Context getAttachedContext() {
        return this;
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    /**
     * 打开应用程序设置界面
     */
    protected void openAppDetailSettings() {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.fromParts("package", getPackageName(), null));
        startActivity(intent);
    }

}
