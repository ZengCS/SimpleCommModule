package cn.sxw.android.base.bean;

import java.io.Serializable;

import cn.sxw.android.base.bean.user.UserInfoResponse;
import cn.sxw.android.base.okhttp.response.LoginResponse;

/**
 * Created by ZengCS on 2019/1/17.
 * E-mail:zcs@sxw.cn
 * Add:成都市天府软件园E3-3F
 */

public class SSODetailBean implements Serializable {
    private LoginResponse tokenBean;// 当前用户的Token信息
    private UserInfoResponse userInfoResponse;// 当前用户的详细信息
    private LoginInfoBean loginInfo;// 当前用户的登录信息,只包括登录名和密码

    public SSODetailBean() {
    }

    public LoginResponse getTokenBean() {
        return tokenBean;
    }

    public void setTokenBean(LoginResponse tokenBean) {
        this.tokenBean = tokenBean;
    }

    public UserInfoResponse getUserInfoResponse() {
        return userInfoResponse;
    }

    public void setUserInfoResponse(UserInfoResponse userInfoResponse) {
        this.userInfoResponse = userInfoResponse;
    }

    public LoginInfoBean getLoginInfo() {
        return loginInfo;
    }

    public void setLoginInfo(LoginInfoBean loginInfo) {
        this.loginInfo = loginInfo;
    }
}
