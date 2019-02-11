package com.zcs.android.lib.sketch.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.view.View;

import com.zcs.android.lib.sketch.config.SketchConfig;

/**
 * Created by ZengCS on 2018/11/30.
 * E-mail:zcs@sxw.cn
 * Add:成都市天府软件园E3-3F
 */

public class SimpleSketchView extends View {
    private float mLastPencilX, mLastPencilY;

    private final Paint mBezierPaint = new Paint();
    private final Path mBezierPath = new Path();

    public SimpleSketchView(Context context) {
        super(context);
//        mBezierPaint.setAntiAlias(true);
//        mBezierPaint.setStrokeWidth(SketchConfig.getCurrLineWidth(context));
//        mBezierPaint.setStyle(Paint.Style.STROKE);
//        mBezierPaint.setStrokeCap(Paint.Cap.ROUND);
//        mBezierPaint.setStrokeJoin(Paint.Join.ROUND);
//        mBezierPaint.setColor(SketchConfig.currColor);

        mBezierPaint.setColor(SketchConfig.currColor);
        mBezierPaint.setStrokeWidth(SketchConfig.getCurrLineWidth(context));
        mBezierPaint.setStyle(Paint.Style.STROKE);
        mBezierPaint.setStrokeCap(Paint.Cap.ROUND);// 结束的笔画为圆心
        mBezierPaint.setStrokeJoin(Paint.Join.ROUND);// 连接处元
        mBezierPaint.setAlpha(0xFF);
        mBezierPaint.setAntiAlias(true);
        mBezierPaint.setStrokeMiter(1.0f);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                onPencilDown(event);
                break;
            case MotionEvent.ACTION_MOVE:
                onPencilMove(event);
        }
        //更新绘制
        invalidate();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 通过画布绘制多点形成的图形
        canvas.drawPath(mBezierPath, mBezierPaint);
    }

    //手指点下屏幕时调用
    private void onPencilDown(MotionEvent event) {
        //重置绘制路线，即隐藏之前绘制的轨迹
        mBezierPath.reset();
        float eventX = event.getX();
        float eventY = event.getY();

        mLastPencilX = eventX;
        mLastPencilY = eventY;
        //mPath绘制的绘制起点
        mBezierPath.moveTo(eventX, eventY);
    }

    // 手指在屏幕上滑动时调用
    private void onPencilMove(MotionEvent event) {
        final float eventX = event.getX();
        final float eventY = event.getY();

        final float previousX = mLastPencilX;
        final float previousY = mLastPencilY;

        final float dx = Math.abs(eventX - previousX);
        final float dy = Math.abs(eventY - previousY);

        //两点之间的距离大于等于3时，生成贝塞尔绘制曲线
        if (dx >= 3 || dy >= 3) {
            // 设置贝塞尔曲线的操作点为起点和终点的一半
            float controlX = (eventX + previousX) / 2;
            float controlY = (eventY + previousY) / 2;

            // 二次贝塞尔，实现平滑曲线；previousX, previousY为操作点，controlX, cY为终点
            mBezierPath.quadTo(previousX, previousY, controlX, controlY);

            // 第二次执行时，第一次结束调用的坐标值将作为第二次调用的初始坐标值
            mLastPencilX = eventX;
            mLastPencilY = eventY;
        }
    }
}
