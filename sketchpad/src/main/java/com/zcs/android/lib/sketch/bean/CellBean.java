package com.zcs.android.lib.sketch.bean;

import android.text.TextUtils;

/**
 * Created by ZengCS on 2018/7/3.
 * E-mail:zcs@sxw.cn
 * Add:成都市天府软件园E3-3F
 */

public class CellBean extends BaseBean{
    private String name;
    private String path;// cell path

    public CellBean() {
    }

    public CellBean(String name, String path) {
        this.name = name;
        this.path = path;
    }

    public String getBgPath() {
        if (!TextUtils.isEmpty(path) && path.contains("cell")) {
            return path.replaceAll("cell", "bg");
        }
        return path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
