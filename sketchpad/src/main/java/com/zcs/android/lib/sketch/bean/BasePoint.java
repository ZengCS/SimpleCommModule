package com.zcs.android.lib.sketch.bean;

/**
 * Created by ZengCS on 2017/12/12.
 * E-mail:zcs@sxw.cn
 * Add:成都市天府软件园E3-3F
 */

public class BasePoint {
    public float x;
    public float y;

    public BasePoint() {
    }

    public BasePoint(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
}
