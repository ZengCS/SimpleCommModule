package cn.sxw.android.base.okhttp.request;

import android.app.Activity;

import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.annotation.JSONField;

import cn.sxw.android.base.okhttp.ApiConfig;
import cn.sxw.android.base.okhttp.BaseRequest;
import cn.sxw.android.base.okhttp.HttpCallback;
import cn.sxw.android.base.okhttp.response.LoginResponse;

/**
 * Created by ZengCS on 2019/1/16.
 * E-mail:zcs@sxw.cn
 * Add:成都市天府软件园E3-3F
 */

public class RefreshTokenRequest extends BaseRequest {
    @JSONField(serialize = false)
    private HttpCallback<RefreshTokenRequest, LoginResponse> httpCallback;
    @JSONField(serialize = false)
    private BaseRequest lastRequest;

    public RefreshTokenRequest(Activity activity) {
        super(activity, ApiConfig.API_REFRESH_TOKEN);
        this.lastRequest = null;
    }

    public RefreshTokenRequest(BaseRequest lastRequest) {
        super(lastRequest.getActivity(), ApiConfig.API_REFRESH_TOKEN);
        this.lastRequest = lastRequest;
    }

    @Override
    public HttpCallback<RefreshTokenRequest, LoginResponse> getHttpCallback() {
        return httpCallback;
    }

    public RefreshTokenRequest setHttpCallback(HttpCallback<RefreshTokenRequest, LoginResponse> httpCallback) {
        this.httpCallback = httpCallback;
        // 不要忘记设置数据类型，用于JSON数据反序列化
        super.setTypeReference(new TypeReference<LoginResponse>() {
        });
        return this;
    }

    public BaseRequest getLastRequest() {
        return lastRequest;
    }
}
