package cn.sxw.android.base.imageloader;

import android.content.Context;
import android.os.Looper;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import javax.inject.Inject;
import javax.inject.Singleton;

import cn.sxw.android.base.imageloader.glideprogress.ProgressLoadListener;
import cn.sxw.android.base.imageloader.glideprogress.ProgressModelLoader;
import cn.sxw.android.base.imageloader.glideprogress.ProgressUIListener;
import cn.sxw.android.base.imageloader.listener.ImageLoadListener;
import cn.sxw.android.base.imageloader.listener.ImageSaveListener;
import cn.sxw.android.base.imageloader.listener.SourceReadyListener;
import cn.sxw.android.base.imageloader.transformation.GlideCircleTransform;
import cn.sxw.android.base.imageloader.utils.ImageLoaderUtils;
import cn.sxw.android.base.utils.LogUtil;

/**
 * Created by Alex.Tang on 2017-03-22.
 */
@Singleton
public class GlideImageLoader implements ImageLoader {
    private static final String TAG = "GlideImageLoader";

    @Inject
    public GlideImageLoader() {

    }

    @Override
    public void init(Context context) {
        //
    }

    @Override
    public void displayImageWithAppCxt(String url, ImageView imageView) {
        LogUtil.w(TAG, "displayImageWithAppCxt() called with: url = [" + url + "]");
        Glide.with(imageView.getContext().getApplicationContext()).load(url)
                .placeholder(imageView.getDrawable())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView);
    }

    @Override
    public void displayImage(String url, int defaultImage, ImageView imageView) {
        loadNormal(imageView.getContext(), url, defaultImage, imageView);
    }

    @Override
    public void displayImage(Context context, String imageUrl, ImageView imageView, int defaultImage) {
        loadNormal(context, imageUrl, defaultImage, imageView);
    }

    @Override
    public void displayImage(Context context, String imageUrl, ImageView imageView,
                             int loadImage, int errorImage, final ImageLoadListener listener) {
        LogUtil.w(TAG, "displayImage() called with: imageUrl = [" + imageUrl + "]");
        Glide.with(context).load(imageUrl)
                .placeholder(loadImage)
                .error(errorImage)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String s,
                                               Target<GlideDrawable> target, boolean b) {
                        if (listener != null) {
                            if (e != null) {
                                listener.loadImageFail(e.getMessage());
                            } else {
                                listener.loadImageFail("图片加载失败，" + s);
                            }
                        }
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable glideDrawable, String s,
                                                   Target<GlideDrawable> target, boolean b,
                                                   boolean b1) {
                        if (listener != null) {
                            listener.loadImageSuccess();
                        }
                        return false;
                    }
                })
                .into(imageView);
    }

    @Override
    public void displayCircleImage(String url, int defaultImage, ImageView imageView) {
        LogUtil.w(TAG, "displayCircleImage() called with: url = [" + url + "]");
        Glide.with(imageView.getContext()).load(url).placeholder(defaultImage)
                .transform(new GlideCircleTransform(imageView.getContext()))
                .diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageView);
    }

    @Override
    public void displayCircleBorderImage(String url, int defaultImage, ImageView imageView, int borderWidth, int borderColor) {
        LogUtil.w(TAG, "displayCircleBorderImage() called with: url = [" + url + "]");
        Glide.with(imageView.getContext()).load(url).placeholder(defaultImage)
                .transform(new GlideCircleTransform(imageView.getContext(), borderWidth, borderColor))
                .diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageView);
    }

    @Override
    public void displayGifImage(String url, int defaultImage, ImageView imageView) {
        loadGif(imageView.getContext(), url, defaultImage, imageView);
    }

    @Override
    public void displayImageWithProgress(String url, final ImageView imageView, final ProgressLoadListener listener) {
        LogUtil.w(TAG, "displayImageWithProgress() called with: url = [" + url + "]");
        Glide.with(imageView.getContext()).using(new ProgressModelLoader(new ProgressUIListener() {
            @Override
            public void update(final int bytesRead, final int contentLength) {
                imageView.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.update(bytesRead, contentLength);
                    }
                });
            }
        })).load(url).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.SOURCE).
                listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        listener.onException();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        listener.onResourceReady();
                        return false;
                    }
                }).into(imageView);
    }

    @Override
    public void displayImageWithPrepareCall(String url, ImageView imageView, int defaultImage, final SourceReadyListener listener) {
        LogUtil.w(TAG, "displayImageWithPrepareCall() called with: url = [" + url + "]");
        Glide.with(imageView.getContext()).load(url)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(defaultImage)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        listener.onResourceReady(resource.getIntrinsicWidth(), resource.getIntrinsicHeight());
                        return false;
                    }
                }).into(imageView);
    }

    @Override
    public void displayGifWithPrepareCall(String url, ImageView imageView, final SourceReadyListener listener) {
        LogUtil.w(TAG, "displayGifWithPrepareCall() called with: url = [" + url + "]");
        Glide.with(imageView.getContext()).load(url).asGif()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE).
                listener(new RequestListener<String, GifDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GifDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GifDrawable resource, String model, Target<GifDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        listener.onResourceReady(resource.getIntrinsicWidth(), resource.getIntrinsicHeight());
                        return false;
                    }
                }).into(imageView);
    }

    @Override
    public void clearImageDiskCache(final Context context) {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.get(context.getApplicationContext()).clearDiskCache();
                    }
                }).start();
            } else {
                Glide.get(context.getApplicationContext()).clearDiskCache();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void clearImageMemoryCache(Context context) {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) { //只能在主线程执行
                Glide.get(context.getApplicationContext()).clearMemory();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void trimMemory(Context context, int level) {
        Glide.get(context).trimMemory(level);
    }

    @Override
    public String getCacheSize(Context context) {
        try {
            return ImageLoaderUtils.getFormatSize(ImageLoaderUtils.getFolderSize(Glide.getPhotoCacheDir(context.getApplicationContext())));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void saveImage(Context context, String url, String savePath, String saveFileName, ImageSaveListener listener) {

    }


    private void loadNormal(final Context ctx, final String url, int placeholder, ImageView imageView) {
        LogUtil.w(TAG, "loadNormal() called with: url = [" + url + "]");
        Glide.with(ctx).load(url).dontAnimate().placeholder(placeholder).diskCacheStrategy(DiskCacheStrategy.SOURCE).listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                return false;
            }
        }).into(imageView);
    }

    /**
     * load image with Glide
     */
    private void loadGif(final Context ctx, String url, int placeholder, ImageView imageView) {
        LogUtil.w(TAG, "loadGif() called with: url = [" + url + "]");
        Glide.with(ctx).load(url).asGif()
                .placeholder(placeholder).skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE).listener(new RequestListener<String, GifDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GifDrawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(GifDrawable resource, String model, Target<GifDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                return false;
            }
        })
                .into(imageView);
    }


}
