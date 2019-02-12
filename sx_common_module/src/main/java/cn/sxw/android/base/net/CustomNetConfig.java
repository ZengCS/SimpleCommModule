package cn.sxw.android.base.net;

/**
 * Created by ZengCS on 2017/8/3.
 * E-mail:zcs@sxw.cn
 * Add:成都市天府软件园E3-3F
 */
public class CustomNetConfig {
    private static String[] ENVIRONMENT_NAMES = {"生产环境", "预发布环境", "测试环境", "开发环境"};

    public static final int ENVIRONMENT_RELEASE = 0;// 生产环境
    public static final int ENVIRONMENT_RELEASE_PRE = 1;// 预发布环境
    public static final int ENVIRONMENT_BETA_TEST = 2;// 测试环境
    public static final int ENVIRONMENT_BETA_DEV = 3;// 开发环境

    // 当前环境，默认生产环境
    public static int currEnvironment = ENVIRONMENT_RELEASE;

    public static boolean isReleaseVersion() {
        return currEnvironment <= ENVIRONMENT_RELEASE_PRE;
    }

    public static String getCurrServerTypeName() {
        try {
            return ENVIRONMENT_NAMES[currEnvironment];
        } catch (Exception e) {
            return "未知的服务器类型";
        }
    }

    /**
     * 切换服务器
     */
    public static String changeServer(int serverType) {
        currEnvironment = serverType;
        return ENVIRONMENT_NAMES[currEnvironment];
    }

    private static class RunEnvironment {
        // Host
        static final String[] SXW_HOST = {
                "api2.sxw.cn",      // 生产环境
                "api2.pre.sxw.cn",  // 预发布环境域名
                "api2.test.sxw.cn", // 测试环境域名
                "api2.dev.sxw.cn"   // 开发环境域名
        };

        // api
        static final String[] SXW_BASE_APIS = {
                "http://api2.sxw.cn",      // 生产环境
                "http://api2.pre.sxw.cn",  // 预发布环境域名
                "http://api2.test.sxw.cn", // 测试环境域名
                "http://api2.dev.sxw.cn"   // 开发环境域名
        };

        static final String[] MDM_APIS = {
                SXW_BASE_APIS[0].concat("/mdc2/api/"),// 生产环境
                SXW_BASE_APIS[1].concat("/mdc2/api/"),// 预发布
                SXW_BASE_APIS[2].concat("/mdc2/api/"),// 测试
                SXW_BASE_APIS[3].concat("/mdc2/api/"),// 开发
        };

        // 版本检测
        static final String[] SXW_UPDATE_APIS = {
                SXW_BASE_APIS[0].concat("/update/"),// 生产环境
                SXW_BASE_APIS[1].concat("/update/"),// 预发布环境域名
                SXW_BASE_APIS[2].concat("/update/"),// 测试环境域名
                SXW_BASE_APIS[3].concat("/update/"),// 开发环境域名
        };
    }

    /**
     * @return Host
     */
    public static String getHost() {
        return RunEnvironment.SXW_HOST[currEnvironment];
    }

    /**
     * @return Passport接口头
     */
    public static String getPassportHost() {
        return RunEnvironment.SXW_BASE_APIS[currEnvironment].concat("/passport/api/");
    }

    public static String getNewUpdateHost() {
        return RunEnvironment.SXW_UPDATE_APIS[currEnvironment];
    }

    /**
     * @return Platform接口头 - NEW
     */
    public static String getNewPlatformHost() {
        return RunEnvironment.SXW_BASE_APIS[currEnvironment].concat("/platform/api/");
    }

    /**
     * @return 管控接口地址头
     */
    public static String getMdmHost() {
        return RunEnvironment.MDM_APIS[currEnvironment];
    }
}
