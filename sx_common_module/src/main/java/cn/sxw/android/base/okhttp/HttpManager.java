package cn.sxw.android.base.okhttp;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * HttpManager
 *
 * @author zcs@sxw.cn
 * @version v1.0
 * @date 2018/12/1 21:02
 */
public class HttpManager implements OkApiHelper {
    private static HttpManager sHttpManager;
    private BaseHttpManagerAdv mHttp;
    private Map<String, String> globalHeaderMap = new HashMap<>();// 全局Header
    private String host;
    private String scheme = "http";
    // 用于刷新Token
    private String refreshToken;

    public static HttpManager getInstance() {
        if (sHttpManager == null) {
            synchronized (HttpManager.class) {
                if (sHttpManager == null) {
                    sHttpManager = new HttpManager();
                }
            }
        }
        return sHttpManager;
    }

    public HttpManager setTokenHeader(String token) {
        globalHeaderMap.clear();
        if (!TextUtils.isEmpty(token)) {
            globalHeaderMap.put("TOKEN", token);
        }
        return sHttpManager;
    }

    public HttpManager setHost(String host) {
        this.host = host;
        return sHttpManager;
    }

    public HttpManager setScheme(String scheme) {
        this.scheme = scheme;
        return sHttpManager;
    }

    public String getHost() {
        return host;
    }

    public String getScheme() {
        return scheme;
    }

    private HttpManager() {
        mHttp = BaseHttpManagerAdv.getInstance();
        // 设置通用的结果处理回调，可在这里处理一些全局错误
        mHttp.setOnResultCallback(new BaseHttpManagerAdv.OnResultCallback() {
            @Override
            public void onResult(String json) {
                //TODO 正确的JSON
            }

            @Override
            public void onError(Object req, String msg) {
                // TODO 处理错误逻辑
            }

            @Override
            public void onSuccess(Object json) {
                //TODO 成功业务处理
            }
        });
    }

    public BaseHttpManagerAdv getHttpManager() {
        return mHttp;
    }

    @Override
    public void sendPost(BaseRequest request) {
        request.getHeadMap().putAll(globalHeaderMap);
        mHttp.sendPost(request);
    }

    @Override
    public void sendGet(BaseRequest request) {
        request.getHeadMap().putAll(globalHeaderMap);
        mHttp.sendGet(request);
    }

    @Override
    public void sendPut(BaseRequest request) {
        request.getHeadMap().putAll(globalHeaderMap);
        mHttp.sendPut(request);
    }

    @Override
    public void sendDelete(BaseRequest request) {
        request.getHeadMap().putAll(globalHeaderMap);
        mHttp.sendDelete(request);
    }

    public HttpManager setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}
