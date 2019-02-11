package com.zcs.android.lib.sketch.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.WindowManager;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 图片工具类
 *
 * @author blue
 */
public class BitmapUtil {
    /**
     * Constants
     */
    private static final String TAG = "BitmapUtil";
    private static final String CONTENT = "content";
    private static final String FILE = "file";

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // 源图片的高度和宽度
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            // 计算出实际宽高和目标宽高的比率
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
            // 一定都会大于等于目标的宽和高。
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    /**
     * 压缩图片
     *
     * @param filePath
     * @return
     */
    public static Bitmap compressFile(Context context, String filePath) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, opts);
        float scale_h = (float) opts.outHeight / (float) ScreenUtils.getScreenHeight(context);
        float scale_w = (float) opts.outWidth / (float) ScreenUtils.getScreenWidth(context);
        float scale = scale_h > scale_w ? scale_w : scale_h;
        if (scale < 1) {
            scale = 1f;
        } else if (scale < 2) {
            scale = 2f;
        } else if (scale < 4) {
            scale = 4f;
        } else if (scale < 8) {
            scale = 8f;
        } else {
            scale = 16f;
        }
        opts.inSampleSize = (int) scale;
        opts.inJustDecodeBounds = false;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, opts);
        return bmp;
    }

    /**
     * 压缩图片
     *
     * @param filePath
     * @return
     */
    public static Bitmap compressFile(Context context, String filePath, float width, float height) {
        Log.d(TAG, "compressFile() called with: " + "context = [" + context + "], filePath = [" + filePath + "], width = [" + width + "], height = [" + height + "]");
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, opts);
        float scale_h = (float) opts.outHeight / height;
        float scale_w = (float) opts.outWidth / width;
        Log.d(TAG, "compressFile: scale_h = " + scale_h);
        Log.d(TAG, "compressFile: scale_w = " + scale_w);
        float scale = scale_h > scale_w ? scale_w : scale_h;
        if (scale < 1) {
            scale = 1f;
        } else if (scale < 2) {
            scale = 2f;
        } else if (scale < 4) {
            scale = 4f;
        } else if (scale < 8) {
            scale = 8f;
        } else {
            scale = 16f;
        }
        Log.d(TAG, "compressFile: scale = " + scale);
        opts.inSampleSize = (int) scale;
        opts.inJustDecodeBounds = false;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, opts);
        return bmp;
    }

    /**
     * 获取文件的路径
     */
    public static String getFilePath(Context context, Uri uri) {
        String filePath = null;
        if (CONTENT.equalsIgnoreCase(uri.getScheme())) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.Media.DATA}, null, null, null);
            if (null == cursor) {
                return null;
            }
            try {
                if (cursor.moveToNext()) {
                    filePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                }
            } finally {
                cursor.close();
            }
        }

        // 从文件中选择
        if (FILE.equalsIgnoreCase(uri.getScheme())) {
            filePath = uri.getPath();
        }
        return filePath;
    }

    /**
     * 根据指定比例缩放图片
     *
     * @param fileDescriptor
     * @param reqWidth
     * @return
     * @author blue
     */
    public static Bitmap decodeSampledBitmap(FileDescriptor fileDescriptor, int reqWidth, int reqHeight) {
        Log.d(TAG, "decodeSampledBitmap() called with: " + "fileDescriptor = [" + fileDescriptor + "], reqWidth = [" + reqWidth + "], reqHeight = [" + reqHeight + "]");
        // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);
        // 调用上面定义的方法计算inSampleSize值
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // 使用获取到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);
    }

    public static Bitmap decodeSampledBitmap(InputStream inputStream, int reqWidth, int reqHeight) throws FileNotFoundException {
        // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(inputStream, null, options);
        // 调用上面定义的方法计算inSampleSize值
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // 使用获取到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeStream(inputStream, null, options);
    }

    public static Bitmap decodeSampledBitmap(Resources resources, int resId, int reqWidth, int reqHeight) throws FileNotFoundException {
        // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(resources, resId, options);
        // 调用上面定义的方法计算inSampleSize值
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // 使用获取到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(resources, resId, options);
    }

    /**
     * 保存Bitmap到SDCard
     *
     * @param bitmap
     * @param filePath 图片保存路径
     */
    public static boolean saveBitmap(Bitmap bitmap, String filePath) {
        boolean success;
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
        File dir = file.getParentFile();
        if (!dir.exists()) {
            dir.mkdirs();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
            out.flush();
            out.close();
            success = true;
        } catch (IOException e) {
            e.printStackTrace();
            success = false;
        }
        return success;
    }

    /**
     * 保存Bitmap到SDCard
     *
     * @param bitmap
     * @param filePath 图片保存路径
     */
    public static boolean saveBitmapPng(Bitmap bitmap, String filePath) {
        boolean success;
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
        File dir = file.getParentFile();
        if (!dir.exists()) {
            dir.mkdirs();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            success = true;
        } catch (IOException e) {
            e.printStackTrace();
            success = false;
        }
        return success;
    }


    /**
     * 保存Bitmap到SDCard
     *
     * @param bitmap
     */
    public static boolean saveBitmap(Bitmap bitmap, String filePath, double width, double height) {
        if (bitmap == null)
            return false;
        boolean success;
        bitmap = zoomImage(bitmap, width, height);
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
        File dir = file.getParentFile();
        if (!dir.exists()) {
            dir.mkdirs();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
            out.flush();
            out.close();
            success = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            success = false;
        } catch (IOException e) {
            e.printStackTrace();
            success = false;
        }
        return success;
    }


    /**
     * 将图片缩放到指定尺寸
     *
     * @param sourceBitmap 图片源
     * @param newWidth     新的宽度
     * @param newHeight    新的高度
     * @return
     */
    public static Bitmap zoomImage(Bitmap sourceBitmap, double newWidth, double newHeight) {
        // 获取这个图片的宽和高
        float width = sourceBitmap.getWidth();
        float height = sourceBitmap.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        float targetScale = Math.max(scaleWidth, scaleHeight);
        // 缩放图片动作
        matrix.postScale(targetScale, targetScale);
        Bitmap bitmap = Bitmap.createBitmap(sourceBitmap, 0, 0, (int) width, (int) height, matrix, true);
        return bitmap;
    }

    public static boolean isLandScreen(Context context) {
        int ori = context.getResources().getConfiguration().orientation;//获取屏幕方向
        return ori == Configuration.ORIENTATION_LANDSCAPE;
    }

    public static Bitmap decodeSampleBitMapFromFile(Context context, String filePath, float sampleScale) {
        //先得到bitmap的高宽
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        //再用屏幕一半高宽、缩小后的高宽对比，取小值进行缩放
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int reqWidth = wm.getDefaultDisplay().getWidth();
        int reqHeight = wm.getDefaultDisplay().getWidth();
        int scaleWidth = (int) (options.outWidth * sampleScale);
        int scaleHeight = (int) (options.outHeight * sampleScale);
        reqWidth = Math.min(reqWidth, scaleWidth);
        reqHeight = Math.min(reqHeight, scaleHeight);
        options = sampleBitmapOptions(context, options, reqWidth, reqHeight);
        Bitmap bm = BitmapFactory.decodeFile(filePath, options);
        Log.e("xxx", bm.getByteCount() + "");
        return bm;
    }

    public static Bitmap decodeSampleBitMapFromResource(Context context, int resId, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(context.getResources(), resId, options);
        options = sampleBitmapOptions(context, options, reqWidth, reqHeight);
        Bitmap bm = BitmapFactory.decodeResource(context.getResources(), resId, options);
        Log.e("xxx", bm.getByteCount() + "");
        return bm;
    }


    public static Bitmap createBitmapThumbnail(Bitmap bitMap, boolean needRecycle, int newHeight, int newWidth) {
        int width = bitMap.getWidth();
        int height = bitMap.getHeight();
        // 计算缩放比例
        float scale = Math.min((float) newWidth / width, (float) (newHeight) / height);
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        // 得到新的图片
        Bitmap newBitMap = Bitmap.createBitmap(bitMap, 0, 0, width, height, matrix, true);
        if (needRecycle)
            bitMap.recycle();
        return newBitMap;
    }

    public static BitmapFactory.Options sampleBitmapOptions(
            Context context, BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int targetDensity = context.getResources().getDisplayMetrics().densityDpi;
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        double xSScale = ((double) options.outWidth) / ((double) reqWidth);
        double ySScale = ((double) options.outHeight) / ((double) reqHeight);

        double startScale = xSScale > ySScale ? xSScale : ySScale;

        options.inScaled = true;
        options.inDensity = (int) (targetDensity * startScale);
        options.inTargetDensity = targetDensity;
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        return options;
    }

    public static Bitmap getBitmapFromAssets(Context context, String path) {
        InputStream open = null;
        Bitmap bitmap = null;
        try {
            open = context.getAssets().open(path);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options = sampleBitmapOptions(context, options, 10, 10);
            bitmap = BitmapFactory.decodeStream(open, null, options);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
