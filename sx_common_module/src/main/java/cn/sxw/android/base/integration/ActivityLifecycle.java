package cn.sxw.android.base.integration;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import javax.inject.Inject;
import javax.inject.Singleton;

import static cn.sxw.android.base.ui.BaseActivity.IS_NOT_ADD_ACTIVITY_LIST;

@Singleton
public class ActivityLifecycle implements Application.ActivityLifecycleCallbacks {
    private boolean isRunOnBackground = true;
    private AppManager mAppManager;
    // 增加对运行在前后台的监控
    private AppRunStateListener appRunStateListener;

    @Inject
    public ActivityLifecycle(AppManager appManager) {
        this.mAppManager = appManager;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        // 如果intent包含了此字段,并且为true说明不加入到list
        // 默认为false,如果不需要管理(比如不需要在退出所有activity(killAll)时，退出此activity就在intent加此字段为true)
        boolean isNotAdd = false;
        if (activity.getIntent() != null)
            isNotAdd = activity.getIntent().getBooleanExtra(IS_NOT_ADD_ACTIVITY_LIST, false);

        if (!isNotAdd) {
            mAppManager.addActivity(activity);
        }

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        // 应用返回前台运行
        if (appRunStateListener != null && isRunOnBackground) {
            appRunStateListener.runOnForeground();
        }
        isRunOnBackground = false;
        mAppManager.setCurrentActivity(activity);
    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {
        // 表示按home按键
        if (mAppManager.getCurrentActivity() == activity) {
            isRunOnBackground = true;
            if (appRunStateListener != null) {
                appRunStateListener.runOnBackground();
            }
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        mAppManager.removeActivity(activity);
    }

    public void addAppRunStateListener(AppRunStateListener listener) {
        this.appRunStateListener = listener;
    }

    public interface AppRunStateListener {
        void runOnBackground();

        void runOnForeground();
    }

}
