package cn.sxw.android.base.okhttp;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import cn.sxw.android.base.ui.BaseApplication;

/**
 * Created by daiyong on 2019/6/5.
 * E-mail:dyong@sxw.cn
 * add:成都天府软件园E3-3F
 */

public class RequestParmUtil {

    /**
     * 获取当前版本号
     *
     * @param @param  context
     * @param @return
     * @param @throws Exception
     * @return String
     * @Title: getVersionName
     * @Description: TODO
     */
    public static String getVersionName(Context context) {
        try {
            return getPackageInfo(context).versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取当前版本号
     *
     * @param @param  context
     * @param @return
     * @param @throws Exception
     * @return String
     * @Title: getVersionName
     * @Description: TODO
     */
    public static int getVersionCode(Context context) {
        try {
            return getPackageInfo(context).versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取
     *
     * @param context
     * @return
     */
    public static PackageInfo getPackageInfo(Context context) {
        // 获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packInfo;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packInfo;
    }

    /**
     * 根据IP地址获取MAC地址
     *
     * @return
     */
    public static String getLocalMacAddressFromIp() {
        String strMacAddr = "";
        try {
            //获得IpD地址
            InetAddress ip = getLocalInetAddress();
            byte[] b = NetworkInterface.getByInetAddress(ip).getHardwareAddress();
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < b.length; i++) {
                if (i != 0) {
                    buffer.append(':');
                }
                String str = Integer.toHexString(b[i] & 0xFF);
                buffer.append(str.length() == 1 ? 0 + str : str);
            }
            strMacAddr = buffer.toString().toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return strMacAddr;
    }

    /**
     * 获取移动设备本地IP
     *
     * @return
     */
    private static InetAddress getLocalInetAddress() {
        InetAddress ip = null;
        try {
            //列举
            Enumeration<NetworkInterface> en_netInterface = NetworkInterface.getNetworkInterfaces();
            while (en_netInterface.hasMoreElements()) {//是否还有元素
                NetworkInterface ni = (NetworkInterface) en_netInterface.nextElement();//得到下一个元素
                Enumeration<InetAddress> en_ip = ni.getInetAddresses();//得到一个ip地址的列举
                while (en_ip.hasMoreElements()) {
                    ip = en_ip.nextElement();
                    if (!ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":") == -1)
                        break;
                    else
                        ip = null;
                }

                if (ip != null) {
                    break;
                }
            }
        } catch (SocketException e) {

            e.printStackTrace();
        }
        return ip;
    }

    /**
     * 获取app类型
     *
     * @return
     */
    public static String getAppType() {
        try {
            String packageName = BaseApplication.getContext().getPackageName();
            if (!TextUtils.isEmpty(packageName) && packageName.contains(".")) {
                return packageName.substring(packageName.lastIndexOf(".") + 1);
            }
        } catch (Exception e) {
        }
        return "";
    }
}
