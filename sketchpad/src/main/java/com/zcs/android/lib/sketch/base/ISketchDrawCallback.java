package com.zcs.android.lib.sketch.base;


import com.zcs.android.lib.sketch.bean.DrawPoint;

import java.util.List;

/**
 * Created by ZengCS on 2018/10/15.
 * E-mail:zcs@sxw.cn
 * Add:成都市天府软件园E3-3F
 */

public interface ISketchDrawCallback {
    void onDrawSuccess(List<DrawPoint> pointList);
}
