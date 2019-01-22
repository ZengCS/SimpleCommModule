package cn.sxw.android.base.okhttp;

public interface HttpCode {
    // 成功
    int OK = 200;

    // 找不到服务器或者接口
    int NOT_FOUND = 404;

    // 访问权限不出
    int FORBIDDEN = 403;

    // 方法不被允许
    int NOT_ALLOWED = 405;

    // 服务器错误
    int INTERNAL_SERVER_ERROR = 500;

    // 502 Bad Gateway
    int BAD_GATEWAY = 502;

    // 504 Gateway Timeout
    int GATEWAY_TIMEOUT = 504;

    // 网络没有接通
    int NETWORK_ERROR = -1;

    // JSON格式错误
    int JSON_ERROR = -2;

    // 文件创建无权限
    int FILE_NO_PRE = -3;

    // 文件保存失败
    int FILE_SAVE_ERROR = -4;

    // 暂无数据
    int NO_DATA = -5;

    // 其他错误
    int OTHER_ERROR = -6;

    // 用户名或者密码错误
    int USER_PWD_ERROR = 8072401;
    // 该账号已被禁用
    int USER_FORBIDDEN = 8072407;
    // 该账号未注册
    int UN_REGISTER = 8072409;
    // 系统内部错误
    int INNER_ERROR = 8072500;
    // token not found.
    int TOKEN_NOT_FOUND = 8500;
    // token is unknown error.
    int TOKEN_UNKNOWN_ERROR = 8501;
    // token have expired.
    int TOKEN_HAVE_EXPIRED = 8502;
    // token signature error.
    int TOKEN_SIGNATURE_ERROR = 8503;
    // unsupported token string.
    int TOKEN_UNSUPPORTED = 8504;
    // token is invalid.
    int TOKEN_IS_INVALID = 8505;
}