package cn.sxw.android.base.ui;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;

import com.umeng.analytics.MobclickAgent;
import com.zhy.autolayout.AutoConstraintLayout;
import com.zhy.autolayout.AutoFrameLayout;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import cn.sxw.android.base.di.component.AppComponent;
import cn.sxw.android.base.mvp.IPresenter;

public abstract class BaseActivity<P extends IPresenter> extends AppCompatActivity {
    protected final String TAG = this.getClass().getSimpleName();
    private static final String LAYOUT_LINEARLAYOUT = "LinearLayout";
    private static final String LAYOUT_FRAMELAYOUT = "FrameLayout";
    private static final String LAYOUT_RELATIVELAYOUT = "RelativeLayout";
    private static final String LAYOUT_CONSTRAINTLAYOUT = "android.support.constraint.ConstraintLayout";
    private static final String LAYOUT_CONSTRAINTLAYOUT_X = "androidx.constraintlayout.widget.ConstraintLayout";
    public static final String IS_NOT_ADD_ACTIVITY_LIST = "is_add_activity_list";//是否加入到activity的list，管理

    public BaseApplication mApplication;
    @Inject
    public P mPresenter;

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        View view = null;
        switch (name) {
            case LAYOUT_FRAMELAYOUT:
                view = new AutoFrameLayout(context, attrs);
                break;
            case LAYOUT_LINEARLAYOUT:
                view = new AutoLinearLayout(context, attrs);
                break;
            case LAYOUT_RELATIVELAYOUT:
                view = new AutoRelativeLayout(context, attrs);
                break;
            case LAYOUT_CONSTRAINTLAYOUT:
            case LAYOUT_CONSTRAINTLAYOUT_X:
                view = new AutoConstraintLayout(context, attrs);
                break;
        }
        if (view != null) return view;

        return super.onCreateView(name, context, attrs);
    }

    @Nullable
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApplication = (BaseApplication) getApplication();
        if (useEventBus())//如果要使用eventbus请将此方法返回true
            EventBus.getDefault().register(this);//注册到事件主线
        setupActivityComponent(mApplication.getAppComponent());//依赖注入
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) mPresenter.onDetach();//释放资源
        if (useEventBus())//如果要使用eventbus请将此方法返回true
            EventBus.getDefault().unregister(this);
        this.mPresenter = null;
        this.mApplication = null;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void FullScreencall() {
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    /**
     * 提供AppComponent(提供所有的单例对象)给子类，进行Component依赖
     *
     * @param appComponent
     */
    public abstract void setupActivityComponent(AppComponent appComponent);

    /**
     * 是否使用eventBus,默认为使用(true)，
     *
     * @return
     */
    public abstract boolean useEventBus();

    protected abstract void getDataFromNet();
}
