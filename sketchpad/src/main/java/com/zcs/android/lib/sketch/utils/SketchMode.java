package com.zcs.android.lib.sketch.utils;

import java.io.Serializable;

/**
 * Created by ZengCS on 2017/10/11.
 * E-mail:zcs@sxw.cn
 * Add:成都市天府软件园E3-3F
 */

public class SketchMode implements Serializable {
    public static final int MODE_DFT = -1;

    public static final class Mode {
        public static final int PEN_WRITE = 0;// 笔
        public static final int ERASER = 1;// 橡皮擦
        public static final int TEXT = 2;// 文本
        public static final int GEOMETRIC = 3;// 几何图形
        public static final int SPOTLIGHT = 4;// 聚光灯
        public static final int EDIT_PIC = 5;// 添加图片
    }

    public static final class Pen {
        public static final int PENCIL = 0;// 铅笔
        public static final int PEN = 1;// 钢笔
        public static final int BRUSH = 2;// 毛笔
        public static final int CHALK = 3;// 粉笔
    }

    public static final class Text {
        public static final int NONE = -1;
        public static final int NORMAL = 0;
        public static final int BIG = 1;
        public static final int LARGE = 2;
    }

    public static final class Eraser {
        public static final int SMALL = 0;
        public static final int LARGE = 1;
    }

    public static final class Geometric {
        public static final int RECTANGLE = 0;// 矩形
        public static final int CIRCLE = 1;// 圆形
        public static final int TRIANGLE = 2;// 三角形
        public static final int RA_TRIANGLE = 3;// 直角三角形
        public static final int LINE = 4;// 直线
        public static final int QUAD = 5;// 波浪线
        public static final int PARALLELOGRAM = 6;// 平行四边形
        public static final int ECHELON = 7;// 梯形
        public static final int DIAMOND = 8;// 菱形
        public static final int PENTAGON = 9;// 五边形
        public static final int LINE_WITH_ARROW = 10;// 直线箭头
        public static final int COORDINATE_AXIS = 11;// 坐标轴
    }

//    public static final class ArrowType {
//        public static final int NORMAL_FILL = 0;
//        public static final int SMALL_STROKE = 1;
//        public static final int BIG_STROKE = 2;
//    }

    /**
     * 操作类型
     */
    public static final class OperateType {
        public static final int OPERATE_WAIT = -1;// 等待操作
        public static final int OPERATE_DRAW = 0;// 作画
        public static final int OPERATE_DRAG = 1;// 拖拽
        public static final int OPERATE_RESIZE = 2;// 改变大小
    }

    private int modeType;
    private int drawType;

    public SketchMode() {
    }

    public SketchMode(int modeType, int drawType) {
        this.modeType = modeType;
        this.drawType = drawType;
    }

    public int getModeType() {
        return modeType;
    }

    public int getDrawType() {
        return drawType;
    }
}
