package cn.sxw.android.base.net;

import android.content.Context;
import android.support.annotation.NonNull;

import org.xutils.http.RequestParams;

import java.util.HashMap;

public interface ApiHelper {
    String REFRESH_TOKEN_API = CustomNetConfig.getPassportHost().concat("auth/refresh_token");

    void postData(final Context context, @NonNull String api, Object data, final ApiCallback callback);

    void postData(final Context context, @NonNull RequestParams params, final ApiCallback callback);

    void getData(final Context context, @NonNull String api, HashMap<String, String> requestParams, final ApiCallback callback);

    void getData(final Context context, @NonNull RequestParams params, final ApiCallback callback);

    void putData(final Context context, @NonNull String api, Object data, final ApiCallback callback);

    void putData(final Context context, @NonNull RequestParams params, final ApiCallback callback);

    void setToken(String token);

    void setRefreshToke(String refreshToke);

    interface ApiCallback {
        void onRequestSuccess(String result);

        void onRequestFailed(String message);
    }


    /**
     * 解析Response数据
     */
    void analysisResponseData(Context context, final HttpRequestParams httpRequestParams, String response);

    /**
     * 解析错误信息
     */
    void analysisErrorInfo(Context context, final HttpRequestParams httpRequestParams, Throwable ex);
}
