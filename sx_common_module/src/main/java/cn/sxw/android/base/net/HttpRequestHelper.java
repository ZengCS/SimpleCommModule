package cn.sxw.android.base.net;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.xutils.common.Callback;
import org.xutils.ex.BaseException;
import org.xutils.ex.HttpException;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Singleton;

import cn.sxw.android.R;
import cn.sxw.android.base.event.ReLoginEvent;
import cn.sxw.android.base.event.RefreshTokenEvent;
import cn.sxw.android.base.event.ResetNetworkWhiteListEvent;
import cn.sxw.android.base.net.bean.BaseResponse;
import cn.sxw.android.base.net.bean.LocalTokenCache;
import cn.sxw.android.base.net.bean.RefreshToken;
import cn.sxw.android.base.utils.LogUtil;
import cn.sxw.android.base.utils.NetworkUtil;

@Singleton
public class HttpRequestHelper implements ApiHelper {
    private static final int TIME_OUT = 5 * 1000;// 5秒超时

    private static final int CODE_TOKEN_NOT_FOUND = 8500;// Token not found

    private static final int CODE_TOKEN_ERROR = 8501;// Token错误

    private static final int CODE_TOKEN_TIMEOUT = 8502;// Token过期

    private static final int CODE_TOKEN_SIGN_ERROR = 8503;// TOKEN签名错误

    private static final int CODE_NOT_SUPPORT_TOKEN_ERROR = 8504;// 不支持的TOKEN

    private static final int CODE_TOKEN_STRING_ERROR = 8505;// TOKEN字符串无效

    public static final int CODE_PASSWORD_ERROR = 8072401;// 密码错误码

    private String token;

    private String refreshToken;

    @Inject
    public HttpRequestHelper() {

    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setRefreshToke(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    private String getRefreshToken() {
        if (TextUtils.isEmpty(refreshToken)) {
            refreshToken = LocalTokenCache.getLocalCacheRefreshToken();
        }
        return refreshToken;
    }

    /**
     * 刷新token 如果刷新失败 发送广播 重新登录
     */
    private void refreshToken(final Context context, @NonNull String api, final HttpRequestParams httpRequestParams) {
        RequestParams requestParams = new RequestParams(api);
        requestParams.setHeader("TOKEN", getRefreshToken());
        requestParams.setCacheMaxAge(0);// 为请求添加缓存时间
        requestParams.setConnectTimeout(5 * 1000); // 请求超时时间
        x.http().request(HttpMethod.GET, requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtil.methodStep("refreshToken：" + result);
                BaseResponse baseResponse = JSON.parseObject(result, BaseResponse.class);
                if (baseResponse.getCode() == CODE_TOKEN_TIMEOUT ||
                        baseResponse.getCode() == CODE_TOKEN_NOT_FOUND ||
                        baseResponse.getCode() == CODE_TOKEN_ERROR ||
                        baseResponse.getCode() == CODE_NOT_SUPPORT_TOKEN_ERROR ||
                        baseResponse.getCode() == CODE_TOKEN_SIGN_ERROR ||
                        baseResponse.getCode() == CODE_TOKEN_STRING_ERROR) {
                    // 这个时候就需要重新登录了
                    LogUtil.e("refreshToken: RefreshToken 已经过期,需要用户重新登录~");
                    // onError(new BaseException(BaseException.CAUSE_REFRESH_TOKEN_TIMEOUT), false);
                    onError(new BaseException(""), false);
                    EventBus.getDefault().post(new ReLoginEvent());
                } else if (baseResponse.isRequestSuccess()) {
                    String tokenResult = baseResponse.getData();
                    if (!TextUtils.isEmpty(tokenResult)) {
                        LogUtil.methodStep("refreshToken: 刷新TOKEN成功~");
                        RefreshToken refreshToken = JSON.parseObject(tokenResult, RefreshToken.class);
                        setRefreshToke(refreshToken.getRefreshToken());
                        setToken(refreshToken.getToken());
                        requestData(context, httpRequestParams);
                        // 发送TOKEN更新通知
                        EventBus.getDefault().post(new RefreshTokenEvent(tokenResult));
                    } else {
                        LogUtil.w("refreshToken: 刷新TOKEN失败~");
                        Toast.makeText(context, R.string.login_time_out, Toast.LENGTH_SHORT).show();
                        // httpRequestParams.getCallback().onRequestFailed(BaseException.CAUSE_REFRESH_TOKEN_TIMEOUT);
                        httpRequestParams.getCallback().onRequestFailed("");
                        EventBus.getDefault().post(new ReLoginEvent());
                    }
                } else {
                    LogUtil.e("mRefreshToken: Token刷新失败~msg = " + baseResponse.getMessage());
                    onError(new BaseException(baseResponse.getMessage()), false);
                    // httpRequestParams.getCallback().onRequestFailed(BaseException.CAUSE_REFRESH_TOKEN_TIMEOUT);
                    httpRequestParams.getCallback().onRequestFailed("");
                    EventBus.getDefault().post(new ReLoginEvent());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("刷新TOKEN失败，" + ex);
                httpRequestParams.getCallback().onRequestFailed(ex.getMessage());
                EventBus.getDefault().post(new ReLoginEvent());
                // 如果刷新Token失败，不做任何操作
//                Toast.makeText(context, R.string.login_time_out, Toast.LENGTH_SHORT).show();
//                context.sendBroadcast(new Intent("cn.sxw.launcher.action.LOGIN_AGAIN"));
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });
    }

    private void setTokenHeader(RequestParams params) {
        // 在网络请求时 增加一个head ：字段为Request-Id 值为 用户id+当前时间  类型为long型啊
//        String requestId = System.nanoTime() + "" + (int) (Math.random() * 90 + 10);
//        params.setHeader("Request-Id", requestId);

        String requestId = UUID.randomUUID().toString();
        params.setHeader("Trace-Id", requestId);

        LogUtil.methodStep("----Trace-Id = " + requestId);
        if (!TextUtils.isEmpty(token)) {
            params.setHeader("TOKEN", token);
        } else {
            String cacheToken = LocalTokenCache.getLocalCacheToken();
            if (!TextUtils.isEmpty(cacheToken)) {
                // token = cacheToken;
                params.setHeader("TOKEN", cacheToken);
            } else {
                LogUtil.methodStepError("Token is empty");
            }
        }
    }

    private void requestData(final Context context, final HttpRequestParams httpRequestParams) {
        requestData(context, httpRequestParams.getMethod(), httpRequestParams.getParams(), httpRequestParams.getCallback());
    }

    private void requestData(final Context context, @NonNull HttpMethod method, @NonNull final RequestParams params, final ApiCallback callback) {
        if (!NetworkUtil.isNetworkAvailable(context)) {
            callback.onRequestFailed(context.getResources().getString(R.string.network_access_error));
            return;
        }
        LogUtil.methodStart("requestData");
        LogUtil.methodStep("-----------api = " + params.getUri());
        LogUtil.methodStep("--------method = " + method);
        LogUtil.methodStep("---------param = " + params.getBodyContent());
        final long startTime = System.currentTimeMillis();

        params.setConnectTimeout(TIME_OUT);// 默认值:1000 * 15
        params.setAsJsonContent(true);
        setTokenHeader(params);
        // 缓存本次请求相关信息，用于TOKEN过期后自动重连
        final HttpRequestParams httpRequestParams = new HttpRequestParams(method, params, callback);

        x.http().request(method, params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtil.methodStart("数据加载成功");
                LogUtil.methodStep(params.getUri());
                LogUtil.methodStep("用时：" + (System.currentTimeMillis() - startTime) + "ms");
                analysisResponseData(context, httpRequestParams, result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                // LogUtil.methodStep( "-----------onError");
                LogUtil.methodStart("数据加载失败");
                LogUtil.methodStep(params.getUri());
                analysisErrorInfo(context, httpRequestParams, ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtil.methodStep("-----------onCancelled");
            }

            @Override
            public void onFinished() {
                // LogUtil.methodStep( "-----------onFinished");
            }
        });
    }

    /**
     * HTTP POST
     */
    public void postData(final Context context, @NonNull String api, Object data, final ApiCallback callback) {
        RequestParams params = new RequestParams(api);
        params.setBodyContent(JSON.toJSONString(data));
        postData(context, params, callback);
    }

    @Override
    public void postData(Context context, @NonNull RequestParams params, final ApiCallback callback) {
        requestData(context, HttpMethod.POST, params, callback);
    }

    /**
     * HTTP GET
     */
    public void getData(final Context context, @NonNull String api, HashMap<String, String> requestParams, final ApiCallback callback) {
        RequestParams params = new RequestParams(api);
        // 遍历读取参数
        if (requestParams != null) {
            for (Map.Entry<String, String> entry : requestParams.entrySet()) {
                params.addQueryStringParameter((entry.getKey()), (entry.getValue()));
            }
        }
        getData(context, params, callback);
    }

    @Override
    public void getData(Context context, @NonNull RequestParams params, final ApiCallback callback) {
        requestData(context, HttpMethod.GET, params, callback);
    }

    /**
     * HTTP PUT
     */
    @Override
    public void putData(Context context, @NonNull String api, Object data, final ApiCallback callback) {
        RequestParams params = new RequestParams(api);
        params.setBodyContent(JSON.toJSONString(data));
        putData(context, params, callback);
    }

    @Override
    public void putData(Context context, @NonNull RequestParams params, final ApiCallback callback) {
        requestData(context, HttpMethod.PUT, params, callback);
    }

    @Override
    public void analysisErrorInfo(final Context context, final HttpRequestParams httpRequestParams, Throwable ex) {
        LogUtil.methodStep("解析Error数据");
        // LogUtil.methodStep( "*********************** 解析Error数据 ***********************");
        ex.printStackTrace();
        String exMessage = ex.getMessage();
        ApiCallback callback = httpRequestParams.getCallback();
        if (exMessage == null)
            exMessage = context.getResources().getString(R.string.server_error_unknown);
        if ("Connection refused".equalsIgnoreCase(exMessage)) {
            LogUtil.methodStart("当出现服务器异常时，重置网络防火墙");
            // 当出现服务器异常时，重置网络防火墙
            EventBus.getDefault().post(new ResetNetworkWhiteListEvent());
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // 两秒后重试
                    LogUtil.methodStep("网络出现异常后，自动重连机制生效。");
                    requestData(context, httpRequestParams);
                }
            }, 1000);
            // callback.onRequestFailed(context.getResources().getString(R.string.server_error));
            return;
        }

        if (ex instanceof SocketTimeoutException) {
            LogUtil.e("[SocketTimeoutException]访问接口超时");
            callback.onRequestFailed(context.getResources().getString(R.string.server_timeout));
        } else if (!TextUtils.isEmpty(exMessage)) {
            if (exMessage.equals("Not Found")) {
                LogUtil.w("analysisErrorInfo: API NOT FOUND");
                callback.onRequestFailed(context.getResources().getString(R.string.get_info_failed));
            } else {
                LogUtil.w("analysisErrorInfo: " + exMessage);
                callback.onRequestFailed(exMessage);
            }
        } else {
            // TODO 判断错误类型，实施刷新Token的操作
            if (ex instanceof HttpException) {
                HttpException he = (HttpException) ex;
                String result = he.getResult();
                if (!TextUtils.isEmpty(result)) {
                    BaseResponse baseResponse = JSON.parseObject(result, BaseResponse.class);
                    if (baseResponse.getCode() == CODE_TOKEN_TIMEOUT) {
                        LogUtil.w("---------TOKEN已过期或TOKEN错误");
                        refreshToken(context, REFRESH_TOKEN_API, httpRequestParams);
                    }
                }
            }
            callback.onRequestFailed(context.getResources().getString(R.string.server_error));
            LogUtil.w("analysisErrorInfo: 服务器错误");
        }
    }

    @Override
    public void analysisResponseData(Context context, final HttpRequestParams httpRequestParams, String response) {
        if (response.contains("@type"))
            response = response.replaceAll("@type", "type");

        LogUtil.methodStep("解析Response数据");
        try {
            // 压缩JSON
            LogUtil.methodStep(new org.json.JSONObject(response).toString());
        } catch (JSONException e) {
            // e.printStackTrace();
            // LogUtil.e(e);
            LogUtil.methodStep(String.valueOf(response));
        }
        BaseResponse baseResponse = JSON.parseObject(response, BaseResponse.class);
        ApiCallback callback = httpRequestParams.getCallback();

        if (baseResponse.isRequestSuccess()) {
            callback.onRequestSuccess(baseResponse.getData());
        } else if (baseResponse.getCode() == CODE_TOKEN_TIMEOUT) {
            LogUtil.w("---------TOKEN已过期或TOKEN错误");
            refreshToken(context, REFRESH_TOKEN_API, httpRequestParams);
        } else if (baseResponse.getCode() == CODE_TOKEN_NOT_FOUND ||
                baseResponse.getCode() == CODE_TOKEN_ERROR ||
                baseResponse.getCode() == CODE_NOT_SUPPORT_TOKEN_ERROR ||
                baseResponse.getCode() == CODE_TOKEN_SIGN_ERROR ||
                baseResponse.getCode() == CODE_TOKEN_STRING_ERROR) {
            Toast.makeText(context, R.string.login_time_out, Toast.LENGTH_SHORT).show();
            // 发送重新登录广播
            // context.sendBroadcast(new Intent("cn.sxw.launcher.action.LOGIN_AGAIN"));
            refreshToken(context, REFRESH_TOKEN_API, httpRequestParams);
        } else if (baseResponse.isFormatError()) {
            LogUtil.w("---------API返回了错误格式的JSON数据！");
            callback.onRequestFailed("JSON数据格式错误！");
        } else {
            /*// 增加密码错误码判断
            if (baseResponse.getCode() == CODE_PASSWORD_ERROR) {
                callback.onRequestFailed(String.valueOf(baseResponse.getCode()));
            } else */
            if (baseResponse.getCode() == 200) {
                callback.onRequestSuccess(response);
            } else {
                LogUtil.w("---------数据加载失败，message = " + baseResponse.getMessage());
                callback.onRequestFailed(baseResponse.getMessage());
            }
        }
    }

}
