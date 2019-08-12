package cn.sxw.android.base.okhttp;

import android.app.Activity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.annotation.JSONField;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by ZengCS on 2019/1/9.
 * E-mail:zcs@sxw.cn
 * Add:成都市天府软件园E3-3F
 */

public abstract class BaseRequest {
    @JSONField(serialize = false)
    private String api = "";// api路径,不带host
    @JSONField(serialize = false)
    private ConcurrentMap<String, String> paramMap;// query 参数列表
    @JSONField(serialize = false)
    private HashMap<String, String> headMap;// Http Header 参数列表
    @JSONField(serialize = false)
    private Activity activity = null;// 1.提供Context 2.验证是否需要回调
    @JSONField(serialize = false)
    private TypeReference typeReference;// 用于FastJson反序列化
    @JSONField(serialize = false)
    private int methodType = OkApiHelper.METHOD_UNKNOWN;
    @JSONField(serialize = false)
    private boolean allowRefreshToken = true;// 是否可以刷新token
    @JSONField(serialize = false)
    private boolean allowAutoLogin = true;// 是否可以自动登录
    // 将Activity设置为弱引用,解决内存泄漏问题
    @JSONField(serialize = false)
    private WeakReference<Activity> activityWeakReference;
    // 2019年3月29日
    @JSONField(serialize = false)
    private boolean responseAllWhenError = false;// 当发生异常时,返回所有信息

    protected abstract <T, V> HttpCallback<T, V> getHttpCallback();

    public BaseRequest(Activity activity, String api) {
        if (activity != null)
            activityWeakReference = new WeakReference<>(activity);
        this.api = api;
    }

    public String getApi() {
        try {
            if (!api.startsWith("http:") && !api.startsWith("https:")) {
                return HttpUrlEncode.encode(
                        HttpManager.getInstance().getScheme(),
                        HttpManager.getInstance().getHost(),
                        api, paramMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public void addQueryParameter(String key, Object val) {
        if (paramMap == null)
            paramMap = new ConcurrentHashMap<>();
        paramMap.put(key, String.valueOf(val));
    }

    public void addHeader(String key, Object val) {
        if (headMap == null)
            headMap = new HashMap<>();
        headMap.put(key, String.valueOf(val));
    }

    public HashMap<String, String> getHeadMap() {
        if (headMap == null)
            headMap = new HashMap<>();
        return headMap;
    }

    public Activity getActivity() {
        if (activityWeakReference == null)
            return null;
        try {
            return activityWeakReference.get();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public TypeReference getTypeReference() {
        return typeReference;
    }

    public void setTypeReference(TypeReference typeReference) {
        this.typeReference = typeReference;
    }

    public void post() {
        HttpManager.getInstance().sendPost(this);
    }

    public void get() {
        HttpManager.getInstance().sendGet(this);
    }

    public void put() {
        HttpManager.getInstance().sendPut(this);
    }

    public void delete() {
        HttpManager.getInstance().sendDelete(this);
    }

    public String toJson() {
        return JSON.toJSONString(this);
    }

    public void setMethodType(int methodType) {
        this.methodType = methodType;
    }

    public int getMethodType() {
        return methodType;
    }

    public boolean isAllowRefreshToken() {
        return allowRefreshToken;
    }

    public void setAllowRefreshToken(boolean allowRefreshToken) {
        this.allowRefreshToken = allowRefreshToken;
    }

    public boolean isAllowAutoLogin() {
        return allowAutoLogin;
    }

    public void setAllowAutoLogin(boolean allowAutoLogin) {
        this.allowAutoLogin = allowAutoLogin;
    }

    public boolean isResponseAllWhenError() {
        return responseAllWhenError;
    }

    public void setResponseAllWhenError(boolean responseAllWhenError) {
        this.responseAllWhenError = responseAllWhenError;
    }
}
