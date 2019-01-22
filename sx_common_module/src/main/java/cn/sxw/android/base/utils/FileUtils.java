package cn.sxw.android.base.utils;

import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {
    /**
     * @param key
     * @return
     */
    public static String getFilenameForKey(String key) {
        if (key.startsWith("#")) {
            key = key.substring(key.indexOf("http:"));
        }
        int firstHalfLength = key.length() / 2;
        String localFilename = String.valueOf(key.substring(0, firstHalfLength).hashCode());
        localFilename += String.valueOf(key.substring(firstHalfLength).hashCode());
        return localFilename;
    }

    /**
     * 删除整个目录
     *
     * @param file
     */
    public static void deleteDir(File file) {
        if (!file.exists()) {
            return;
        }

        if (file.isFile()) {
            Log.d("CacheFile", "delete-->[" + file.length() + "]" + file.getPath());
            file.delete();
            return;
        }

        File[] lists = file.listFiles();
        for (File f : lists) {
            deleteDir(f);
        }
        // 最后删除当前目录
//        Log.d("CacheFile", "最后删除当前目录delete-->[" + file.length() + "]" + file.getPath());
//        file.delete();
    }

    /**
     * 获取目录文件大小
     *
     * @param dir
     * @return
     */
    public static long getDirSize(File dir) {
        if (null == dir || !dir.exists()) {
            return 0;
        }
        if (!dir.isDirectory()) {
            return 0;
        }
        long dirSize = 0;
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                Log.d("CacheFile", "[" + file.length() + "]" + file.getPath());
                dirSize += file.length();
            } else if (file.isDirectory()) {
//                dirSize += file.length();
                dirSize += getDirSize(file); // 递归调用继续统计
            }
        }
        return dirSize;
    }

    public static List<File> getDirFileList(File dir) {
        if (null == dir || !dir.exists()) {
            return null;
        }
        if (!dir.isDirectory()) {
            return null;
        }
        List<File> fileList = new ArrayList<>();
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                LogUtil.d("CacheFile", "[" + file.length() + "]" + file.getPath());
                fileList.add(file);
            } else if (file.isDirectory()) {
                fileList.addAll(getDirFileList(file));// 递归调用继续统计
            }
        }
        return fileList;
    }

    /**
     * 转换文件大小
     *
     * @param fileS
     * @return B/KB/MB/GB
     */
    public static String formatFileSize(long fileS) {
        if (fileS == 0) {
            return "0.00 B";
        }

        java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + " B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + " KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + " MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + " G";
        }
        return fileSizeString;
    }

    public static int clearCacheFolder(File dir, long curTime) {
        int deletedFiles = 0;
        if (dir != null && dir.isDirectory()) {
            try {
                for (File child : dir.listFiles()) {
                    if (child.isDirectory()) {
                        deletedFiles += clearCacheFolder(child, curTime);
                    }
                    if (child.lastModified() < curTime) {
                        if (child.delete()) {
                            deletedFiles++;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return deletedFiles;
    }

    public static String getFileType(String name) {
        return name.substring(name.lastIndexOf("."));
    }

    public static String getFileName(String name) {
        return name.substring(0, name.lastIndexOf("."));
    }

    /**
     * 删除文件夹
     *
     * @param file
     */
    public static void delFileDir(File file) {
        if (!file.exists()) {
            return;
        }
        try {
            file.delete();
        } catch (Exception e) {
        }
    }

}
