package cn.sxw.android.base.di.component;

import android.app.Application;
import android.content.Context;

import javax.inject.Provider;

import cn.sxw.android.base.di.module.AppModule;
import cn.sxw.android.base.di.module.AppModule_ProvideApplicationFactory;
import cn.sxw.android.base.di.module.AppModule_ProvideContextFactory;
import cn.sxw.android.base.di.module.AppModule_ProvideHttpRequestHelperFactory;
import cn.sxw.android.base.di.module.AppModule_ProvidePreferenceNameFactory;
import cn.sxw.android.base.di.module.AppModule_ProvidePreferencesHelperFactory;
import cn.sxw.android.base.di.module.ImageModule;
import cn.sxw.android.base.di.module.ImageModule_ProvideImageLoaderFactory;
import cn.sxw.android.base.imageloader.GlideImageLoader;
import cn.sxw.android.base.imageloader.GlideImageLoader_Factory;
import cn.sxw.android.base.imageloader.ImageLoader;
import cn.sxw.android.base.integration.ActivityLifecycle;
import cn.sxw.android.base.integration.ActivityLifecycle_Factory;
import cn.sxw.android.base.integration.AppManager;
import cn.sxw.android.base.integration.AppManager_Factory;
import cn.sxw.android.base.net.ApiHelper;
import cn.sxw.android.base.net.HttpRequestHelper;
import cn.sxw.android.base.net.HttpRequestHelper_Factory;
import cn.sxw.android.base.prefer.AppPreferencesHelper;
import cn.sxw.android.base.prefer.AppPreferencesHelper_Factory;
import cn.sxw.android.base.prefer.PreferencesHelper;
import cn.sxw.android.base.ui.BaseApplication;
import cn.sxw.android.base.ui.BaseApplication_MembersInjector;
import dagger.MembersInjector;
import dagger.internal.DoubleCheck;
import dagger.internal.Preconditions;

public final class DaggerAppComponent implements AppComponent {
  private Provider<Application> provideApplicationProvider;

  private Provider<GlideImageLoader> glideImageLoaderProvider;

  private Provider<ImageLoader> provideImageLoaderProvider;

  private Provider<AppManager> appManagerProvider;

  private Provider<Context> provideContextProvider;

  private Provider<String> providePreferenceNameProvider;

  private Provider<AppPreferencesHelper> appPreferencesHelperProvider;

  private Provider<PreferencesHelper> providePreferencesHelperProvider;

  private Provider<HttpRequestHelper> httpRequestHelperProvider;

  private Provider<ApiHelper> provideHttpRequestHelperProvider;

  private Provider<ActivityLifecycle> activityLifecycleProvider;

  private MembersInjector<BaseApplication> baseApplicationMembersInjector;

  private DaggerAppComponent(Builder builder) {
    assert builder != null;
    initialize(builder);
  }

  public static Builder builder() {
    return new Builder();
  }

  @SuppressWarnings("unchecked")
  private void initialize(final Builder builder) {

    this.provideApplicationProvider =
        DoubleCheck.provider(AppModule_ProvideApplicationFactory.create(builder.appModule));

    this.glideImageLoaderProvider = DoubleCheck.provider(GlideImageLoader_Factory.create());

    this.provideImageLoaderProvider =
        DoubleCheck.provider(
            ImageModule_ProvideImageLoaderFactory.create(
                builder.imageModule, glideImageLoaderProvider));

    this.appManagerProvider =
        DoubleCheck.provider(AppManager_Factory.create(provideApplicationProvider));

    this.provideContextProvider = AppModule_ProvideContextFactory.create(builder.appModule);

    this.providePreferenceNameProvider =
        AppModule_ProvidePreferenceNameFactory.create(builder.appModule);

    this.appPreferencesHelperProvider =
        DoubleCheck.provider(
            AppPreferencesHelper_Factory.create(
                provideContextProvider, providePreferenceNameProvider));

    this.providePreferencesHelperProvider =
        DoubleCheck.provider(
            AppModule_ProvidePreferencesHelperFactory.create(
                builder.appModule, appPreferencesHelperProvider));

    this.httpRequestHelperProvider = DoubleCheck.provider(HttpRequestHelper_Factory.create());

    this.provideHttpRequestHelperProvider =
        DoubleCheck.provider(
            AppModule_ProvideHttpRequestHelperFactory.create(
                builder.appModule, httpRequestHelperProvider));

    this.activityLifecycleProvider =
        DoubleCheck.provider(ActivityLifecycle_Factory.create(appManagerProvider));

    this.baseApplicationMembersInjector =
        BaseApplication_MembersInjector.create(activityLifecycleProvider);
  }

  @Override
  public Application Application() {
    return provideApplicationProvider.get();
  }

  @Override
  public ImageLoader imageLoader() {
    return provideImageLoaderProvider.get();
  }

  @Override
  public AppManager appManager() {
    return appManagerProvider.get();
  }

  @Override
  public PreferencesHelper preferencesHelper() {
    return providePreferencesHelperProvider.get();
  }

  @Override
  public ApiHelper httpRequestHelper() {
    return provideHttpRequestHelperProvider.get();
  }

  @Override
  public void inject(BaseApplication application) {
    baseApplicationMembersInjector.injectMembers(application);
  }

  public static final class Builder {
    private AppModule appModule;

    private ImageModule imageModule;

    private Builder() {}

    public AppComponent build() {
      if (appModule == null) {
        throw new IllegalStateException(AppModule.class.getCanonicalName() + " must be set");
      }
      if (imageModule == null) {
        this.imageModule = new ImageModule();
      }
      return new DaggerAppComponent(this);
    }

    public Builder appModule(AppModule appModule) {
      this.appModule = Preconditions.checkNotNull(appModule);
      return this;
    }

    public Builder imageModule(ImageModule imageModule) {
      this.imageModule = Preconditions.checkNotNull(imageModule);
      return this;
    }
  }
}
