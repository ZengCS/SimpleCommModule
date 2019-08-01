package cn.sxw.android.base.okhttp;

/**
 * Created by ZengCS on 2019/1/17.
 * E-mail:zcs@sxw.cn
 * Add:成都市天府软件园E3-3F
 */

public class ApiConfig {
    /**
     * 用户登录
     */
    public static final String API_LOGIN = "/passport/api/auth/login";

    /**
     * 刷新Token
     */
    public static final String API_REFRESH_TOKEN = "/passport/api/auth/refresh_token";

    /**
     * 查找用户信息
     */
    public static final String API_FIND_USER_INFO = "platform/api/user/get_user_info/v3/%s";
}
