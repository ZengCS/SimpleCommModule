package com.zcs.android.lib.sketch.helper;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by ZengCS on 2018/2/8.
 * E-mail:zcs@sxw.cn
 * Add:成都市天府软件园E3-3F
 */

public interface ISketchPenHelper {

    /**
     * 清空上一次的点
     */
    void cleanPoints(boolean init);

    /**
     * 初始化
     */
    void init(Paint paint);

    /**
     * 铅笔书写
     */
    void writeWithPencil();

    /**
     * 钢笔书写
     */
    void writeWithPen(Canvas mSketchCanvas, float eventX, float eventY);

    /**
     * 毛笔书写
     */
    void writeWithBrush();

    /**
     * 粉笔书写
     */
    void writeWithChalk();
}
