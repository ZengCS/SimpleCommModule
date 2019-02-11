package com.zcs.android.lib.sketch.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.zcs.android.lib.sketch.R;
import com.zcs.android.lib.sketch.bean.BasePoint;
import com.zcs.android.lib.sketch.config.SketchConfig;
import com.zcs.android.lib.sketch.utils.SketchMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZengCS on 2017/10/11.
 * E-mail:zcs@sxw.cn
 * Add:成都市天府软件园E3-3F
 */
public class GeometricView extends View {
    private static final String TAG = "GeometricView";
    private static final int CUSTOM_PADDING = 42;

    // 参数
    private int mCurrGeometric = SketchMode.Geometric.RECTANGLE;
    private boolean mLightMode = false;
    private int mLineSize = SketchConfig.GEOMETRIC_SIZE;// 5

    private Paint mGeometricPaint;
    private Path mPath;
    private float mRadius;
    private float mCenterX = 0;
    private float mCenterY = 0;

    public GeometricView(Context context) {
        this(context, null);
    }

    public GeometricView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GeometricView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.GeometricView,
                0, 0);

        //Configurable parameters
        try {
            mCurrGeometric = a.getInteger(R.styleable.GeometricView_geometric, SketchMode.Geometric.RECTANGLE);
        } finally {
            a.recycle();
        }

        init();
    }

    /**
     * 设置图形类型
     */
    public void setGeometricType(int mCurrGeometric) {
        this.mCurrGeometric = mCurrGeometric;
        if (mPath != null)
            mPath.reset();
        invalidate();
    }

    /**
     * 初始化画笔和路径对象
     */
    private void init() {
        mPath = new Path();

        mGeometricPaint = new Paint();
        mGeometricPaint.setAntiAlias(true); //消除锯齿
        mGeometricPaint.setStyle(Paint.Style.STROKE); //绘制空心圆
        mGeometricPaint.setStrokeWidth(mLineSize);
        mGeometricPaint.setStrokeJoin(Paint.Join.MITER);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        float mWidth = getMeasuredWidth();
        float mHeight = getMeasuredHeight();
        if (mWidth > mHeight)
            mRadius = mHeight;
        else
            mRadius = mWidth;

        mRadius = (mRadius - mLineSize - CUSTOM_PADDING) / 2;

        mCenterX = mWidth / 2;
        mCenterY = mHeight / 2;
        Log.d(TAG, "onMeasure: -------------------------");
        Log.d(TAG, "onMeasure: mWidth = " + mWidth);
        Log.d(TAG, "onMeasure: mHeight = " + mHeight);
        Log.d(TAG, "onMeasure: mRadius = " + mRadius);
        Log.d(TAG, "onMeasure: mCenterX = " + mCenterX);
        Log.d(TAG, "onMeasure: mCenterY = " + mCenterY);
        Log.d(TAG, "onMeasure: -------------------------");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mLightMode) {
            mGeometricPaint.setColor(SketchConfig.currColor);
        } else {
            mGeometricPaint.setColor(SketchConfig.darkColor);
        }
        switch (mCurrGeometric) {
            case SketchMode.Geometric.RECTANGLE:// 画矩形
                float left = mCenterX - mRadius;
                float top = mCenterY - mRadius;
                float right = mCenterX + mRadius;
                float bottom = mCenterY + mRadius;

                canvas.drawRect(left, top, right, bottom, mGeometricPaint);
                break;
            case SketchMode.Geometric.CIRCLE:// 画圆
                canvas.drawCircle(mCenterX, mCenterY, mRadius, mGeometricPaint);
                break;
            case SketchMode.Geometric.TRIANGLE:// 画三角形
                drawTriangle(canvas);
                break;
            case SketchMode.Geometric.RA_TRIANGLE:// 画直角三角形
                drawRATriangle(canvas);
                break;
            case SketchMode.Geometric.LINE:// 画直线
                canvas.drawLine(mCenterX - mRadius, mCenterY, mCenterX + mRadius, mCenterY, mGeometricPaint);
                break;
            case SketchMode.Geometric.QUAD:// 波浪曲线
                drawQuad(canvas);
                break;
            case SketchMode.Geometric.PARALLELOGRAM:// 平行四边形
                drawParallelogram(canvas);
                break;
            case SketchMode.Geometric.ECHELON:// 梯形
                drawEchelon(canvas);
                break;
            case SketchMode.Geometric.DIAMOND:// 菱形
                drawDiamond(canvas);
                break;
            case SketchMode.Geometric.PENTAGON:// 五边形
                drawPentagon(canvas);
                break;
            case SketchMode.Geometric.LINE_WITH_ARROW:// 直线箭头
                drawLineWithArrow(canvas);
                break;
            case SketchMode.Geometric.COORDINATE_AXIS:// 坐标轴
                drawCoordinateAxis(canvas);
                break;
        }
    }

    /**
     * 画坐标轴
     */
    private void drawCoordinateAxis(Canvas canvas) {
        int arrowHeight = 3;
        int arrowLength = 6;
        mGeometricPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mGeometricPaint.setStrokeWidth(3);

        float gap = mRadius;
        float w = mCenterX * 2;
        float h = mCenterY * 2;

        List<BasePoint> pointList = new ArrayList<>();

        // 计算坐标轴各个点位坐标
        pointList.add(new BasePoint(gap, h - gap / 2));// 1
//        pointList.add(new BasePoint(gap, h - gap));// 原点
        pointList.add(new BasePoint(gap, arrowLength + gap));// 2
        pointList.add(new BasePoint(gap - arrowHeight, arrowLength + gap));// 3
        pointList.add(new BasePoint(gap, gap));// 4
        pointList.add(new BasePoint(gap + arrowHeight, arrowLength + gap));// 5
        pointList.add(new BasePoint(gap, arrowLength + gap));// 2
        pointList.add(new BasePoint(gap, h - gap));// 原点
        pointList.add(new BasePoint(gap / 2, h - gap));// 6
        pointList.add(new BasePoint(w - arrowLength - gap, h - gap));// 7
        pointList.add(new BasePoint(w - arrowLength - gap, h - gap - arrowHeight));// 8
        pointList.add(new BasePoint(w - gap, h - gap));// 9
        pointList.add(new BasePoint(w - arrowLength - gap, h - gap + arrowHeight));// 10
        pointList.add(new BasePoint(w - arrowLength - gap, h - gap));// 7
        pointList.add(new BasePoint(gap, h - gap));// 原点
        drawPath(canvas, pointList);

        // 恢复空心
        mGeometricPaint.setStyle(Paint.Style.STROKE);
        mGeometricPaint.setStrokeWidth(SketchConfig.GEOMETRIC_SIZE);
    }

    /**
     * 画直线箭头
     */
    private void drawLineWithArrow(Canvas canvas) {
        int arrowHeight = 3;
        int arrowWidth = 6;
        mGeometricPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        List<BasePoint> pointList = new ArrayList<>();
        pointList.add(new BasePoint(mCenterX - mRadius, mCenterY));
        pointList.add(new BasePoint(mCenterX + mRadius - arrowWidth, mCenterY));
        pointList.add(new BasePoint(mCenterX + mRadius - arrowWidth, mCenterY - arrowHeight));
        pointList.add(new BasePoint(mCenterX + mRadius, mCenterY));
        pointList.add(new BasePoint(mCenterX + mRadius - arrowWidth, mCenterY + arrowHeight));
        pointList.add(new BasePoint(mCenterX + mRadius - arrowWidth, mCenterY));
        drawPath(canvas, pointList);

        // 恢复空心
        mGeometricPaint.setStyle(Paint.Style.STROKE);
    }

    /**
     * 画五边形
     */
    private void drawPentagon(Canvas canvas) {
        float f1 = 0.809017f;
        float f2 = 0.309017f;
        float f3 = 0.587785f;
        float f4 = 0.951057f;

        float minX = mCenterX - mRadius;
        float minY = mCenterY - mRadius;
        float maxX = mCenterX + mRadius;
        float maxY = mCenterY + mRadius;

        List<BasePoint> pointList = new ArrayList<>();
        pointList.add(new BasePoint(mCenterX, minY));// 中上
        pointList.add(new BasePoint(maxX, minY + (maxY - minY) * f3 / (f3 + f4)));// 右上
        pointList.add(new BasePoint(maxX - (maxX - minX) * f2 / (f1 * 2), maxY));// 右下
        pointList.add(new BasePoint(minX + (maxX - minX) * f2 / (f1 * 2), maxY));// 左下
        pointList.add(new BasePoint(minX, minY + (maxY - minY) * f3 / (f3 + f4)));// 左上
        drawPath(canvas, pointList);
    }


    /**
     * 画菱形
     */
    private void drawDiamond(Canvas canvas) {
        float offset = mRadius / 1.4f;
        List<BasePoint> pointList = new ArrayList<>();
        pointList.add(new BasePoint(mCenterX, mCenterY - mRadius));// 上
        pointList.add(new BasePoint(mCenterX + offset, mCenterY));// 右
        pointList.add(new BasePoint(mCenterX, mCenterY + mRadius));// 下
        pointList.add(new BasePoint(mCenterX - offset, mCenterY));// 左
        drawPath(canvas, pointList);
    }

    /**
     * 画梯形
     */
    private void drawEchelon(Canvas canvas) {
        int offset = (int) (mRadius / 1.6f);
        List<BasePoint> pointList = new ArrayList<>();
        pointList.add(new BasePoint(mCenterX - offset, mCenterY - offset));// 左上点
        pointList.add(new BasePoint(mCenterX + offset, mCenterY - offset));// 右上点
        pointList.add(new BasePoint(mCenterX + offset * 2, mCenterY + offset));// 右下点
        pointList.add(new BasePoint(mCenterX - offset * 2, mCenterY + offset));// 左下点
        drawPath(canvas, pointList);
    }

    /**
     * 画直角三角形
     */
    private void drawRATriangle(Canvas canvas) {
        int offset = (int) (mRadius / 3);

        float l = 2 * mRadius;
        // 正三角的高 = 边长/2 * 根号3;
        float h = (float) (l / 2 * Math.sqrt(3));

        float x1 = mCenterX - l / 2 + offset;
        float y1 = mCenterY - h / 2;

        float y2 = y1 + h;
        float x3 = x1 + l - offset * 2;

        List<BasePoint> pointList = new ArrayList<>();
        pointList.add(new BasePoint(x1, y1));
        pointList.add(new BasePoint(x1, y2));
        pointList.add(new BasePoint(x3, y2));
        drawPath(canvas, pointList);
    }

    /**
     * 画正三角形
     */
    private void drawTriangle(Canvas canvas) {
        float l = 2 * mRadius;
        // 正三角的高 = 边长/2 * 根号3;
        float h = (float) (l / 2 * Math.sqrt(3));

        float x1 = mCenterX;
        float y1 = mCenterY - h / 2;

        float x2 = mCenterX - l / 2;
        float y2 = y1 + h;

        float x3 = x2 + l;

        List<BasePoint> pointList = new ArrayList<>();
        pointList.add(new BasePoint(x1, y1));
        pointList.add(new BasePoint(x2, y2));
        pointList.add(new BasePoint(x3, y2));
        drawPath(canvas, pointList);
    }

    /**
     * 画波浪曲线
     */
    private void drawQuad(Canvas canvas) {
        float startX = mCenterX - mRadius;
        float endX = mCenterX + mRadius;
        mPath.moveTo(startX, mCenterY);
        mPath.quadTo((startX + mCenterX) / 2, mCenterY - 10, mCenterX, mCenterY);
        mPath.quadTo((endX + mCenterX) / 2, mCenterY + 10, endX, mCenterY);
        canvas.drawPath(mPath, mGeometricPaint);
    }

    /**
     * 画平行四边形
     */
    private void drawParallelogram(Canvas canvas) {
        int offset = (int) (mRadius / 1.6f);
        List<BasePoint> pointList = new ArrayList<>();
        pointList.add(new BasePoint(mCenterX - offset, mCenterY - offset));// 左上点
        pointList.add(new BasePoint(mCenterX + offset * 2, mCenterY - offset));// 右上点
        pointList.add(new BasePoint(mCenterX + offset, mCenterY + offset));// 右下点
        pointList.add(new BasePoint(mCenterX - offset * 2, mCenterY + offset));// 左下点
        drawPath(canvas, pointList);
    }

    /**
     * 画路径
     *
     * @param canvas 画布
     * @param points 点列表
     */
    private void drawPath(Canvas canvas, List<BasePoint> points) {
        int c = 0;
        for (BasePoint point : points) {
            if (c == 0)
                mPath.moveTo(point.getX(), point.getY());
            else
                mPath.lineTo(point.getX(), point.getY());
            c++;
        }
        mPath.close();
        canvas.drawPath(mPath, mGeometricPaint);
    }

    /**
     * 设置高亮模式
     */
    public void setLightMode(boolean mLightMode) {
        this.mLightMode = mLightMode;
        if (mPath != null)
            mPath.reset();
        invalidate();
    }
}
