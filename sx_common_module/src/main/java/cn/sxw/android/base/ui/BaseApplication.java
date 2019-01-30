package cn.sxw.android.base.ui;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.StrictMode;

import com.umeng.analytics.MobclickAgent;
import com.zhy.autolayout.config.AutoLayoutConfig;

import org.xutils.x;

import javax.inject.Inject;

import cn.sxw.android.base.di.component.AppComponent;
import cn.sxw.android.base.di.component.DaggerAppComponent;
import cn.sxw.android.base.di.module.AppModule;
import cn.sxw.android.base.di.module.ImageModule;
import cn.sxw.android.base.integration.ActivityLifecycle;

public abstract class BaseApplication extends Application {
    private static BaseApplication mApplication;
    private AppComponent mAppComponent;

    @Inject
    protected ActivityLifecycle mActivityLifecycle;

    protected final String TAG = this.getClass().getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
//        if (BuildConfig.DEBUG) {//Timber日志打印
//            Timber.plant(new Timber.DebugTree());
//        }
        mApplication = this;
        mAppComponent = DaggerAppComponent
                .builder()
                .appModule(new AppModule(this))//提供application
                .imageModule(new ImageModule())//图片加载框架默认使用glide
                .build();
        mAppComponent.inject(this);
        registerActivityLifecycleCallbacks(mActivityLifecycle);
        //网络初始化
        // initOkGo();
        AutoLayoutConfig.getInstance().useDeviceSize().useLandscape();

        x.Ext.init(this);
//        //路由初始化
//        Router.initialize(this);
        //初始化友盟
        MobclickAgent.setScenarioType(mApplication, MobclickAgent.EScenarioType.E_UM_NORMAL);
        //初始化crash
        // CrashHandler.getInstance().init(this);
        //初始化sophix热修复
        // initHotFix();

        /**
         * TODO 解决android7.0以上版本传递URI问题
         * @Modify by zzy@sxw.cn on 2018/2/8
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }
    }

//    private void initOkGo() {
//        OkHttpClient.Builder builder = new OkHttpClient.Builder();
//        //全局的读取超时时间
//        builder.readTimeout(15 * 1000, TimeUnit.MILLISECONDS);
//        //全局的写入超时时间
//        builder.writeTimeout(15 * 1000, TimeUnit.MILLISECONDS);
//        //全局的连接超时时间
//        builder.connectTimeout(15 * 1000, TimeUnit.MILLISECONDS);
//
//        OkGo.getInstance().init(this)                       //必须调用初始化
//                .setOkHttpClient(builder.build())               //建议设置OkHttpClient，不设置将使用默认的
//                .setCacheMode(CacheMode.NO_CACHE)               //全局统一缓存模式，默认不使用缓存，可以不传
//                .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)   //全局统一缓存时间，默认永不过期，可以不传
//                .setRetryCount(3);                         //全局统一超时重连次数，默认为三次，那么最差的情况会请求4次(一次原始请求，三次重连请求)，不需要可以设置为0
//    }

//    private void initHotFix() {
//        Log.d("tjy", "--------version=" + PackageInfoUtil.getVersionName(this));
//        SophixManager.getInstance().setContext(this)
//                .setAppVersion(PackageInfoUtil.getVersionName(this))
//                .setAesKey(null)
//                .setEnableDebug(true)
//                .setPatchLoadStatusStub(new PatchLoadStatusListener() {
//                    @Override
//                    public void onLoad(final int mode, final int code, final String info, final int handlePatchVersion) {
//                        // 补丁加载回调通知
//                        if (code == PatchStatus.CODE_LOAD_SUCCESS) {
//                            // 表明补丁加载成功
//                        } else if (code == PatchStatus.CODE_LOAD_RELAUNCH) {
//                            // 表明新补丁生效需要重启. 开发者可提示用户或者强制重启;
//                            // 建议: 用户可以监听进入后台事件, 然后应用自杀
//                        } else if (code == PatchStatus.CODE_LOAD_FAIL) {
//                            // 内部引擎异常, 推荐此时清空本地补丁, 防止失败补丁重复加载
//                            SophixManager.getInstance().cleanPatches();
//                        } else {
//                            // 其它错误信息, 查看PatchStatus类说明
//                        }
//                    }
//                }).initialize();
//    }

    /**
     * 程序终止的时候执行
     */
    @Override
    public void onTerminate() {
        super.onTerminate();
        if (mActivityLifecycle != null) {
            unregisterActivityLifecycleCallbacks(mActivityLifecycle);
        }
        this.mAppComponent = null;
        this.mActivityLifecycle = null;
        this.mApplication = null;
    }

    /**
     * 将AppComponent返回出去,供其它地方使用, AppComponent接口中声明的方法返回的实例,
     * 在getAppComponent()拿到对象后都可以直接使用
     *
     * @return
     */
    public AppComponent getAppComponent() {
        return mAppComponent;
    }

    /**
     * 返回上下文
     *
     * @return
     */
    public static Context getContext() {
        return mApplication;
    }

    /**
     * 重启APP
     */
    public static void relaunchApp() {
        PackageManager packageManager = mApplication.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(mApplication.getPackageName());
        if (intent == null) return;
        ComponentName componentName = intent.getComponent();
        Intent mainIntent = Intent.makeRestartActivityTask(componentName);
        mApplication.startActivity(mainIntent);
        System.exit(0);
    }
}
