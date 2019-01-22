package cn.sxw.android.base.okhttp.response;

/**
 * Created by ZengCS on 2019/1/16.
 * E-mail:zcs@sxw.cn
 * Add:成都市天府软件园E3-3F
 */

public class LoginResponse {
    private String token;
    private String refreshToken;
    private long tokenExpiryDate;// Token有效期，单位秒
    private long refreshTokenExpiryDate;// RefreshToken有效期，单位秒

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

    public long getTokenExpiryDate() {
        return tokenExpiryDate;
    }

    public void setTokenExpiryDate(long tokenExpiryDate) {
        this.tokenExpiryDate = tokenExpiryDate;
    }

    public long getRefreshTokenExpiryDate() {
        return refreshTokenExpiryDate;
    }

    public void setRefreshTokenExpiryDate(long refreshTokenExpiryDate) {
        this.refreshTokenExpiryDate = refreshTokenExpiryDate;
    }
}
