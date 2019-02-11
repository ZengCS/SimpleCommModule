package com.zcs.android.lib.sketch.bean;

/**
 * Created by ZengCS on 2017/10/11.
 * E-mail:zcs@sxw.cn
 * Add:成都市天府软件园E3-3F
 */
public class ToolColorBean extends BaseBean {
    private int color;
    private String colorString;

    public ToolColorBean(int color, String colorString) {
        this.color = color;
        this.colorString = colorString;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getColorString() {
        return colorString;
    }

    public void setColorString(String colorString) {
        this.colorString = colorString;
    }
}
