/**
 * Android存储路径分 SDCard,手机内存
 * <p/>
 * 手机内存一般存在/data/data/package/files目录下
 */
package cn.sxw.android.base.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;

import java.io.File;
import java.io.FilenameFilter;

import cn.sxw.android.base.exception.CustomBaseException;

/**
 * Android操作文件工具类
 */
public class JFileKit {

    /**
     * 检测 SD 是否可写
     *
     * @return
     */
    public static boolean sdcardIsReadyForWrite() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /**
     * 检测 SD 是否可读
     *
     * @return
     */
    public static boolean sdcardIsReadyForRead() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    /**
     * 获取SDCard的路径
     *
     * @return
     */
    public static String getSDCardPath() {
        if (!sdcardIsReadyForWrite()) {
            throw new CustomBaseException("SDCard不是可读写模式");
        }

        String sdcard = Environment.getExternalStorageDirectory().getAbsolutePath();

        return sdcard;

    }

    /**
     * 根据传入的uniqueName获取硬盘缓存的路径地址
     *
     * @author blue
     */
    public static String getDiskCacheDir(Context context) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {
            File file = context.getExternalCacheDir();
            if (!file.exists()) {
                if (file.mkdirs()) {
                    cachePath = context.getExternalCacheDir().getAbsolutePath();
                } else {
                    cachePath = context.getCacheDir().getAbsolutePath();
                }
            } else {
                cachePath = context.getExternalCacheDir().getAbsolutePath();
            }
        } else {
            cachePath = context.getCacheDir().getAbsolutePath();
        }
        return cachePath;
    }

    /**
     * 在SDCard 创建目录 或文件
     * <p/>
     * 如果存在同名文件则删除文件后再创建,同名文件夹不做任何操作
     *
     * @throws CustomBaseException
     */
    public static String createFileOnSDCard(String path) {
        if (!sdcardIsReadyForWrite()) {
            throw new CustomBaseException("SD卡不可写");
        }

        if (!path.startsWith(File.separator)) {
            path = File.separator + path;
        }

        File file = new File(getSDCardPath() + path);

        if (file.isFile() && file.exists()) {
            file.delete();
        }

        if (!file.exists()) {
            if (!file.mkdirs()) {
                throw new CustomBaseException("文件/文件夹" + path + "创建失败");
            }
        }

        return getSDCardPath() + path;
    }

    /**
     * 删除SDCard文件或文件夹
     *
     * @param path 文件路径
     */
    public static void deleteFileOnSDCard(String path) {
        if (!sdcardIsReadyForWrite()) {
            throw new CustomBaseException("SD卡不可写");
        }

        if (!path.startsWith(File.separator)) {
            path = File.separator + path;
        }

        File file = new File(getSDCardPath() + path);

        if (file.isFile() && file.exists()) {
            if (!file.delete()) {
                throw new CustomBaseException("删除文件" + path + "失败");
            }
        }
    }

    /**
     * 获取data/data/package/files目录
     * <p/>
     * 该目录在卸载程序时自动删除
     *
     * @param context
     * @return
     */
    public static String getDataFolderPath(Context context) {

        String sdcard = context.getFilesDir().getAbsolutePath();

        return sdcard;
    }

    /**
     * 在data/data/package/files创建目录 或文件
     * <p/>
     * 如果存在同名文件则删除文件后再创建,同名文件夹不做任何操作
     * <p/>
     * 该目录在卸载程序时自动删除
     *
     * @param path 文件夹绝对路径
     * @throws CustomBaseException
     */
    public static String createFileOnDataFolder(Context contenxt, String path) {
        if (!path.startsWith(File.separator)) {
            path = File.separator + path;
        }

        File file = new File(getDataFolderPath(contenxt) + path);

        if (file.isFile() && file.exists()) {
            file.delete();
        }

        if (!file.exists()) {
            if (!file.mkdirs()) {
                throw new CustomBaseException("文件/文件夹" + path + "创建失败");
            }
        }

        return getDataFolderPath(contenxt) + path;
    }

    /**
     * 删除data/data/package/files文件或文件夹
     * <p/>
     * 该目录在卸载程序时自动删除
     *
     * @param path 文件路径
     */
    public static void deleteFileOnDataFolder(String path) {
        if (!path.startsWith(File.separator)) {
            path = File.separator + path;
        }

        File file = new File(getSDCardPath() + path);

        if (file.isFile() && file.exists()) {
            if (!file.delete()) {
                throw new CustomBaseException("删除文件" + path + "失败");
            }
        }
    }

    /**
     * Java文件操作 获取文件扩展名
     */
    public static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return filename;
    }

    /**
     * Java文件操作 获取不带扩展名的文件名
     */
    public static String getFileNameNoEx(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length()))) {
                return filename.substring(0, dot);
            }
        }
        return filename;
    }

    /**
     * 递归删除指定文件夹下的所有文件（包括该文件夹）
     *
     * @param file
     * @author andrew
     */
    public static void deleteAll(File file) {
        try {
            if (file.isFile() || file.list() != null && file.list().length == 0) {
                file.delete();
            } else {
                File[] files = file.listFiles();
                if (files != null)
                    for (File f : files) {
                        deleteAll(f);// 递归删除每一个文件
                        f.delete();// 删除该文件夹
                    }
                file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static final String CONTENT = "content";
    private static final String FILE = "file";

    public static String getVideoPath(Context context, Uri uri) {
        String filePath = null;
        if (CONTENT.equalsIgnoreCase(uri.getScheme())) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Video.Media.DATA}, null, null, null);
            if (null == cursor) {
                return null;
            }
            try {
                if (cursor.moveToNext()) {
                    filePath = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
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
     * 删除指定目录中特定的文件
     *
     * @param dir
     * @param filter
     */
    public static void delete(String dir, FilenameFilter filter) {
        if (TextUtils.isEmpty(dir))
            return;
        File file = new File(dir);
        if (!file.exists())
            return;
        if (file.isFile())
            file.delete();
        if (!file.isDirectory())
            return;

        File[] lists = null;
        if (filter != null)
            lists = file.listFiles(filter);
        else
            lists = file.listFiles();

        if (lists == null)
            return;
        for (File f : lists) {
            if (f.isFile()) {
                f.delete();
            }
        }
    }

    /**
     * 分隔符.
     */
    public final static String FILE_EXTENSION_SEPARATOR = ".";

    /**
     * 获得不带扩展名的文件名称
     *
     * @param filePath 文件路径
     * @return
     */
    public static String getFileNameWithoutExtension(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return filePath;
        }
        int extenPosi = filePath.lastIndexOf(FILE_EXTENSION_SEPARATOR);
        int filePosi = filePath.lastIndexOf(File.separator);
        if (filePosi == -1) {
            return (extenPosi == -1 ? filePath : filePath.substring(0,
                    extenPosi));
        }
        if (extenPosi == -1) {
            return filePath.substring(filePosi + 1);
        }
        return (filePosi < extenPosi ? filePath.substring(filePosi + 1,
                extenPosi) : filePath.substring(filePosi + 1));
    }

}
