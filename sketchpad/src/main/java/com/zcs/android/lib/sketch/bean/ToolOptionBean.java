package com.zcs.android.lib.sketch.bean;

/**
 * Created by ZengCS on 2018/2/8.
 * E-mail:zcs@sxw.cn
 * Add:成都市天府软件园E3-3F
 */
public class ToolOptionBean extends BaseBean {
    public static final int TOOL_OPTION_INPUT = 0;// 输入
    public static final int TOOL_OPTION_ATTR = 1;// 属性
    public static final int TOOL_OPTION_GEO = 2;// 图形
    public static final int TOOL_OPTION_PIC = 3;// 图片
    public static final int TOOL_OPTION_OTHER = 4;// 其他
    public static final int TOOL_OPTION_HIDE = -1;// 隐藏

    private int option;
    private int resId;

    public ToolOptionBean() {
    }

    public ToolOptionBean(int option, int resId) {
        this.option = option;
        this.resId = resId;
    }

    public int getOption() {
        return option;
    }

    public void setOption(int option) {
        this.option = option;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }
}
