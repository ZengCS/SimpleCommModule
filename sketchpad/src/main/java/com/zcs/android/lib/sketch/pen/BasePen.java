package com.zcs.android.lib.sketch.pen;

import android.graphics.Canvas;
import android.view.MotionEvent;

import com.zcs.android.lib.sketch.base.ISketchDrawCallback;

/**
 * 处理draw和touch事件的基类
 */
public abstract class BasePen {
    public static final String TAG = "BasePen";
    protected ISketchDrawCallback mSketchDrawCallback;

    public void setSketchDrawCallback(ISketchDrawCallback mSketchDrawCallback) {
        this.mSketchDrawCallback = mSketchDrawCallback;
    }

    /**
     * 绘制
     *
     * @param canvas
     */
    public abstract void draw(Canvas canvas);

    /**
     * 接受并处理onTouchEvent
     *
     * @param event
     * @return
     */
    public boolean onTouchEvent(MotionEvent event, Canvas canvas) {
        return false;
    }
}
