package cn.sxw.android.base.ui;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;

import com.umeng.commonsdk.UMConfigure;
import com.zhy.autolayout.config.AutoLayoutConfig;

import org.xutils.x;

import cn.sxw.android.base.di.component.AppComponent;
import cn.sxw.android.base.di.component.DaggerAppComponent;
import cn.sxw.android.base.di.module.AppModule;
import cn.sxw.android.base.di.module.ImageModule;
import cn.sxw.android.base.utils.CrashHandler;

public abstract class BaseApplication extends Application {
    private static BaseApplication mApplication;
    private AppComponent mAppComponent;

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
        AutoLayoutConfig.getInstance().useDeviceSize().useLandscape();

        x.Ext.init(this);
//        //路由初始化
//        Router.initialize(this);

        preInitUMSDK();

        //初始化CrashHandler
        CrashHandler.getInstance().init(this);

        /**
         * TODO 解决android7.0以上版本传递URI问题
         * @Modify by zzy@sxw.cn on 2018/2/8
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }
    }

    /**
     * 初始化友盟SDK
     */
    private void preInitUMSDK(){
        try {
            Bundle bundle = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA).metaData;
            String appKey = bundle.getString("UMENG_APPKEY");
            //默认用生学渠道
            String appChannel = bundle.getString("UMENG_CHANNEL","official");
            UMConfigure.preInit(this,appKey,appChannel);
        }catch (Exception e){
        }
    }

    /**
     * 用户同意隐私协议后真正的进行一次舒适化工作
     */
    protected void initUMSDKReally(){
        UMConfigure.init(this,UMConfigure.DEVICE_TYPE_PHONE,null);
    }

    /**
     * 程序终止的时候执行
     */
    @Override
    public void onTerminate() {
        super.onTerminate();
        this.mAppComponent = null;
        mApplication = null;
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
}
