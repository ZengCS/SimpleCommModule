package cn.sxw.android.base.di.component;

import android.app.Application;

import javax.inject.Singleton;

import cn.sxw.android.base.di.module.AppModule;
import cn.sxw.android.base.di.module.ImageModule;
import cn.sxw.android.base.imageloader.ImageLoader;
import cn.sxw.android.base.integration.AppManager;
import cn.sxw.android.base.net.ApiHelper;
import cn.sxw.android.base.prefer.PreferencesHelper;
import cn.sxw.android.base.ui.BaseApplication;
import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, ImageModule.class})
public interface AppComponent {

    Application Application();

    //图片管理器,用于加载图片的管理类,默认使用glide
    ImageLoader imageLoader();

    //用于管理所有activity
    AppManager appManager();

    PreferencesHelper preferencesHelper();

    ApiHelper httpRequestHelper();

    void inject(BaseApplication application);
}
