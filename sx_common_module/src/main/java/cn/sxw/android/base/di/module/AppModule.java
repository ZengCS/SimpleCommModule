package cn.sxw.android.base.di.module;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import cn.sxw.android.base.di.scope.ApplicationContext;
import cn.sxw.android.base.di.scope.PreferenceInfo;
import cn.sxw.android.base.net.ApiHelper;
import cn.sxw.android.base.net.HttpRequestHelper;
import cn.sxw.android.base.prefer.AppPreferencesHelper;
import cn.sxw.android.base.prefer.PreferencesHelper;
import cn.sxw.android.base.utils.AppConstants;
import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    private Application mApplication;

    public AppModule(Application application) {
        this.mApplication = application;
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return mApplication;
    }

    @Singleton
    @Provides
    public Application provideApplication() {
        return mApplication;
    }

    @Provides
    @PreferenceInfo
    String providePreferenceName() {
        return AppConstants.PREF_NAME;}

    @Provides
    @Singleton
    PreferencesHelper providePreferencesHelper(AppPreferencesHelper appPreferencesHelper) {
        return appPreferencesHelper;
    }

    @Provides
    @Singleton
    ApiHelper provideHttpRequestHelper(HttpRequestHelper httpRequestHelper) {
        return httpRequestHelper;
    }
}
