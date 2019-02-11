package com.zcs.android.lib.sketch.bean;

import android.graphics.RectF;
import android.util.Log;

import com.zcs.android.lib.sketch.helper.SketchViewHelper;

/**
 * Created by ZengCS on 2017/12/14.
 * E-mail:zcs@sxw.cn
 * Add:成都市天府软件园E3-3F
 */

public class DashRectangle extends BaseBean {
    public static class Direction {
        public static final int OUTSIDE = -1;
        public static final int LEFT = 0;
        public static final int TOP = 1;
        public static final int RIGHT = 2;
        public static final int BOTTOM = 3;
    }

    public static final int RANGE_OFFSET = SketchViewHelper.BLOCK_WIDTH * 2;// 给X个像素作为误差值

    private float startX;
    private float startY;
    private float endX;
    private float endY;

    public void update(float startX, float startY, float endX, float endY) {
//        this.startX = Math.min(startX, endX);
//        this.startY = Math.min(startY, endY);
//        this.endX = Math.max(startX, endX);
//        this.endY = Math.max(startY, endY);
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
    }

    public void move(float disX, float disY) {
        this.startX += disX;
        this.startY += disY;
        this.endX += disX;
        this.endY += disY;
    }

    /**
     * 判断当前点是否在虚线框范围内
     */
    public boolean isContain(float x, float y) {
        return x - RANGE_OFFSET > getMinX()
                && x + RANGE_OFFSET < getMaxX()
                && y - RANGE_OFFSET > getMinY()
                && y + RANGE_OFFSET < getMaxY();
    }

    public DashRectangle cloneRect() {
        DashRectangle temp = new DashRectangle();
        temp.update(startX, startY, endX, endY);
        return temp;
    }

    public void setMinX(float x) {
        if (startX < endX)
            this.startX = x;
        else
            this.endX = x;
    }

    public void setMinY(float y) {
        if (startY < endY)
            this.startY = y;
        else
            this.endY = y;
    }

    public void setMaxX(float x) {
        if (startX > endX)
            this.startX = x;
        else
            this.endX = x;
    }

    public void setMaxY(float y) {
        if (startY > endY)
            this.startY = y;
        else
            this.endY = y;
    }

    public float getMinX() {
        return Math.min(startX, endX);
    }

    public float getMinY() {
        return Math.min(startY, endY);
    }

    public float getMaxX() {
        return Math.max(startX, endX);
    }

    public float getMaxY() {
        return Math.max(startY, endY);
    }

    public float getStartX() {
        return startX;
    }

    public float getStartY() {
        return startY;
    }

    public float getEndX() {
        return endX;
    }

    public float getEndY() {
        return endY;
    }

    public void setStartX(float startX) {
        this.startX = startX;
    }

    public void setStartY(float startY) {
        this.startY = startY;
    }

    public void setEndX(float endX) {
        this.endX = endX;
    }

    public void setEndY(float endY) {
        this.endY = endY;
    }

    public void reset() {
        this.startX = 0;
        this.startY = 0;
        this.endX = 0;
        this.endY = 0;
    }

    public RectF getTopRectF(int blockWidth) {
        float topCenterX = (startX + endX) / 2;
        return new RectF(
                topCenterX - blockWidth,
                startY - blockWidth,
                topCenterX + blockWidth,
                startY + blockWidth
        );
    }

    public RectF getLeftRectF(int blockWidth) {
        float leftCenterY = (startY + endY) / 2;
        return new RectF(startX - blockWidth,
                leftCenterY - blockWidth,
                startX + blockWidth,
                leftCenterY + blockWidth
        );
    }

    public RectF getRightRectF(int blockWidth) {
        float rightCenterY = (startY + endY) / 2;
        return new RectF(endX - blockWidth,
                rightCenterY - blockWidth,
                endX + blockWidth,
                rightCenterY + blockWidth
        );
    }

    public RectF getBottomRectF(int blockWidth) {
        float bottomCenterX = (startX + endX) / 2;
        return new RectF(
                bottomCenterX - blockWidth,
                endY - blockWidth,
                bottomCenterX + blockWidth,
                endY + blockWidth
        );
    }

    /**
     * 获取当前resize的方向
     */
    public int getDirection(float x, float y) {
        if (x > getMinX() && x < getMaxX()) {
            if (y > getMinY() - RANGE_OFFSET && y < getMinY() + RANGE_OFFSET) {
                Log.d(TAG, "getDirection: TOP");
                return Direction.TOP;
            } else if (y > getMaxY() - RANGE_OFFSET && y < getMaxY() + RANGE_OFFSET) {
                Log.d(TAG, "getDirection: BOTTOM");
                return Direction.BOTTOM;
            }
        }
        if (y > getMinY() && y < getMaxY()) {
            if (x > getMinX() - RANGE_OFFSET && x < getMinX() + RANGE_OFFSET) {
                Log.d(TAG, "getDirection: LEFT");
                return Direction.LEFT;
            } else if (x > getMaxX() - RANGE_OFFSET && x < getMaxX() + RANGE_OFFSET) {
                Log.d(TAG, "getDirection: RIGHT");
                return Direction.RIGHT;
            }
        }
        Log.d(TAG, "getDirection: OUTSIDE");
        return Direction.OUTSIDE;
    }

    /**
     * 是否是不可用的
     */
    public boolean isDisable() {
        return startX == 0 && startY == 0 && endX == 0 && endY == 0;
    }

    private static final String TAG = "DashRectangle";
}
