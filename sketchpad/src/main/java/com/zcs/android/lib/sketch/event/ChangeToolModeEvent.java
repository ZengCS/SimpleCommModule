package com.zcs.android.lib.sketch.event;

/**
 * Created by ZengCS on 2018/11/30.
 * E-mail:zcs@sxw.cn
 * Add:成都市天府软件园E3-3F
 */

public class ChangeToolModeEvent {
    private String msg;

    public ChangeToolModeEvent() {
    }

    public ChangeToolModeEvent(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
