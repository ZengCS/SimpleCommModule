package cn.sxw.android.base.bean;

import java.io.Serializable;

/**
 * Created by ZengCS on 2019/1/17.
 * E-mail:zcs@sxw.cn
 * Add:成都市天府软件园E3-3F
 */

public class LoginInfoBean implements Serializable {
    private String account;
    private String pwd;

    public LoginInfoBean() {
    }

    public LoginInfoBean(String account, String pwd) {
        this.account = account;
        this.pwd = pwd;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
