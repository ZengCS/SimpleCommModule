package cn.sxw.android.base.okhttp.request;

import android.app.Activity;

import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.annotation.JSONField;

import cn.sxw.android.base.okhttp.ApiConfig;
import cn.sxw.android.base.okhttp.BaseRequest;
import cn.sxw.android.base.okhttp.HttpCallback;
import cn.sxw.android.base.okhttp.response.LoginResponse;
import cn.sxw.android.base.utils.MyTextUtils;

/**
 * Created by ZengCS on 2019/1/9.
 * E-mail:zcs@sxw.cn
 * Add:成都市天府软件园E3-3F
 * <p>
 * 返回结果是列表时使用
 */
public class LoginRequest extends BaseRequest {
    // 具体的参数，只适用于Body，当Method=GET时，无效
    private String account;// 登录帐号值，根据登录类型不同，取对应的值
    private int accountType = 1;// 登录帐号类型 0=手机 1=身份证 2=用户名 3=邮箱 4=qq 5=微信 6=微博,7=网阅临时账号，8=手机验证码
    private String app = "MDM";// App: SXT|XWCZ|XWKT|XWKW|MDM
    /**
     * 该参数 3.0.0 已废弃
     */
    @Deprecated
    private String client = "STUDENT";// 客户端：STUDENT|TEACHER|PARENTS|MANAGER
    private String password;// 登录密码，请求时请将密码进行32位 MD5(password) 后传参;
    private String platform = "ANDROID";// 平台：IOS|ANDROID|PAD|PC|H5|WECHAT
    // 3.0新增参数
    private boolean twoFa = false;// 是否使用多因素一次性授权码进行登录，为 true 时，password 为一次性6位数随机授权码，参数无需加密
    private String userType = "1";// 用户组类型(0=普通,1=学生,2=老师,3=家长,5=管理员,6=租户管理员)多个类型，请使用逗号分隔,如[2,5]

    @JSONField(serialize = false)
    private HttpCallback<LoginRequest, LoginResponse> httpCallback;

    /**
     * @param activity
     */
    public LoginRequest(Activity activity) {
        super(activity, ApiConfig.API_LOGIN);
    }

    @Override
    public HttpCallback<LoginRequest, LoginResponse> getHttpCallback() {
        return httpCallback;
    }

    public LoginRequest setHttpCallback(HttpCallback<LoginRequest, LoginResponse> httpCallback) {
        this.httpCallback = httpCallback;
        // 不要忘记设置数据类型，用于JSON数据反序列化
        super.setTypeReference(new TypeReference<LoginResponse>() {
        });
        return this;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        if (MyTextUtils.isPhoneNumber(account)) {
            setAccountType(0);
        } else {
            setAccountType(1);
        }
        this.account = account;
    }

    public int getAccountType() {
        return accountType;
    }

    public void setAccountType(int accountType) {
        this.accountType = accountType;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public boolean isTwoFa() {
        return twoFa;
    }

    public void setTwoFa(boolean twoFa) {
        this.twoFa = twoFa;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
