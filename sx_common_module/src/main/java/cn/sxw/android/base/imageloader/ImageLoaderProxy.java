package cn.sxw.android.base.imageloader;

import android.content.Context;
import android.widget.ImageView;

import javax.inject.Inject;
import javax.inject.Singleton;

import cn.sxw.android.base.imageloader.glideprogress.ProgressLoadListener;
import cn.sxw.android.base.imageloader.listener.ImageLoadListener;
import cn.sxw.android.base.imageloader.listener.ImageSaveListener;
import cn.sxw.android.base.imageloader.listener.SourceReadyListener;

/**
 * Created by Alex.Tang on 2017-03-13.
 */

@Singleton
public final class ImageLoaderProxy implements ImageLoader {

    private ImageLoader imageLoader;//代理对象

    private Context context;

    @Inject
    public ImageLoaderProxy(ImageLoader imageLoader) {
        setImageLoaderProxy(imageLoader);
    }


    @Override
    public void init(Context context) {
        imageLoader.init(context);
    }

    @Override
    public void displayImageWithAppCxt(String url, ImageView imageView) {
        imageLoader.displayImageWithAppCxt(url, imageView);
    }

    @Override
    public void displayImage(String url, int defaultImage, ImageView imageView) {
        imageLoader.displayImage(url, defaultImage, imageView);
    }

    @Override
    public void displayImage(Context context, String imageUrl, ImageView imageView,
                             int defaultImage) {
        imageLoader.displayImage(context, imageUrl, imageView, defaultImage);
    }

    @Override
    public void displayImage(Context context, String imageUrl, ImageView imageView, int loadImage,
                             int errorImage, ImageLoadListener listener) {
        imageLoader.displayImage(context, imageUrl, imageView, loadImage, errorImage, listener);
    }

    @Override
    public void displayCircleImage(String url, int defaultImage, ImageView imageView) {
        imageLoader.displayCircleImage(url, defaultImage, imageView);
    }

    @Override
    public void displayCircleBorderImage(String url, int defaultImage, ImageView imageView,
                                         int borderWidth, int borderColor) {
        imageLoader.displayCircleBorderImage(url, defaultImage, imageView, borderWidth, borderColor);
    }

    @Override
    public void displayGifImage(String url, int defaultImage, ImageView imageView) {
        imageLoader.displayGifImage(url, defaultImage, imageView);
    }

    @Override
    public void displayImageWithProgress(String url, ImageView imageView,
                                         ProgressLoadListener listener) {
        imageLoader.displayImageWithProgress(url, imageView, listener);

    }

    @Override
    public void displayImageWithPrepareCall(String url, ImageView imageView,
                                            int defaultImage, SourceReadyListener listener) {
        imageLoader.displayImageWithPrepareCall(url, imageView, defaultImage, listener);
    }

    @Override
    public void displayGifWithPrepareCall(String url, ImageView imageView,
                                          SourceReadyListener listener) {
        imageLoader.displayGifWithPrepareCall(url, imageView, listener);
    }

    @Override
    public void clearImageDiskCache(Context context) {
        imageLoader.clearImageDiskCache(context);
    }

    @Override
    public void clearImageMemoryCache(Context context) {
        imageLoader.clearImageMemoryCache(context);
    }

    @Override
    public void trimMemory(Context context, int level) {
        imageLoader.trimMemory(context, level);
    }

    @Override
    public String getCacheSize(Context context) {
        return imageLoader.getCacheSize(context);
    }

    @Override
    public void saveImage(Context context, String url, String savePath, String saveFileName,
                          ImageSaveListener listener) {
        imageLoader.saveImage(context, url, savePath, saveFileName, listener);
    }

    public void setImageLoaderProxy(ImageLoader imageLoader) {
        this.imageLoader = imageLoader;
    }
}
