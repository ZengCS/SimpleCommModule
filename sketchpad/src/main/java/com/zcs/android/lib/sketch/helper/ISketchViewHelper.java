package com.zcs.android.lib.sketch.helper;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

import com.zcs.android.lib.sketch.bean.DrawPoint;
import com.zcs.android.lib.sketch.bean.DashRectangle;

import java.util.List;

/**
 * Created by ZengCS on 2017/12/15.
 * E-mail:zcs@sxw.cn
 * Add:成都市天府软件园E3-3F
 */

public interface ISketchViewHelper {
    /**
     * 画平行四边形
     *
     * @param canvas        画布
     * @param geoPath       图形路径
     * @param paint         画笔工具
     * @param dashRectangle 虚线矩形范围
     * @param dashPath      虚线路径
     */
    List<DrawPoint> drawParallelogram(Canvas canvas, Path geoPath, Paint paint, DashRectangle dashRectangle, Path dashPath);

    /**
     * 画矩形
     */
    RectF drawRectangle(Canvas canvas, Paint paint, DashRectangle dashRectangle, Path dashPath);

    /**
     * 画圆
     */
    RectF drawCircle(Canvas canvas, Paint paint, DashRectangle dashRectangle, Path dashPath);

    /**
     * 画三角形
     */
    List<DrawPoint> drawTriangle(Canvas canvas, Path geoPath, Paint paint, DashRectangle dashRectangle, Path dashPath);

    /**
     * 画正三角形
     */
    List<DrawPoint> drawRegularTriangle(Canvas canvas, Path geoPath, Paint paint, DashRectangle dashRectangle, Path dashPath);

    /**
     * 画直角三角形
     */
    List<DrawPoint> drawRATriangle(Canvas canvas, Path geoPath, Paint paint, DashRectangle dashRectangle, Path dashPath);

    /**
     * 画梯形
     */
    List<DrawPoint> drawEchelon(Canvas canvas, Path geoPath, Paint paint, DashRectangle dashRectangle, Path dashPath);

    /**
     * 画菱形
     */
    List<DrawPoint> drawDiamond(Canvas canvas, Path geoPath, Paint paint, DashRectangle dashRectangle, Path dashPath);

    /**
     * 画五边形
     */
    List<DrawPoint> drawPentagon(Canvas canvas, Path geoPath, Paint paint, DashRectangle dashRectangle, Path dashPath);

    /**
     * 画直线
     */
    List<DrawPoint> drawLine(Canvas canvas, Path geoPath, Paint paint, DashRectangle dashRectangle, Path dashPath);

    /**
     * 画曲线
     */
    List<DrawPoint> drawQuadLine(Canvas canvas, Path geoPath, Paint paint, DashRectangle dashRectangle, Path dashPath);

    /**
     * 画直线箭头
     */
    List<DrawPoint> drawLineWithArrow(Canvas canvas, Path geoPath, Paint paint, DashRectangle dashRectangle, Path dashPath);

    /**
     * 画坐标轴
     */
    List<DrawPoint> drawCoordinateAxis(Canvas canvas, Path geoPath, Paint paint, DashRectangle dashRectangle, Path dashPath);
}
