package com.zcs.android.lib.sketch.bean;

import java.io.Serializable;

/**
 * Created by ZengCS on 2017/9/5.
 * E-mail:zcs@sxw.cn
 * Add:成都市天府软件园E3-3F
 */
public class BaseBean implements Serializable {
    protected boolean checked;

    public BaseBean() {
    }

    public BaseBean(boolean checked) {
        this.checked = checked;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

}
