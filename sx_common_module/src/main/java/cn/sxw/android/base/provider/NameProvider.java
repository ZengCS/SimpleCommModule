package cn.sxw.android.base.provider;

import java.util.Random;

/**
 * Created by ZengCS on 2016/8/17.
 * E-mail:zcs@sxw.cn
 * Add:成都市天府软件园E3-3F
 */
public class NameProvider {
    public static final String[] STUDENT_NAMES = {"周杰伦", "林俊杰", "刘德华", "蔡依林", "林志炫", "罗志祥",
            "五月天", "博尔特", "王力宏", "科比", "姚明", "林宥嘉", "梁咏琪", "孙燕姿", "周星驰", "范冰冰",
            "刘思思", "吴亦凡", "高圆圆", "柯震东", "郭德纲", "岳云鹏", "汪峰", "章子怡", "宋仲基", "薛之谦"
    };

    public static final String[] STUDENT_NAMES_LESS = {"曾成顺", "唐晋义", "韩祥", "张海", "张芸鹏"};


    public static final String[] STUDENT_NAMES_MORE = {"周杰伦", "林俊杰", "刘德华", "蔡依林", "林志炫", "罗志祥",
            "五月天", "博尔特", "王力宏", "科比", "姚明", "林宥嘉", "梁咏琪", "孙燕姿", "周星驰", "范冰冰",
            "刘思思", "吴亦凡", "高圆圆", "柯震东", "郭德纲", "岳云鹏", "汪峰", "章子怡", "宋仲基", "薛之谦",
            "学生01", "学生02", "学生03", "学生04", "学生05", "学生06", "学生07", "学生08", "学生09", "学生0a",
            "学生0b", "学生0c", "学生0d", "学生0e", "学生0f", "学生10", "学生11", "学生12", "学生13", "学生14",
            "学生15", "学生16", "学生17", "学生18", "学生19", "学生1a", "学生1b", "学生1c", "学生1d", "学生1e",
            "学生1f", "学生20", "学生21", "学生22", "学生23", "学生24", "学生25", "学生26", "学生27", "学生28",
            "学生29", "学生2a", "学生2b", "学生2c", "学生2d", "学生2e", "学生2f", "学生30", "学生31", "学生32"
    };

    private static Random random = new Random();
    private static int lastInt = -1;

    public static String getName() {
        int i = random.nextInt(STUDENT_NAMES.length);
        if (i == lastInt) {
            return getName();
        }
        lastInt = i;
        return STUDENT_NAMES[i];
    }
}
