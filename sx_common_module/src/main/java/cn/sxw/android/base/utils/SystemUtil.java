package cn.sxw.android.base.utils;

import java.util.Locale;

/**
 * 系统工具类
 */
public class SystemUtil {

    /**
     * 获取当前手机系统语言。
     *
     * @return 返回当前系统语言。例如：当前设置的是“中文-中国”，则返回“zh-CN”
     */
    public static String getSystemLanguage() {
        return Locale.getDefault().getLanguage();
    }

    /**
     * 获取当前系统上的语言列表(Locale列表)
     *
     * @return 语言列表
     */
    public static Locale[] getSystemLanguageList() {
        return Locale.getAvailableLocales();
    }

    /**
     * 获取当前手机系统版本号
     *
     * @return 系统版本号
     */
    public static String getSystemVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * 获取手机型号
     *
     * @return 手机型号
     */
    public static String getSystemModel() {
        // C5 BZT-W09
        // T3 BZT-L00
        return android.os.Build.MODEL;
    }

    /**
     * 获取手机厂商
     *
     * @return 手机厂商
     */
    public static String getDeviceBrand() {
        return android.os.Build.BRAND;
    }

    public static boolean isHuaweiPad() {
        return "HUAWEI".equalsIgnoreCase(getDeviceBrand());
    }

    public static boolean isLenovo() {
        return "Lenovo".equalsIgnoreCase(getDeviceBrand());
    }

    public static boolean isSamsung() {
        return "samsung".equalsIgnoreCase(getDeviceBrand());
    }

    /**
     * 设备是否是华为C5系列
     */
    public static boolean isHwC5() {
        return "BZT-W09".equals(getSystemModel());
    }

    /**
     * 设备是否是华为C3系列
     */
    public static boolean isHwC3() {
        return "BZA-W00".equals(getSystemModel());
    }

    /**
     * 设备是否是华为T3系列
     */
    public static boolean isHwT3() {
        return "BZT-L00".equals(getSystemModel());
    }

    /**
     * 设备是否是华为T5系列
     */
    public static boolean isHwT5() {
        return "AGS2-AL00".equals(getSystemModel());
    }
}