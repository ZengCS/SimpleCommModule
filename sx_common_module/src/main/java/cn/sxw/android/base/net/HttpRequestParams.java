package cn.sxw.android.base.net;

import androidx.annotation.NonNull;

import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;

import java.io.Serializable;

/**
 * Created by ZengCS on 2018/7/20.
 * E-mail:zcs@sxw.cn
 * Add:成都市天府软件园E3-3F
 */
public class HttpRequestParams implements Serializable {
    private HttpMethod method;
    private RequestParams params;
    private ApiHelper.ApiCallback callback;

    public HttpRequestParams(@NonNull HttpMethod method, @NonNull RequestParams params, @NonNull ApiHelper.ApiCallback callback) {
        this.method = method;
        this.params = params;
        this.callback = callback;
    }

    @NonNull
    public HttpMethod getMethod() {
        return method;
    }

    public void setMethod(HttpMethod method) {
        this.method = method;
    }

    @NonNull
    public RequestParams getParams() {
        return params;
    }

    public void setParams(RequestParams params) {
        this.params = params;
    }

    @NonNull
    public ApiHelper.ApiCallback getCallback() {
        return callback;
    }

    public void setCallback(ApiHelper.ApiCallback callback) {
        this.callback = callback;
    }
}
