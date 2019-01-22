package cn.sxw.android.base.imageloader;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.nostra13.universalimageloader.cache.disc.impl.LimitedAgeDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import cn.sxw.android.base.imageloader.glideprogress.ProgressLoadListener;
import cn.sxw.android.base.imageloader.listener.ImageLoadListener;
import cn.sxw.android.base.imageloader.listener.ImageSaveListener;
import cn.sxw.android.base.imageloader.listener.SourceReadyListener;
import cn.sxw.android.base.imageloader.utils.ImageLoaderUtils;

/**
 * Created by Alex.Tang on 2017-03-13.
 */

public class UniversalImageLoader implements ImageLoader {
    private final long discCacheLimitTime = 3600 * 24 * 15L;

    com.nostra13.universalimageloader.core.ImageLoader imageLoader =
            com.nostra13.universalimageloader.core.ImageLoader.getInstance();

    @Override
    public void init(Context context) {
        if (!imageLoader.isInited()) {
            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                    context)
                    .threadPriority(Thread.NORM_PRIORITY)
                    .denyCacheImageMultipleSizesInMemory()
                    .memoryCache(new WeakMemoryCache())
                    .memoryCacheSize((2 * 1024 * 1024))
                    .memoryCacheSizePercentage(13)
                    .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                    .diskCache(
                            new LimitedAgeDiskCache(StorageUtils
                                    .getCacheDirectory(context),
                                    discCacheLimitTime))
                    .tasksProcessingOrder(QueueProcessingType.LIFO).build();
            com.nostra13.universalimageloader.core.ImageLoader.getInstance().init(config);
        }
    }

    @Override
    public void displayImageWithAppCxt(String url, ImageView imageView) {

    }

    @Override
    public void displayImage(String url, int defaultImage, ImageView imageView) {

    }


    @Override
    public void displayImage(Context context,String uri, ImageView img, int default_pic) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(default_pic)
                .showImageForEmptyUri(default_pic).showImageOnFail(default_pic)
                .cacheInMemory(true).cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new SimpleBitmapDisplayer()).build();
        imageLoader.displayImage(uri, img, options);
    }

    @Override
    public void displayImage(Context context, String imageUrl, ImageView imageView,
                             int loadImage, int errorImage, ImageLoadListener listener) {

    }

    @Override
    public void displayCircleImage(String url, int defaultImage, ImageView imageView) {

    }

    @Override
    public void displayCircleBorderImage(String url, int defaultImage, ImageView imageView,
                                         int borderWidth, int borderColor) {

    }

    @Override
    public void displayGifImage(String url, int defaultImage, ImageView imageView) {

    }

    @Override
    public void displayImageWithProgress(String url, ImageView imageView,
                                         ProgressLoadListener listener) {

    }

    @Override
    public void displayImageWithPrepareCall(String url, ImageView imageView, int defaultImage,
                                            SourceReadyListener listener) {

    }

    @Override
    public void displayGifWithPrepareCall(String url, ImageView imageView, SourceReadyListener listener) {

    }

    @Override
    public void clearImageDiskCache(Context context) {

    }

    @Override
    public void clearImageMemoryCache(Context context) {

    }

    @Override
    public void trimMemory(Context context, int level) {

    }

    @Override
    public String getCacheSize(Context context) {
        return null;
    }

    @Override
    public void saveImage(Context context, String url, String savePath, String saveFileName,
                          ImageSaveListener listener) {
        if (!ImageLoaderUtils.isSDCardExsit() || TextUtils.isEmpty(url)) {
            listener.onSaveFail();
            return;
        }
        InputStream fromStream = null;
        OutputStream toStream = null;
        try {
            File cacheFile = Glide.with(context).load(url)
                    .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();
            if (cacheFile == null || !cacheFile.exists()) {
                listener.onSaveFail();
                return;
            }
            File dir = new File(savePath);
            if (!dir.exists()) {
                dir.mkdir();
            }
            File file = new File(dir, saveFileName
                    + ImageLoaderUtils.getPicType(cacheFile.getAbsolutePath()));

            fromStream = new FileInputStream(cacheFile);
            toStream = new FileOutputStream(file);
            byte length[] = new byte[1024];
            int count;
            while ((count = fromStream.read(length)) > 0) {
                toStream.write(length, 0, count);
            }
            //用广播通知相册进行更新相册
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri uri = Uri.fromFile(file);
            intent.setData(uri);
            context.sendBroadcast(intent);
            listener.onSaveSuccess();
        } catch (Exception e) {
            e.printStackTrace();
            listener.onSaveFail();
        } finally {
            if (fromStream != null) {
                try {
                    fromStream.close();
                    toStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    fromStream = null;
                    toStream = null;
                }
            }
        }
    }
}
