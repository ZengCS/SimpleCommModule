package com.zcs.android.lib.sketch.pen;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import com.zcs.android.lib.sketch.bean.DrawPoint;

/**
 * @author ZengCS
 * @version v1.0 create at 2017/10/17
 * @des 铅笔
 */
public class Pencil extends BasePenExtend {

    public Pencil(Context context) {
        super(context);
    }

    @Override
    protected void drawNeedToDo(Canvas canvas) {
        for (int i = 1; i < mHWPointList.size(); i++) {
            DrawPoint point = mHWPointList.get(i);
            drawToPoint(canvas, point, mPaint);
            mCurPoint = point;
        }
    }

    @Override
    protected void moveNeedToDo(double curDis) {
        int steps = 1 + (int) curDis / IPenConfig.STEPFACTOR;
        double step = 1.0 / steps;
        for (double t = 0; t < 1.0; t += step) {
            DrawPoint point = mBezier.getPoint(t);
            mHWPointList.add(point);
        }
    }

    @Override
    protected void doNeedToDo(Canvas canvas, DrawPoint point, Paint paint) {
        drawLine(canvas, mCurPoint.x, mCurPoint.y, mPaint.getStrokeWidth(), point.x,
                point.y, mPaint.getStrokeWidth(), paint);
    }

    private void drawLine(Canvas canvas, double x0, double y0, double w0, double x1, double y1, double w1, Paint paint) {
        //求两个数字的平方根 x的平方+y的平方在开方记得X的平方+y的平方=1，这就是一个园
        double curDis = Math.hypot(x0 - x1, y0 - y1);
        int steps = 1;
        if (paint.getStrokeWidth() < 6) {
            steps = 1 + (int) (curDis / 2);
        } else if (paint.getStrokeWidth() > 60) {
            steps = 1 + (int) (curDis / 4);
        } else {
            steps = 1 + (int) (curDis / 3);
        }
        double deltaX = (x1 - x0) / steps;
        double deltaY = (y1 - y0) / steps;
        double deltaW = (w1 - w0) / steps;
        double x = x0;
        double y = y0;
        double w = w0;

        for (int i = 0; i < steps; i++) {
            //都是用于表示坐标系中的一块矩形区域，并可以对其做一些简单操作
            //精度不一样。Rect是使用int类型作为数值，RectF是使用float类型作为数值。
            //            Rect rect = new Rect();
            RectF oval = new RectF();
            oval.set((float) (x - w / 4.0f), (float) (y - w / 2.0f), (float) (x + w / 4.0f), (float) (y + w / 2.0f));
            // oval.set((float)(x+w/4.0f), (float)(y+w/4.0f), (float)(x-w/4.0f), (float)(y-w/4.0f));
            //最基本的实现，通过点控制线，绘制椭圆
            canvas.drawOval(oval, paint);
            x += deltaX;
            y += deltaY;
            w += deltaW;
        }
    }
}
