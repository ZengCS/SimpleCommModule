package com.zcs.android.lib.sketch.bean;

/**
 * Created by ZengCS on 2017/10/12.
 * E-mail:zcs@sxw.cn
 * Add:成都市天府软件园E3-3F
 */

public class DrawPoint extends BasePoint {
    public float width;
    public int alpha = 255;

    public DrawPoint() {
        super();
    }

    public DrawPoint(float x, float y) {
        super(x, y);
    }

    public DrawPoint(float x, float y, float width) {
        super(x, y);
        this.width = width;
    }

    public void set(float x, float y, float w) {
        this.x = x;
        this.y = y;
        this.width = w;
    }

    public void set(DrawPoint point) {
        this.x = point.x;
        this.y = point.y;
        this.width = point.width;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public String toString() {
        return "X = " + x + "; Y = " + y + "; W = " + width;
    }

}
