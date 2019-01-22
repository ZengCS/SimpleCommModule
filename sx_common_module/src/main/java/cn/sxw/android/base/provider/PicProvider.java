package cn.sxw.android.base.provider;

import java.util.Random;

/**
 * Created by ZengCS on 2016/8/17.
 * E-mail:zcs@sxw.cn
 * Add:成都市天府软件园E3-3F
 */
public class PicProvider {
    private static final String[] PICS = {
            "http://img.pconline.com.cn/images/upload/upc/tx/wallpaper/1402/12/c1/31189058_1392186616852.jpg",
            "http://a2.att.hudong.com/71/04/300224654811132504044925945_950.jpg",
            "http://img.pconline.com.cn/images/upload/upc/tx/wallpaper/1212/06/c1/16396010_1354784049718.jpg",
            "http://img.pconline.com.cn/images/upload/upc/tx/wallpaper/1408/07/c0/37179063_1407421362265_800x600.jpg",
            "http://upload.univs.cn/2012/0104/1325645518461.jpg",
            "http://image.xcar.com.cn/attachments/a/day_111128/2011112813_13bef7047ddcaa063d94bvHJ3Ie1ksVq.jpg",
            "http://static.yingyonghui.com/screenshots/1545/1545423_2.jpg",
            "http://img15.3lian.com/2015/f1/111/d/21.jpg",
            "http://img1.3lian.com/2015/w7/85/d/102.jpg",
            "http://img15.3lian.com/2015/f1/41/d/80.jpg",
            "http://img3.3lian.com/2013/v9/58/d/33.jpg",
            "http://www.ytxww.com/upload/image/201203/20120309100442708165.jpg",
            "http://img1.3lian.com/img13/c4/98/d/74.jpg",
            "http://img2.3lian.com/2014/f6/173/d/51.jpg",
            "http://b.zol-img.com.cn/desk/bizhi/image/5/960x600/1403577593720.jpg",
            "http://img1.ph.126.net/eO9bWb_YekOV0YYT7uscvQ==/3912220701401427541.jpg",
            "http://img15.3lian.com/2015/f1/41/d/79.jpg",
            "http://a.hiphotos.baidu.com/image/pic/item/0b7b02087bf40ad182fac5ab5a2c11dfa9ecce58.jpg",
            "http://g.hiphotos.baidu.com/image/pic/item/5bafa40f4bfbfbed5572eb3875f0f736afc31f4a.jpg",
            "http://a.hiphotos.baidu.com/image/pic/item/b999a9014c086e062550d0020f087bf40bd1cbfb.jpg",
            "http://b.hiphotos.baidu.com/image/pic/item/bf096b63f6246b608dfd3d31e6f81a4c500fa280.jpg",
            "http://e.hiphotos.baidu.com/image/pic/item/00e93901213fb80e670701903bd12f2eb83894a6.jpg",
            "http://d.hiphotos.baidu.com/image/pic/item/e7cd7b899e510fb38c2414d0d433c895d1430cb3.jpg"
    };
    private static Random random = new Random();
    private static int lastInt = -1;

    public static String getPicture() {
        int i = random.nextInt(PICS.length);
        if (i == lastInt) {
            return getPicture();
        }
        lastInt = i;
        return PICS[i];
    }
}
