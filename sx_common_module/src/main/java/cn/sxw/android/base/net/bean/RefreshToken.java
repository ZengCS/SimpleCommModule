package cn.sxw.android.base.net.bean;

/**
 * Created by ZengCS on 2017/7/19.
 * E-mail:zcs@sxw.cn
 * Add:成都市天府软件园E3-3F
 */

public class RefreshToken {
    private String token;
    private String refreshToken;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
