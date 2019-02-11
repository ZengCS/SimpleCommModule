package com.zcs.android.lib.sketch.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by ZengCS on 2017/10/11.
 * E-mail:zcs@sxw.cn
 * Add:成都市天府软件园E3-3F
 */

public class CircleColorView extends View {
    private Paint mPaint;
    private int mColor = Color.GREEN;
    private float mRadius;

    private float mCenterX = 0;
    private float mCenterY = 0;

    public CircleColorView(Context context) {
        this(context, null);
    }

    public CircleColorView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleColorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint = new Paint();
        mPaint.setAntiAlias(true); //消除锯齿
        mPaint.setStyle(Paint.Style.FILL); //绘制空心圆
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();
        if (measuredWidth > measuredHeight)
            mRadius = measuredHeight / 2;
        else
            mRadius = measuredWidth / 2;

        mCenterX = measuredWidth / 2;
        mCenterY = measuredHeight / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setColor(mColor);

        canvas.drawCircle(mCenterX, mCenterY, mRadius, mPaint);
    }

    public void setColor(int mColor) {
        this.mColor = mColor;
        invalidate();
    }
}
