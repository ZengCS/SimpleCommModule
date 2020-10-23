package cn.sxw.android.base.utils;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Android操作日期工具类
 */
public class JDateKit {
    private static final String TAG = "JDateKit";
    public static final long ONE_HOUR = 1000 * 60 * 60;

    public static String getLastDay(String baseDay) {
        if (TextUtils.isEmpty(baseDay)) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date base = sdf.parse(baseDay);
            Calendar c = Calendar.getInstance();
            c.setTime(base);
            c.add(Calendar.DAY_OF_MONTH, -1);
            String yesterday = sdf.format(c.getTime());
            return yesterday;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 将目标时间转换成 今天/昨天/日期
     *
     * @param date 必须是yyyy-MM-dd格式
     */
    public static String dateToDay(String date) {
        if (TextUtils.isEmpty(date)) {
            return "";
        }
        SimpleDateFormat sdf = null;
        try {
            sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date curr = sdf.parse(date);
            date = sdf.format(curr);
        } catch (ParseException e) {
            e.printStackTrace();
            LogUtil.e(e);
        }

        Calendar c = Calendar.getInstance();
        String today = sdf.format(c.getTime());
        if (date.equals(today)) {
            return "今天";
        }
        c.add(Calendar.DAY_OF_MONTH, -1);
        String yesterday = sdf.format(c.getTime());
        if (date.equals(yesterday)) {
            return "昨天";
        }
        return date;
    }

    public static String timeToString(long time) {
        // LogUtil.w("timeToString: time = " + time);
        int s = (int) (time / 1000);
        int m = s / 60;
        int h = m / 60;
        int d = h / 24;
        if (d > 0) {
            return d + "d" + h % 24 + "h" + m % 60 + "'" + s % 60 + "\"";
        } else if (h > 0) {
            return h % 24 + "h" + m % 60 + "'" + s % 60 + "\"";
        } else if (m > 0) {
            return m % 60 + "'" + s % 60 + "\"";
        } else {
            return "0'" + s % 60 + "\"";
        }
    }

    public static String timeToStringChineChinese(long time) {
        // LogUtil.w("timeToString: time = " + time);
        int s = (int) (time / 1000);
        int m = s / 60;
        int h = m / 60;
        int d = h / 24;
        if (d > 0) {
            return d + "天" + h % 24 + "小时" + m % 60 + "分" + s % 60 + "秒";
        } else if (h > 0) {
            return h % 24 + "小时" + m % 60 + "分" + s % 60 + "秒";
        } else if (m > 0) {
            return m % 60 + "分" + s % 60 + "秒";
        } else {
            return s % 60 + "秒";
        }
    }


    /**
     * 将Date转化成String
     *
     * @return
     */
    public static String dateToStr(Date date) {
        return dateToStr("yyyy-MM-dd", date);
    }

    public static String currDateTime() {
        return dateToStr("yyyy-MM-dd HH:mm:ss", new Date());
    }

    /**
     * 将Date转化成String
     *
     * @return
     */
    public static String dateToStr(String template, Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat(template, Locale.CHINA);
        String dateString = formatter.format(date);
        return dateString;
    }

    public static String dateToStr(String template, String date) {
        Date d = getDateByDateStr("yyyy-MM-dd HH:mm:ss", date);

        SimpleDateFormat formatter = new SimpleDateFormat(template, Locale.CHINA);

        String dateString = formatter.format(d);
        return dateString;
    }

    /**
     * 返回当月的第一天
     *
     * @return
     */
    public static Date getFirstDay() {
        Calendar calendar = Calendar.getInstance();
        Date theDate = calendar.getTime();
        GregorianCalendar gcLast = (GregorianCalendar) Calendar.getInstance();
        gcLast.setTime(theDate);
        gcLast.set(Calendar.DAY_OF_MONTH, 1);
        return gcLast.getTime();
    }

    public static String getToday(String template) {
        SimpleDateFormat sdf = new SimpleDateFormat(template, Locale.CHINA);
        return sdf.format(new Date());
    }

    /**
     * 根据时间字符串得到日期
     *
     * @param dateStr
     * @return
     * @author andrew
     */
    public static Date getDateByDateStr(String dateStr) {
        return getDateByDateStr("yyyy-MM-dd", dateStr);
    }

    /**
     * 根据指定格式的时间字符串得到日期
     *
     * @param dateStr
     * @return
     * @author andrew
     */
    public static Date getDateByDateStr(String template, String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat(template, Locale.CHINA);
        ParsePosition pos = new ParsePosition(0);
        return sdf.parse(dateStr, pos);
    }

    /**
     * 根据时间字符串得到Calendar
     *
     * @param dateStr
     * @return
     */
    public static Calendar getCalendarByDateStr(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        Calendar calendar = Calendar.getInstance();
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = sdf.parse(dateStr, pos);
        calendar.setTime(strtodate);
        return calendar;
    }

    /**
     * 得到选择的日期的近几天
     *
     * @return
     */
    public static Date getLatelyDate(Date date, int day) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, day);
        return cal.getTime();
    }

    /**
     * 计算两个日期之间相差的天数
     *
     * @param smdate 较小的时间
     * @param bdate  较大的时间
     * @return 相差天数
     * @throws ParseException
     */
    public static int daysBetween(Date smdate, Date bdate) throws ParseException {
        return daysBetween(smdate, bdate, "yyyy-MM-dd");
    }

    /**
     * 计算两个日期之间相差的天数
     *
     * @param smdate 较小的时间
     * @param bdate  较大的时间
     * @return 相差天数
     * @throws ParseException
     */
    public static int daysBetween(Date smdate, Date bdate, String template) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(template, Locale.CHINA);
        smdate = sdf.parse(sdf.format(smdate));
        bdate = sdf.parse(sdf.format(bdate));
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        long between_days = Math.abs((time2 - time1)) / (1000 * 3600 * 24);

        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * 将时间毫秒数 转成日期
     */
    public static String timeToDate(long time) {
        Date date = new Date(time);
        return dateToStr(date);
    }

    public static String timeToDate(String format, long time) {
        Date date = new Date(time);
        return dateToStr(format, date);
    }
}
