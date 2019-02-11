package com.zcs.android.lib.sketch.bean;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.RectF;

public class PhotoRecord {
    public static final int DFT_MAX_SCALE = 8;

    public boolean isFromUser = false;// 是否来自用户
    public String picUrl;// 图片地址
    public Bitmap bitmap;// 图形
    public Matrix matrix;// 图形
    public RectF photoRectSrc = new RectF();
    public float scaleMax = DFT_MAX_SCALE;
}