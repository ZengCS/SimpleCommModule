package cn.sxw.android.lib.camera.util;

import android.graphics.Bitmap;
import android.os.Environment;
import android.text.TextUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.sxw.android.lib.camera.core.CameraApp;

public class FileUtil {
    private static String storagePath = "";
    private static String DST_FOLDER_NAME = "ZCameraPic";

    /**
     * 检验SDcard状态
     */
    private static boolean checkSDCard() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public static String getStoragePath(){
        if (CameraApp.getApp() != null && checkSDCard()) {
            File externalCacheDir = CameraApp.getApp().getExternalCacheDir();
            if (externalCacheDir != null) {
                return externalCacheDir.getAbsolutePath();
            }
        }
        return "";
    }

    private static String initPath() {
        if (TextUtils.isEmpty(storagePath)) {
            storagePath = getStoragePath()
                    .concat(File.separator)
                    .concat(DST_FOLDER_NAME);
            File f = new File(storagePath);
            if (!f.exists()) {
                f.mkdir();
            }
        }
        return storagePath;
    }

    public static String saveBitmap(String dir, Bitmap b) {
        DST_FOLDER_NAME = dir;
        String path = initPath();
        long dataTake = System.currentTimeMillis();
        String jpegName = path + File.separator + "picture_" + dataTake + ".jpg";
        try {
            FileOutputStream fout = new FileOutputStream(jpegName);
            BufferedOutputStream bos = new BufferedOutputStream(fout);
            b.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
            return jpegName;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static boolean deleteFile(String url) {
        boolean result = false;
        File file = new File(url);
        if (file.exists()) {
            result = file.delete();
        }
        return result;
    }

    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }
}
