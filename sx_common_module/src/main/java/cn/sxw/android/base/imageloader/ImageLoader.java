package cn.sxw.android.base.imageloader;

import android.content.Context;
import android.widget.ImageView;

import cn.sxw.android.base.imageloader.glideprogress.ProgressLoadListener;
import cn.sxw.android.base.imageloader.listener.ImageLoadListener;
import cn.sxw.android.base.imageloader.listener.ImageSaveListener;
import cn.sxw.android.base.imageloader.listener.SourceReadyListener;

/**
 * Created by Alex.Tang on 2017-03-13.
 */

public interface ImageLoader {
    /**
     * Init ImageLoader
     */
    void init(Context context);

    /**
     * @param url
     * @param imageView
     */
    void displayImageWithAppCxt(String url, ImageView imageView);

    /**
     * @param url
     * @param defaultImage
     * @param imageView
     */
    void displayImage(String url, int defaultImage, ImageView imageView);

    /**
     * @param context
     * @param imageUrl
     * @param imageView
     * @param defaultImage
     */
    void displayImage(Context context, String imageUrl, ImageView imageView, int defaultImage);

    /**
     * 加载图片（加载失败监听）
     *
     * @param context
     * @param imageUrl
     * @param imageView
     * @param loadImage
     * @param errorImage
     * @param listener
     */
    void displayImage(Context context, String imageUrl, ImageView imageView, int loadImage,
                      int errorImage, ImageLoadListener listener);

    /**
     * 展示圆角图片
     *
     * @param url
     * @param defaultImage
     * @param imageView
     */
    void displayCircleImage(String url, int defaultImage, ImageView imageView);

    /**
     * 展示有边角的圆图
     *
     * @param url
     * @param defaultImage
     * @param imageView
     * @param borderWidth
     * @param borderColor
     */

    void displayCircleBorderImage(String url, int defaultImage, ImageView imageView,
                                  int borderWidth, int borderColor);

    /**
     * Gif图片
     *
     * @param url
     * @param defaultImage
     * @param imageView
     */

    void displayGifImage(String url, int defaultImage, ImageView imageView);

    /**
     * 带进度
     *
     * @param url
     * @param imageView
     * @param listener
     */
    void displayImageWithProgress(String url, ImageView imageView, ProgressLoadListener listener);

    /**
     * @param url
     * @param imageView
     * @param defaultImage
     * @param listener
     */
    void displayImageWithPrepareCall(String url, ImageView imageView, int defaultImage,
                                     SourceReadyListener listener);

    /**
     * @param url
     * @param imageView
     * @param listener
     */
    void displayGifWithPrepareCall(String url, ImageView imageView, SourceReadyListener listener);

    //清除硬盘缓存
    void clearImageDiskCache(final Context context);

    //清除内存缓存
    void clearImageMemoryCache(Context context);

    //根据不同的内存状态，来响应不同的内存释放策略
    void trimMemory(Context context, int level);

    //获取缓存大小
    String getCacheSize(Context context);

    void saveImage(Context context, String url, String savePath, String saveFileName,
                   ImageSaveListener listener);

}

