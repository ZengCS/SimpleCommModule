package cn.sxw.android.lib.mvp.presenter;

import android.app.Application;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import cn.sxw.android.BuildConfig;
import cn.sxw.android.base.account.SAccountUtil;
import cn.sxw.android.base.bean.BlankBean;
import cn.sxw.android.base.bean.LoginInfoBean;
import cn.sxw.android.base.bean.user.UserInfoResponse;
import cn.sxw.android.base.di.scope.PerActivity;
import cn.sxw.android.base.mvp.BasePresenter;
import cn.sxw.android.base.okhttp.HttpCallback;
import cn.sxw.android.base.okhttp.HttpCode;
import cn.sxw.android.base.okhttp.request.LoginRequest;
import cn.sxw.android.base.okhttp.request.UserInfoRequest;
import cn.sxw.android.base.okhttp.response.LoginResponse;
import cn.sxw.android.base.utils.AESUtils;
import cn.sxw.android.base.utils.JTextUtils;
import cn.sxw.android.lib.mvp.model.empty.IEmptyModel;
import cn.sxw.android.lib.mvp.model.request.TestListRequest;
import cn.sxw.android.lib.mvp.model.request.TestObjRequest;
import cn.sxw.android.lib.mvp.model.request.TestStringRequest;
import cn.sxw.android.lib.mvp.view.IEmptyView;

@PerActivity
public class EmptyPresenter extends BasePresenter<IEmptyModel, IEmptyView> {
    private Application mApplication;
    private int requestTimes = 0;

    @Inject
    public EmptyPresenter(IEmptyModel model, IEmptyView view, Application application) {
        super(model, view);
        this.mApplication = application;
    }

    @Override
    public boolean useEventBus() {
        return false;
    }

    /**
     * 加载列表类型的数据
     */
    public void getListByOkhttp(String api) {
        // 这里注意，只需要输入API名称，无需输入HOST
        TestListRequest request = new TestListRequest(mRootView.getActivity(), api);
        // 这些参数会被转成JSON，只有是POST请求时才有用，因为这些要放进Body内，GET方式无法设置Body
        request.setKey1("val1");
        request.setKey2("val2");
        // 这些参数会拼接到url
        request.addQueryParameter("p1", 1);
        request.addQueryParameter("p2", 2.0);
        request.addQueryParameter("p3", "test");
        request.addQueryParameter("p4", true);
        // 这些参数会放到Header里
        request.addHeader("header1", "1234567890");
        request.addHeader("header2", "9874563210");

        request.setHttpCallback(new HttpCallback<TestListRequest, List<BlankBean>>() {
            @Override
            public void onStart() {
                mRootView.showLoading();
            }

            @Override
            public void onResult(TestListRequest req, List<BlankBean> list) {
                if (requestTimes == 0 || requestTimes == 3) {
                    onFail(req, HttpCode.NO_DATA, "模拟加载失败");
                } else if (requestTimes == 1) {
                    // 模拟返回空列表
                    mRootView.onRequestSuccess(new ArrayList<>());
                } else {
                    if (list != null && list.size() > 0) {
                        // TODO 在这里处理数据缓存或者其他持久化操作
                        mModel.cacheListData(req, list);
                        // 回调成功
                        mRootView.onRequestSuccess(list);
                    } else {
                        onFail(req, HttpCode.NO_DATA, "暂无数据");
                    }
                }
                requestTimes++;
            }

            @Override
            public void onFail(TestListRequest req, int code, String msg) {
                mRootView.onFailed(msg);
                mRootView.showToast("[ErrorCode = " + code + "]" + msg);
                mRootView.getTipsTextView().setText("[ErrorCode = " + code + "]" + msg);
            }

            @Override
            public void onFinish() {
                mRootView.hideLoading();
            }
        }).post();
    }

    /**
     * 加载对象类型的数据
     */
    public void getObjectByOkhttp(String api) {
        // 这里注意，只需要输入API名称，无需输入HOST
        TestObjRequest request = new TestObjRequest(mRootView.getActivity(), api);
        // 这些参数会被转成JSON，只有是POST请求时才有用，因为这些要放进Body内，GET方式无法设置Body
        request.setKey1("val1");
        request.setKey2("val2");
        // 这些参数会拼接到url
        request.addQueryParameter("p1", 1);
        request.addQueryParameter("p2", 2.0);
        request.addQueryParameter("p3", "test");
        request.addQueryParameter("p4", true);
        // 这些参数会放到Header里
        request.addHeader("header1", "1234567890");
        request.addHeader("header2", "9874563210");

        request.setHttpCallback(new HttpCallback<TestObjRequest, BlankBean>() {
            @Override
            public void onStart() {
                mRootView.showLoading();
            }

            @Override
            public void onResult(TestObjRequest req, BlankBean bean) {
                mRootView.getTipsTextView().setText(JSON.toJSONString(bean));
            }

            @Override
            public void onFail(TestObjRequest req, int code, String msg) {
                mRootView.showToast("[ErrorCode = " + code + "]" + msg);
                mRootView.getTipsTextView().setText("[ErrorCode = " + code + "]" + msg);
            }

            @Override
            public void onFinish() {
                mRootView.hideLoading();
            }
        }).get();
    }

    /**
     * 加载对象类型的数据
     */
    public void getStringByOkhttp(String api) {
        // 这里注意，只需要输入API名称，无需输入HOST
        TestStringRequest request = new TestStringRequest(mRootView.getActivity(), api);
        // 这些参数会被转成JSON，只有是POST请求时才有用，因为这些要放进Body内，GET方式无法设置Body
        request.setKey1("val1");
        request.setKey2("val2");
        // 这些参数会拼接到url
        request.addQueryParameter("p1", 1);
        request.addQueryParameter("p2", 2.0);
        request.addQueryParameter("p3", "test");
        request.addQueryParameter("p4", true);
        // 这些参数会放到Header里
        request.addHeader("header1", "1234567890");
        request.addHeader("header2", "9874563210");

        request.setHttpCallback(new HttpCallback<TestStringRequest, String>() {
            @Override
            public void onStart() {
                mRootView.showLoading();
            }

            @Override
            public void onResult(TestStringRequest req, String result) {
                if (TextUtils.isDigitsOnly(result)) {// 接口返回的Data字段类型是Integer
                    Integer integer = Integer.parseInt(result);
                    mRootView.getTipsTextView().setText("Integer --> " + integer);
                } else if (JTextUtils.isBoolean(result)) {// 接口返回的Data字段类型是Boolean
                    Boolean aBoolean = Boolean.valueOf(result);
                    mRootView.getTipsTextView().setText("Boolean --> " + aBoolean);
                } else {// 接口返回的Data字段是其他，默认都是String
                    mRootView.getTipsTextView().setText("String --> " + result);
                }
            }

            @Override
            public void onFail(TestStringRequest req, int code, String msg) {
                mRootView.showToast("[ErrorCode = " + code + "]" + msg);
                mRootView.getTipsTextView().setText("[ErrorCode = " + code + "]" + msg);
            }

            @Override
            public void onFinish() {
                mRootView.hideLoading();
            }
        }).get();
    }

    public void login(String account, String pwd, int userType) {
        LoginRequest loginRequest = new LoginRequest(mRootView.getActivity());
        loginRequest.setUserType(String.valueOf(userType));
        loginRequest.setAccount(account);
        try {
            loginRequest.setPassword(AESUtils.Encrypt(pwd, BuildConfig.SSO_KEY_V3));
        } catch (Exception e) {
            loginRequest.setPassword(pwd);
        }
        loginRequest.setHttpCallback(new HttpCallback<LoginRequest, LoginResponse>() {
            @Override
            public void onStart() {
                mRootView.showLoading("登陆中...");
            }

            @Override
            public void onResult(LoginRequest req, LoginResponse loginResponse) {
                // TODO 登录成功后，读取用户详细信息
                LoginInfoBean loginInfoBean = new LoginInfoBean(account, pwd);
                SAccountUtil.syncTokenInfo(loginResponse);
                findUserInfo(loginInfoBean, loginResponse, userType);
            }

            @Override
            public void onFail(LoginRequest req, int code, String msg) {
                mRootView.onLoginResult(false, msg);
            }

            @Override
            public void onFinish() {
            }
        }).post();
    }

    /**
     * 获取用户详细信息
     */
    private void findUserInfo(LoginInfoBean loginInfoBean, LoginResponse loginResponse, int userType) {
        UserInfoRequest request = new UserInfoRequest(mRootView.getActivity(), userType);
        request.setHttpCallback(new HttpCallback<UserInfoRequest, UserInfoResponse>() {
            @Override
            public void onStart() {
            }

            @Override
            public void onResult(UserInfoRequest req, UserInfoResponse userInfoResponse) {
                // TODO 缓存当前登录信息
                mModel.cacheLoginInfo(loginInfoBean, loginResponse, userInfoResponse);
                // mModel.cacheLoginInfo(account, pwd, loginResponse);
                // 告知登录成功
                mRootView.getTipsTextView().setText(String.format("登录成功：%s", JSON.toJSONString(loginResponse)));
                mRootView.onLoginResult(true, "");
            }

            @Override
            public void onFail(UserInfoRequest req, int code, String msg) {
                mRootView.onLoginResult(false, msg);
            }

            @Override
            public void onFinish() {
                mRootView.hideLoading();
            }
        }).get();
    }
}
