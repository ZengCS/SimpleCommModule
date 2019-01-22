package cn.sxw.android.base.di.module;

import javax.inject.Singleton;

import cn.sxw.android.base.imageloader.GlideImageLoader;
import cn.sxw.android.base.imageloader.ImageLoader;
import dagger.Module;
import dagger.Provides;

@Module
public class ImageModule {

    @Singleton
    @Provides
    public ImageLoader provideImageLoader(GlideImageLoader glideImageLoader) {
        return glideImageLoader;
    }

}
