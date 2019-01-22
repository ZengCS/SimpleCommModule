package cn.sxw.android.base.utils;

import java.text.NumberFormat;

/**
 * Created by ZengCS on 2017/3/2.
 * E-mail:zcs@sxw.cn
 * Add:成都市天府软件园E3-3F
 */
public class JNumberUtil {
    private static NumberFormat percentFormat = null;
    private static NumberFormat numberFormat = null;

    private static NumberFormat newNumberInstance() {
        if (numberFormat == null) {
            synchronized (JNumberUtil.class) {
                numberFormat = NumberFormat.getNumberInstance();
                numberFormat.setMaximumIntegerDigits(3);// 整数显示最多位数超出前面截取
                numberFormat.setMaximumFractionDigits(2);// 小数显示最多位数超出四舍五入
            }
        }
        return numberFormat;
    }

    private static NumberFormat newPercentInstance() {
        if (percentFormat == null) {
            synchronized (JNumberUtil.class) {
                percentFormat = NumberFormat.getPercentInstance();
                percentFormat.setMaximumIntegerDigits(3);// 整数显示最多位数超出前面截取
                percentFormat.setMaximumFractionDigits(2);// 小数显示最多位数超出四舍五入
            }
        }
        return percentFormat;
    }

    public static double cutDoubleNum(double num) {
        String format = newNumberInstance().format(num);
        return Double.parseDouble(format);
    }

    /**
     * 小数转百分比
     */
    public static String numToPercent(Double num) {
        if (num == null) {
            return "0.00%";
        }
        return newPercentInstance().format(num);
    }
}
