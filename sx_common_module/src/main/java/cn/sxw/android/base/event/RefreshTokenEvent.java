package cn.sxw.android.base.event;

/**
 * Created by ZengCS on 2018/7/20.
 * E-mail:zcs@sxw.cn
 * Add:成都市天府软件园E3-3F
 */

public class RefreshTokenEvent {
    private String result;

    public RefreshTokenEvent(String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
