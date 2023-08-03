package cn.sxw.android.base.utils;

import android.content.Context;
import android.os.Build;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import cn.sxw.android.base.ui.BaseApplication;

/**
 * @Description 文件工具类
 * @Author kk20
 * @Date 2017/5/17
 * @Version V1.0.0
 */
public class IFileUtils {

    /**
     * 兼容Android 11存储方式更新 采用Context#getExternalFilesDir方式，无需申请权限
     * 存储路径：/storage/emulated/0/Android/data/{applicationId}/files
     * @return 返回文件对象
     */
    public static File getExternalFileDir(){
        //sdcard正常挂载，可读写
        if(checkSDCard()){
            return BaseApplication.getContext().getExternalFilesDir(null);
        }
        return Environment.getDataDirectory();
    }

    /**
     * 兼容Android 11存储方式更新 采用Context#getExternalFilesDir方式
     * @return 返回目录路径
     */
    public static String getExternalFileDirPath(){
        return getExternalFileDir().getAbsolutePath();
    }

    /**
     * 检验SDcard状态
     */
    private static boolean checkSDCard() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 按名称创建目录
     *
     * @param filePath eg:"test/temp/"
     * @return 文件夹完整路径
     */
    public static String mkdirs(String filePath) {
        String rootPath = getExternalFileDirPath();
        if (rootPath != null) {
            File dir = new File(rootPath + filePath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            return dir.getAbsolutePath();
        }
        return null;
    }

    /**
     * 保存文件数据到SD卡
     *
     * @param filePath
     * @param fileName
     * @param content
     * @throws Exception
     */
    public static void save2SDCard(String filePath, String fileName, String content)
            throws Exception {
        // 从API中获取SDCard的路径，解决各种Android系统的兼容性问题
        String path = mkdirs(filePath);
        if (path != null) {
            // 创建文件
            File file = new File(path, fileName);
            // 新建一个文件的输出流
            FileOutputStream outStream = new FileOutputStream(file);
            // 向文件中写入数据，将字符串转换为字节
            outStream.write(content.getBytes());
            // 关掉这个流
            outStream.close();
        }
    }

    /**
     * 保存数据到指定文件
     *
     * @param context
     * @param fileName
     * @param content
     * @throws Exception
     */
    public static void save(Context context, String fileName, String content) throws Exception {
        // 利用javaIO实现文件的保存
        FileOutputStream outStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
        outStream.write(content.getBytes());
        outStream.close();
    }

    /**
     * 解压文件到指定目录
     *
     * @param zipPath
     * @param descDir
     */
    public static boolean unZipFile(String zipPath, String descDir) throws IOException {
        File zipFile = new File(zipPath);
        if (!zipFile.exists()) {
            // System.out.println("压缩文件不存在");
            return false;
        }

        File pathFile = new File(descDir);
        if (!pathFile.exists()) {
            pathFile.mkdirs();
        }
        ZipFile zip = new ZipFile(zipFile);
        for (Enumeration entries = zip.entries(); entries.hasMoreElements(); ) {
            ZipEntry entry = (ZipEntry) entries.nextElement();
            String zipEntryName = entry.getName();
            if (zipEntryName.contains("../")) {
                continue;
            }
            InputStream in = zip.getInputStream(entry);
            String outPath = (descDir + zipEntryName).replaceAll("\\*", "/");
            // 判断路径是否存在,不存在则创建文件路径
            File file = new File(outPath.substring(0, outPath.lastIndexOf('/')));
            if (!file.exists()) {
                file.mkdirs();
            }
            // 判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
            if (new File(outPath).isDirectory()) {
                continue;
            }
            //输出文件路径信息
            // System.out.println(outPath);

            OutputStream out = new FileOutputStream(outPath);
            byte[] buf1 = new byte[1024];
            int len;
            while ((len = in.read(buf1)) > 0) {
                out.write(buf1, 0, len);
            }
            in.close();
            out.close();
        }
        // System.out.println("解压完毕");
        return true;
    }
}
