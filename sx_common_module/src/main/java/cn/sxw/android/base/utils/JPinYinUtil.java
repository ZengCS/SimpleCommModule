package cn.sxw.android.base.utils;

import java.io.UnsupportedEncodingException;
import java.util.Random;

/**
 * Created by ZengCS on 2018/4/23.
 * E-mail:zcs@sxw.cn
 * Add:成都市天府软件园E3-3F
 */

public class JPinYinUtil {
    private static final int GB_SP_DIFF = 160;
    // 存放国标一级汉字不同读音的起始区位码
    private static final int[] secPosValueList = {1601, 1637, 1833, 2078, 2274, 2302,
            2433, 2594, 2787, 3106, 3212, 3472, 3635, 3722, 3730, 3858, 4027,
            4086, 4390, 4558, 4684, 4925, 5249, 5600};
    // 存放国标一级汉字不同读音的起始区位码对应读音
    private static final char[] firstLetter = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
            'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'W', 'X',
            'Y', 'Z'};

    public static String getSpells(String characters) {
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < characters.length(); i++) {
            char ch = characters.charAt(i);
            if ((ch >> 7) == 0) {
                // 判断是否为汉字，如果左移7为为0就不是汉字，否则是汉字
                continue;
            }
            Character firstLetter = getFirstLetter(ch);
            if (firstLetter == null) {
                return "*";
            }
            buffer.append(String.valueOf(firstLetter));
        }
        return buffer.toString();
    }

    // 获取一个汉字的首字母
    public static Character getFirstLetter(char ch) {
        byte[] uniCode;
        try {
            uniCode = String.valueOf(ch).getBytes("GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
        if (uniCode[0] < 128 && uniCode[0] > 0) { // 非汉字
            return null;
        } else {
            return convert(uniCode);
        }
    }

    /**
     * 获取一个汉字的拼音首字母。 GB码两个字节分别减去160，转换成10进制码组合就可以得到区位码
     * 例如汉字“你”的GB码是0xC4/0xE3，分别减去0xA0（160）就是0x24/0x43
     * 0x24转成10进制就是36，0x43是67，那么它的区位码就是3667，在对照表中读音为‘n’
     */
    private static char convert(byte[] bytes) {
        char result = '-';
        int i;
        for (i = 0; i < bytes.length; i++) {
            bytes[i] -= GB_SP_DIFF;
        }
        int secPosValue = bytes[0] * 100 + bytes[1];
        for (i = 0; i < 23; i++) {
            if (secPosValue >= secPosValueList[i]
                    && secPosValue < secPosValueList[i + 1]) {
                result = firstLetter[i];
                break;
            }
        }
        return result;
    }

    public static String randomFirstLetter() {
        int len = firstLetter.length;
        int position = new Random().nextInt(len);
        return String.valueOf(firstLetter[position]);
    }
}
