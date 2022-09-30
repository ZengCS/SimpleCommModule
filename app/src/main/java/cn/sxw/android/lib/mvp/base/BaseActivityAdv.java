package cn.sxw.android.lib.mvp.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import cn.sxw.android.base.dialog.CustomDialogHelper;
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

    protected boolean isLoading = false;
    private boolean isFullScreen = false;
    private AlertDialog progressDialog;

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
        if (isFinishing()) return;
        showLoading(getString(R.string.txt_loading_common));
    }

    @Override
    public void hideLoading() {
        if (mSwipeRefreshLayout != null)
            mSwipeRefreshLayout.setRefreshing(false);
        if (isFinishing()) return;
        isLoading = false;
        if (progressDialog != null)
            progressDialog.dismiss();
        if (isFullScreen)
            requestFullScreen(null);
    }

    /**
     * 全屏
     */
    protected void requestFullScreen(Boolean keepScreenOn) {
        isFullScreen = true;
        if (Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        } else {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(option);
        }
        // 设置常亮
        if (keepScreenOn != null && keepScreenOn)
            setKeepScreenOn();
    }

    /**
     * 设置屏幕常亮
     */
    protected void setKeepScreenOn() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }


    @Override
    public void showMessage(String message) {
        if (isFinishing()) return;
        hideLoading();
        if (!isFinishing())
            CustomDialogHelper.showCustomMessageDialog(this, message);
    }

    public void showExitMessageDialog(String msg) {
        if (isFinishing()) return;
        CustomDialogHelper.DialogParam dialogParam = new CustomDialogHelper.DialogParam();
        dialogParam.setMessage(msg);
        dialogParam.setPositiveBtnText("退出");
        CustomDialogHelper.showCustomMessageDialog(this, dialogParam, new CustomDialogHelper.NativeDialogCallback() {
            @Override
            public void onConfirm() {
                killMyself();
            }

            @Override
            public void onCancel() {
            }
        });
    }


    @Override
    public void launchActivity(Intent intent) {
        startActivity(intent);
    }

    @Override
    public void launchActivity(Class clz) {
        launchActivity(new Intent(this, clz));
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
        // 显示Loading框
        if (!isFinishing())
            forceShowLoading(msg);
    }

    @Override
    public void forceShowLoading(String msg) {
        if (isFinishing()) return;
        if (progressDialog == null) {
            View view = LayoutInflater.from(this).inflate(R.layout.dialog_custom_loading, null);
            TextView tips = view.findViewById(R.id.tv_tip);
            tips.setText(msg);

            // progressDialog = new Dialog(this, R.style.CustomDialogWithDim);
            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomDialogWithDim);
            builder.setView(view).setCancelable(false);

            progressDialog = builder.show();
            progressDialog.setCanceledOnTouchOutside(false);
        } else {
            TextView tips = progressDialog.findViewById(R.id.tv_tip);
            if (tips != null)
                tips.setText(msg);
        }
        if (!progressDialog.isShowing()) {
            if (!isFinishing())
                progressDialog.show();
        }
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
