package cn.sxw.android.base.provider;

/**
 * Created by ZengCS on 2017/9/28.
 * E-mail:zcs@sxw.cn
 * Add:成都市天府软件园E3-3F
 */

public class OptionProvider {
    private static final String[] OPTION_CHOOSE = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K"};
    private static final String[] OPTION_JUDGE = {"正确", "错误"};

    public static String getChoseOption(int position) {
        try {
            return OPTION_CHOOSE[position];
        } catch (Exception e) {
            e.printStackTrace();
            return "未知";
        }
    }

    public static String getJudgeOption(int position) {
        try {
            return OPTION_JUDGE[position];
        } catch (Exception e) {
            e.printStackTrace();
            return "未知";
        }
    }

}
