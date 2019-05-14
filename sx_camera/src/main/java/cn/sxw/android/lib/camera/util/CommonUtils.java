package cn.sxw.android.lib.camera.util;

/**
 * Created by ZengCS on 2019/5/14.
 * E-mail:zcs@sxw.cn
 * Add:成都市天府软件园E3-3F
 */
public class CommonUtils {
    public static String timeToString4Timer(long time) {
        int s = (int) (time / 1000);
        int m = s / 60;
        int h = m / 60;

        String sh, sm, ss;
        if (h > 0) {
            sh = h < 10 ? ("0" + h + ":") : (h + ":");
        } else {
            sh = "00:";
        }
        m = m % 60;
        sm = m < 10 ? ("0" + m + ":") : (m + ":");

        s = s % 60;
        ss = s < 10 ? ("0" + s) : String.valueOf(s);
        return sh.concat(sm).concat(ss);
    }

}
