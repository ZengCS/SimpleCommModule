package cn.sxw.android.base.provider;

import java.util.Random;

/**
 * Created by ZengCS on 2017/9/18.
 * E-mail:zcs@sxw.cn
 * Add:成都市天府软件园E3-3F
 */

public class ChapterProvider {
    private static final String[] INDEX_CHINESE = {"一", "二", "三", "四", "五", "六", "七", "八", "九", "十"};
    private static final String[] MATERIALS = {"高一物理（上）", "高一物理（下）", "高二物理（上）", "高二物理（下）", "高三物理（上）", "高三物理（下）"};
    private static final String[] CHAPTERS = {
            "运动的描述", "匀变速直线运动的研究", "相互作用", "牛顿运动定律", "机械能及其守恒定律",
            "曲线运动", "万有引力与航天", "电流", "磁场", "电磁感应", "电磁波及其应用", "分子动理论 内能",
            "能量的守恒与耗散", "核能", "能源的开发与利用", "电场 直流电路", "交变电流电机", "电磁波通信技术"};
    private static final String[] SECTIONS = {
            "集成电路传感器", "物体的平衡", "材料与结构", "机械与传动装置", "热机", "制冷机", "光的折射",
            "常用光学仪器", "光的干涉、衍射和偏振", "光源与激光", "放射性与原子核", "核能与反应堆技术",
            "静电场", "恒定电流", "传感器", "动量守恒定律", "波粒二象性", "物态和物态变化", "分子动理论"};
    private static Random random = new Random();

//    /**
//     * 生成教材列表
//     */
//    public static List<ChapterBean> generateMaterialList() {
//        List<ChapterBean> list = JListKit.newArrayList();
//        for (String s : MATERIALS) {
//            list.add(new ChapterBean(generateId(), ChapterBean.TYPE_MATERIAL, s));
//        }
//        list.get(0).setChecked(true);
//        return list;
//    }

    /**
     * 生成章列表
     */
//    public static List<ChapterBean> generateChapterList() {
//        List<ChapterBean> list = JListKit.newArrayList();
//        int len = random.nextInt(8) + 2;
//        int count = CHAPTERS.length;
//        for (int i = 0; i < len; i++) {
//            String name = "第" + INDEX_CHINESE[i] + "章 " + CHAPTERS[random.nextInt(count)];
//            list.add(new ChapterBean(generateId(), ChapterBean.TYPE_CHAPTER, name));
//        }
//        list.get(0).setChecked(true);
//        return list;
//    }

    /**
     * 生成节列表
     */
//    public static List<ChapterBean> generateSectionList() {
//        List<ChapterBean> list = JListKit.newArrayList();
//        int len = random.nextInt(8) + 2;
//        int count = SECTIONS.length;
//        for (int i = 0; i < len; i++) {
//            String name = "第" + INDEX_CHINESE[i] + "节 " + SECTIONS[random.nextInt(count)];
//            list.add(new ChapterBean(ChapterBean.TYPE_SECTION, name));
//        }
//        list.get(0).setChecked(true);
//        return list;
//    }

    private static int generateId() {
        return (random.nextInt(100) + 1) * random.nextInt(50) + 1;
    }
}
