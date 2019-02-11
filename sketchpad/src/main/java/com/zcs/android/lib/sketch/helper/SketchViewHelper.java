package com.zcs.android.lib.sketch.helper;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.RectF;

import com.zcs.android.lib.sketch.bean.DashRectangle;
import com.zcs.android.lib.sketch.bean.DrawPoint;
import com.zcs.android.lib.sketch.config.SketchConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * 画板助手
 * Created by ZengCS on 2017/12/15.
 * E-mail:zcs@sxw.cn
 * Add:成都市天府软件园E3-3F
 */
public class SketchViewHelper implements ISketchViewHelper {
    // 虚线边框属性设置
    private static final int DASH_LINE_COLOR = Color.parseColor("#BBBBBB");
    private static final int DASH_LINE_WIDTH = 3;
    public static final int BLOCK_WIDTH = 6;

    @Override
    public List<DrawPoint> drawRATriangle(Canvas canvas, Path geoPath, Paint paint, DashRectangle dashRectangle, Path dashPath) {
        float sx = dashRectangle.getStartX();
        float sy = dashRectangle.getStartY();
        float ex = dashRectangle.getEndX();
        float ey = dashRectangle.getEndY();

        List<DrawPoint> pointList = new ArrayList<>();
        pointList.add(new DrawPoint(sx, sy));
        pointList.add(new DrawPoint(sx, ey));
        pointList.add(new DrawPoint(ex, ey));

        // 第一步:画图形
        drawPath(canvas, geoPath, paint, pointList);
        // 第二步:画虚线
        if (dashPath != null)
            drawDashRectangle(canvas, dashPath, dashRectangle);

        return pointList;
    }

    @Override
    public List<DrawPoint> drawTriangle(Canvas canvas, Path geoPath, Paint paint, DashRectangle dashRectangle, Path dashPath) {
        float sx = dashRectangle.getStartX();
        float sy = dashRectangle.getStartY();
        float ex = dashRectangle.getEndX();
        float ey = dashRectangle.getEndY();

        List<DrawPoint> pointList = new ArrayList<>();
        pointList.add(new DrawPoint((sx + ex) / 2, sy));
        pointList.add(new DrawPoint(sx, ey));
        pointList.add(new DrawPoint(ex, ey));

        // 第一步:画图形
        drawPath(canvas, geoPath, paint, pointList);
        // 第二步:画虚线
        if (dashPath != null)
            drawDashRectangle(canvas, dashPath, dashRectangle);

        return pointList;
    }

    @Override
    public List<DrawPoint> drawQuadLine(Canvas canvas, Path geoPath, Paint paint, DashRectangle dashRectangle, Path dashPath) {
        float sx = dashRectangle.getStartX();
        float sy = dashRectangle.getStartY();
        float ex = dashRectangle.getEndX();
        float ey = dashRectangle.getEndY();

        float centerX = (sx + ex) / 2;
        float centerY = (sy + ey) / 2;
        float leftHalfPointX = (sx + centerX) / 2;// 左边中点X坐标
        float leftHalfPointY = (sy + centerY) / 2;// 左边中点Y坐标
        float rightHalfPointX = (centerX + ex) / 2;// 右边中点X坐标
        float rightHalfPointY = (centerY + ey) / 2;// 右边中点Y坐标

        // 第一步:得到两个辅助点
        DrawPoint quadPoint1 = getQuadPoint(sx, sy, leftHalfPointX, leftHalfPointY, 0);
        DrawPoint quadPoint2 = getQuadPoint(centerX, centerY, rightHalfPointX, rightHalfPointY, 1);

        List<DrawPoint> pointList = new ArrayList<>();
        if (quadPoint1 != null && quadPoint2 != null) {
            pointList.add(new DrawPoint(sx, sy));
            pointList.add(new DrawPoint(quadPoint1.getX(), quadPoint1.getY()));
            pointList.add(new DrawPoint(centerX, centerY));
            pointList.add(new DrawPoint(quadPoint2.getX(), quadPoint2.getY()));
            pointList.add(new DrawPoint(ex, ey));
        }

        if (pointList.size() == 5) {
            DrawPoint startPoint = pointList.get(0);
            DrawPoint qp1 = pointList.get(1);
            DrawPoint centerPoint = pointList.get(2);
            DrawPoint qp2 = pointList.get(3);
            DrawPoint endPoint = pointList.get(4);

            geoPath.moveTo(startPoint.getX(), startPoint.getY());
            geoPath.quadTo(qp1.getX(), qp1.getY(), centerPoint.getX(), centerPoint.getY());
            geoPath.quadTo(qp2.getX(), qp2.getY(), endPoint.getX(), endPoint.getY());
            canvas.drawPath(geoPath, paint);
        }
        DashRectangle tempRect = dashRectangle.cloneRect();
        if (quadPoint1 != null && quadPoint2 != null) {
            float minQuadX = Math.min(quadPoint1.getX(), quadPoint2.getX());
            float minQuadY = Math.min(quadPoint1.getY(), quadPoint2.getY());
            float maxQuadX = Math.max(quadPoint1.getX(), quadPoint2.getX());
            float maxQuadY = Math.max(quadPoint1.getY(), quadPoint2.getY());

//            tempRect.update(
//                    Math.min(dashRectangle.getMinX(), minQuadX),
//                    Math.min(dashRectangle.getMinY(), minQuadY),
//                    Math.max(dashRectangle.getMaxX(), maxQuadX),
//                    Math.max(dashRectangle.getMaxY(), maxQuadY)
//            );
        }
        // 第二步:画虚线
        if (dashPath != null)
            drawDashRectangle(canvas, dashPath, tempRect);

        return pointList;
    }

    @Override
    public List<DrawPoint> drawLine(Canvas canvas, Path geoPath, Paint paint, DashRectangle dashRectangle, Path dashPath) {
        float sx = dashRectangle.getStartX();
        float sy = dashRectangle.getStartY();
        float ex = dashRectangle.getEndX();
        float ey = dashRectangle.getEndY();

        List<DrawPoint> pointList = new ArrayList<>();
        pointList.add(new DrawPoint(sx, sy));
        pointList.add(new DrawPoint(ex, ey));

        // 第一步:画图形
        drawPath(canvas, geoPath, paint, pointList);
        // 第二步:画虚线
        if (dashPath != null)
            drawDashRectangle(canvas, dashPath, dashRectangle);

        return pointList;
    }

    @Override
    public List<DrawPoint> drawPentagon(Canvas canvas, Path geoPath, Paint paint, DashRectangle dashRectangle, Path dashPath) {
        float f1 = 0.809017f;
        float f2 = 0.309017f;
        float f3 = 0.587785f;
        float f4 = 0.951057f;

        float sx = dashRectangle.getStartX();
        float sy = dashRectangle.getStartY();
        float ex = dashRectangle.getEndX();
        float ey = dashRectangle.getEndY();

        float centerX = (ex + sx) / 2;

        // 缓存五边形
        List<DrawPoint> pointList = new ArrayList<>();
        pointList.add(new DrawPoint(centerX, sy));
        pointList.add(new DrawPoint(ex, sy + (ey - sy) * f3 / (f3 + f4)));
        pointList.add(new DrawPoint(ex - (ex - sx) * f2 / (f1 * 2), ey));
        pointList.add(new DrawPoint(sx + (ex - sx) * f2 / (f1 * 2), ey));
        pointList.add(new DrawPoint(sx, sy + (ey - sy) * f3 / (f3 + f4)));

        // 第一步:画图形
        drawPath(canvas, geoPath, paint, pointList);
        // 第二步:画虚线
        if (dashPath != null)
            drawDashRectangle(canvas, dashPath, dashRectangle);

        return pointList;
    }

    @Override
    public List<DrawPoint> drawDiamond(Canvas canvas, Path geoPath, Paint paint, DashRectangle dashRectangle, Path dashPath) {
        float sx = dashRectangle.getStartX();
        float sy = dashRectangle.getStartY();
        float ex = dashRectangle.getEndX();
        float ey = dashRectangle.getEndY();

        float centerX = (ex + sx) / 2;
        float centerY = (ey + sy) / 2;

        List<DrawPoint> pointList = new ArrayList<>();
        pointList.add(new DrawPoint(centerX, sy));
        pointList.add(new DrawPoint(ex, centerY));
        pointList.add(new DrawPoint(centerX, ey));
        pointList.add(new DrawPoint(sx, centerY));

        // 第一步:画图形
        drawPath(canvas, geoPath, paint, pointList);
        // 第二步:画虚线
        if (dashPath != null)
            drawDashRectangle(canvas, dashPath, dashRectangle);

        return pointList;
    }

    @Override
    public List<DrawPoint> drawEchelon(Canvas canvas, Path geoPath, Paint paint, DashRectangle dashRectangle, Path dashPath) {
        float sx = dashRectangle.getStartX();
        float sy = dashRectangle.getStartY();
        float ex = dashRectangle.getEndX();
        float ey = dashRectangle.getEndY();
        float offset = (ex - sx) / 5;

        List<DrawPoint> pointList = new ArrayList<>();
        pointList.add(new DrawPoint(sx + offset, sy));
        pointList.add(new DrawPoint(ex - offset, sy));
        pointList.add(new DrawPoint(ex, ey));
        pointList.add(new DrawPoint(sx, ey));

        // 第一步:画图形
        drawPath(canvas, geoPath, paint, pointList);
        // 第二步:画虚线
        if (dashPath != null)
            drawDashRectangle(canvas, dashPath, dashRectangle);

        return pointList;
    }

    @Override
    public RectF drawCircle(Canvas canvas, Paint paint, DashRectangle dashRectangle, Path dashPath) {
        RectF rectF = new RectF(dashRectangle.getStartX(), dashRectangle.getStartY(),
                dashRectangle.getEndX(), dashRectangle.getEndY());
        // 第一步:画矩形
        canvas.drawOval(rectF, paint);
        // 第二步:画虚线
        if (dashPath != null)
            drawDashRectangle(canvas, dashPath, dashRectangle);

        return rectF;
    }

    @Override
    public RectF drawRectangle(Canvas canvas, Paint paint, DashRectangle dashRectangle, Path dashPath) {
        RectF rectF = new RectF(dashRectangle.getStartX(), dashRectangle.getStartY(),
                dashRectangle.getEndX(), dashRectangle.getEndY());
        // 第一步:画矩形
        canvas.drawRect(rectF, paint);
        // 第二步:画虚线
        if (dashPath != null)
            drawDashRectangle(canvas, dashPath, dashRectangle);

        return rectF;
    }

    /**
     * 画平行四边形
     */
    @Override
    public List<DrawPoint> drawParallelogram(Canvas canvas, Path geoPath, Paint paint, DashRectangle dashRectangle, Path dashPath) {
        float sx = dashRectangle.getStartX();
        float sy = dashRectangle.getStartY();
        float ex = dashRectangle.getEndX();
        float ey = dashRectangle.getEndY();

        float offset = (ex - sx) / 3;
        List<DrawPoint> pointList = new ArrayList<>();
        pointList.add(new DrawPoint(sx + offset, sy));
        pointList.add(new DrawPoint(ex, sy));
        pointList.add(new DrawPoint(ex - offset, ey));
        pointList.add(new DrawPoint(sx, ey));

        // 第一步:画图形
        drawPath(canvas, geoPath, paint, pointList);
        // 第二步:画虚线
        if (dashPath != null)
            drawDashRectangle(canvas, dashPath, dashRectangle);

        return pointList;
    }

    /**
     * 画图形
     *
     * @param canvas    画布
     * @param path      路径对象
     * @param paint     画笔
     * @param pointList 点位列表
     */
    private void drawPath(Canvas canvas, Path path, Paint paint, List<DrawPoint> pointList) {
        int c = 0;
        path.reset();
        for (DrawPoint point : pointList) {
            if (c == 0) {
                path.moveTo(point.getX(), point.getY());
            } else {
                path.lineTo(point.getX(), point.getY());
            }
            c++;
        }
        if (pointList.size() > 2)
            path.close();
        canvas.drawPath(path, paint);
    }

    /**
     * 画矩形虚线框
     */
    private void drawDashRectangle(Canvas canvas, Path path, DashRectangle dashRectangle) {
        // 虚线向外扩，保证虚线与图形不重合
        float expand = SketchConfig.GEOMETRIC_SIZE / 2 + DASH_LINE_WIDTH + BLOCK_WIDTH;
        expand = 0;
        float sx = dashRectangle.getMinX() - expand;
        float sy = dashRectangle.getMinY() - expand;
        float ex = dashRectangle.getMaxX() + expand;
        float ey = dashRectangle.getMaxY() + expand;
        DashRectangle tempRectangle = new DashRectangle();
        tempRectangle.update(sx, sy, ex, ey);

        // 初始化方块画笔
        Paint blockPaint = new Paint();
        blockPaint.setAntiAlias(true);
        blockPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        blockPaint.setColor(DASH_LINE_COLOR);
        // 画方块
        canvas.drawRect(tempRectangle.getTopRectF(BLOCK_WIDTH), blockPaint);
        canvas.drawRect(tempRectangle.getLeftRectF(BLOCK_WIDTH), blockPaint);
        canvas.drawRect(tempRectangle.getRightRectF(BLOCK_WIDTH), blockPaint);
        canvas.drawRect(tempRectangle.getBottomRectF(BLOCK_WIDTH), blockPaint);

        path.moveTo(sx, sy);
        path.lineTo(sx, ey);
        path.lineTo(ex, ey);
        path.lineTo(ex, sy);
        path.close();

        PathEffect effects = new DashPathEffect(new float[]{9, 6, 9, 6}, 0);

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(DASH_LINE_COLOR);
        paint.setStrokeWidth(DASH_LINE_WIDTH);
        paint.setPathEffect(effects);

        canvas.drawPath(path, paint);
    }

    private DrawPoint getQuadPoint(float x1, float y1, float x, float y, int index) {
        // index must be 0 or 1
        if (index > 1 || index < 0) return null;

        float distanceX = Math.abs(x1 - x);
        float distanceY = Math.abs(y1 - y);
        float hh = distanceX * distanceX + distanceY * distanceY;
        float h = (float) Math.sqrt(hh);
        float l = (float) (2 * h / Math.sqrt(3));

        float a = l / 2;

        float tx, ty;
        if (index == 0) {
            if (x > x1) {
                tx = (float) (x + a * Math.sin(Math.atan((y1 - y) / (x1 - x))));
                ty = (float) (y - a * Math.cos(Math.atan((y1 - y) / (x1 - x))));
            } else {
                tx = (float) (x - a * Math.sin(Math.atan((y1 - y) / (x1 - x))));
                ty = (float) (y + a * Math.cos(Math.atan((y1 - y) / (x1 - x))));
            }
        } else {
            if (x > x1) {
                tx = (float) (x - a * Math.sin(Math.atan((y1 - y) / (x1 - x))));
                ty = (float) (y + a * Math.cos(Math.atan((y1 - y) / (x1 - x))));
            } else {
                tx = (float) (x + a * Math.sin(Math.atan((y1 - y) / (x1 - x))));
                ty = (float) (y - a * Math.cos(Math.atan((y1 - y) / (x1 - x))));
            }
        }
        return new DrawPoint(tx, ty);
    }

    @Override
    public List<DrawPoint> drawCoordinateAxis(Canvas canvas, Path geoPath, Paint paint, DashRectangle dashRectangle, Path dashPath) {
        float sx = dashRectangle.getStartX();
        float sy = dashRectangle.getStartY();
        float ex = dashRectangle.getEndX();
        float ey = dashRectangle.getEndY();

        float minX = Math.min(sx, ex);
        float minY = Math.min(sy, ey);
        float maxX = Math.max(sx, ex);
        float maxY = Math.max(sy, ey);

        paint.setStyle(Paint.Style.FILL_AND_STROKE); // 绘制实心

        int arrowHeight = 9;
        int arrowLength = 2 * arrowHeight;

        float w = maxX - minX;// x轴位移距离
        float h = maxY - minY;// y轴位移距离

        float gap = 36;
        float originX = minX + gap;
        float originY = maxY - gap;

        List<DrawPoint> pointList = new ArrayList<>();
        if (w > gap * 2 && h > gap * 2) {
            // 计算坐标轴各个点位坐标
            pointList.add(new DrawPoint(originX, h + minY));// 1
            pointList.add(new DrawPoint(originX, arrowLength + minY));// 2
            pointList.add(new DrawPoint(originX - arrowHeight, arrowLength + minY));// 3
            pointList.add(new DrawPoint(originX, minY));// 4 顶点
            pointList.add(new DrawPoint(originX + arrowHeight, arrowLength + minY));// 5
            pointList.add(new DrawPoint(originX, arrowLength + minY));// 2
            pointList.add(new DrawPoint(originX, originY));// 原点
            pointList.add(new DrawPoint(minX, originY));// 6
            pointList.add(new DrawPoint(minX + w - arrowLength, originY));// 7
            pointList.add(new DrawPoint(minX + w - arrowLength, originY - arrowHeight));// 8
            pointList.add(new DrawPoint(minX + w, originY));// 9 顶点
            pointList.add(new DrawPoint(minX + w - arrowLength, originY + arrowHeight));// 10
            pointList.add(new DrawPoint(minX + w - arrowLength, originY));// 7
            pointList.add(new DrawPoint(originX, originY));// 原点

            // 第一步:画图形
            drawPath(canvas, geoPath, paint, pointList);
        }/* else {
            TextPaint mTextPaint = new TextPaint();
            mTextPaint.setAntiAlias(true);
            mTextPaint.setStyle(Paint.Style.FILL);
            mTextPaint.setColor(SketchConfig.currColor);
            mTextPaint.setTextSize(24);
            canvas.drawText("请再拖大一点", ex, ey, mTextPaint);
        }*/
        // 第二步:画虚线
        if (dashPath != null)
            drawDashRectangle(canvas, dashPath, dashRectangle);

        // 恢复空心
        paint.setStyle(Paint.Style.STROKE);
        return pointList;
    }

    @Override
    public List<DrawPoint> drawLineWithArrow(Canvas canvas, Path geoPath, Paint paint, DashRectangle dashRectangle, Path dashPath) {
        float sx = dashRectangle.getStartX();
        float sy = dashRectangle.getStartY();
        float ex = dashRectangle.getEndX();
        float ey = dashRectangle.getEndY();

        paint.setStyle(Paint.Style.FILL_AND_STROKE); // 绘制实心

        int arrowHeight = 9;
        int arrowWidth = 2 * arrowHeight;

        float disX = ex - sx;// x轴位移距离
        float disY = ey - sy;// y轴位移距离
        double r = Math.sqrt(disX * disX + disY * disY);// 斜边长

        float zx = (float) (ex - (arrowWidth * disX / r));
        float zy = (float) (ey - (arrowWidth * disY / r));
        float xz = zx - sx;
        float yz = zy - sy;
        double zd = xz * xz + yz * yz;
        double zr = Math.sqrt(zd);
        List<DrawPoint> pointList = new ArrayList<>();
        pointList.add(new DrawPoint(sx, sy));
        pointList.add(new DrawPoint(zx, zy));
        pointList.add(new DrawPoint((float) (zx + arrowHeight * yz / zr), (float) (zy - arrowHeight * xz / zr)));
        pointList.add(new DrawPoint(ex, ey));
        pointList.add(new DrawPoint((float) (zx - arrowHeight * yz / zr), (float) (zy + arrowHeight * xz / zr)));
        pointList.add(new DrawPoint(zx, zy));

        // 第一步:画图形
        drawPath(canvas, geoPath, paint, pointList);
        // 第二步:画虚线
        if (dashPath != null)
            drawDashRectangle(canvas, dashPath, dashRectangle);

        // 恢复空心
        paint.setStyle(Paint.Style.STROKE);
        return pointList;
    }

    @Override
    public List<DrawPoint> drawRegularTriangle(Canvas canvas, Path geoPath, Paint paint, DashRectangle dashRectangle, Path dashPath) {
        float sx = dashRectangle.getStartX();
        float sy = dashRectangle.getStartY();
        float ex = dashRectangle.getEndX();
        float ey = dashRectangle.getEndY();

        float distanceX = Math.abs(sx - ex);
        float distanceY = Math.abs(sy - ey);
        float hh = distanceX * distanceX + distanceY * distanceY;
        float h = (float) Math.sqrt(hh);
        float l = (float) (2 * h / Math.sqrt(3));

        float a = l / 2;

        // 计算第二个点的坐标
        float x2 = (float) (ex - a * Math.sin(Math.atan((sy - ey) / (sx - ex))));
        float y2 = (float) (ey + a * Math.cos(Math.atan((sy - ey) / (sx - ex))));

        // 计算第三个点的坐标
        float x3 = (float) (ex + a * Math.sin(Math.atan((sy - ey) / (sx - ex))));
        float y3 = (float) (ey - a * Math.cos(Math.atan((sy - ey) / (sx - ex))));

        List<DrawPoint> pointList = new ArrayList<>();
        pointList.add(new DrawPoint(sx, sy));
        pointList.add(new DrawPoint(x2, y2));
        pointList.add(new DrawPoint(x3, y3));

        // 第一步:画图形
        drawPath(canvas, geoPath, paint, pointList);
        // 第二步:画虚线
        if (dashPath != null)
            drawDashRectangle(canvas, dashPath, dashRectangle);

        return pointList;
    }
}
