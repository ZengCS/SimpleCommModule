package cn.sxw.android.base.okhttp.request;

import android.app.Activity;

import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.annotation.JSONField;

import cn.sxw.android.base.bean.user.UserInfoResponse;
import cn.sxw.android.base.okhttp.ApiConfig;
import cn.sxw.android.base.okhttp.BaseRequest;
import cn.sxw.android.base.okhttp.HttpCallback;

/**
 * Created by ZengCS on 2019/1/9.
 * E-mail:zcs@sxw.cn
 * Add:成都市天府软件园E3-3F
 * <p>
 * 返回结果是列表时使用
 */
public class UserInfoRequest extends BaseRequest {
    @JSONField(serialize = false)
    private HttpCallback<UserInfoRequest, UserInfoResponse> httpCallback;

    /**
     * @param activity
     */
    public UserInfoRequest(Activity activity, int userType) {
        super(activity, String.format(ApiConfig.API_FIND_USER_INFO, userType));
    }

    @Override
    public HttpCallback<UserInfoRequest, UserInfoResponse> getHttpCallback() {
        return httpCallback;
    }

    public UserInfoRequest setHttpCallback(HttpCallback<UserInfoRequest, UserInfoResponse> httpCallback) {
        this.httpCallback = httpCallback;
        // 不要忘记设置数据类型，用于JSON数据反序列化
        super.setTypeReference(new TypeReference<UserInfoResponse>() {
        });
        return this;
    }
}
