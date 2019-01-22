package cn.sxw.android.base.event;

/**
 * Created by ZengCS on 2018/7/20.
 * E-mail:zcs@sxw.cn
 * Add:成都市天府软件园E3-3F
 */

public class RefreshTokenFailedEvent {
    private int code;
    private String msg;

    public RefreshTokenFailedEvent(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
