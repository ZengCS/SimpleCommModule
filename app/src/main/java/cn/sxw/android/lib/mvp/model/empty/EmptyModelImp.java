package cn.sxw.android.lib.mvp.model.empty;

import com.alibaba.fastjson.JSON;

import java.util.List;

import javax.inject.Inject;

import cn.sxw.android.base.account.SAccountUtil;
import cn.sxw.android.base.bean.BlankBean;
import cn.sxw.android.base.bean.LoginInfoBean;
import cn.sxw.android.base.bean.SSODetailBean;
import cn.sxw.android.base.bean.user.UserInfoResponse;
import cn.sxw.android.base.mvp.BaseModel;
import cn.sxw.android.base.net.ApiHelper;
import cn.sxw.android.base.okhttp.response.LoginResponse;
import cn.sxw.android.base.prefer.PreferencesHelper;
import cn.sxw.android.base.utils.LogUtil;
import cn.sxw.android.base.utils.SxwMobileSSOUtil;
import cn.sxw.android.lib.BuildConfig;
import cn.sxw.android.lib.mvp.model.request.TestListRequest;

public class EmptyModelImp extends BaseModel implements IEmptyModel {

    private PreferencesHelper preferencesHelper;

    private ApiHelper apiHelper;

    @Inject
    public EmptyModelImp(PreferencesHelper preferencesHelper, ApiHelper apiHelper) {
        this.preferencesHelper = preferencesHelper;
        this.apiHelper = apiHelper;
    }

    @Override
    public void cacheLoginInfo(LoginInfoBean loginInfoBean, LoginResponse loginResponse, UserInfoResponse userInfoResponse) {
        // 1.同步Token缓存
        SAccountUtil.syncTokenInfo(loginResponse);
        // 2.把登录信息缓存到SP中,可通过SAccountUtil读取
        SAccountUtil.saveLoginInfo(userInfoResponse);
        // 3.************** 保存单点登录信息 **************
        SSODetailBean ssoDetailBean = new SSODetailBean();
        // 添加Token对象
        ssoDetailBean.setTokenBean(loginResponse);
        // 添加用户详细信息
        ssoDetailBean.setUserInfoResponse(userInfoResponse);
        // 添加账号密码缓存
        ssoDetailBean.setLoginInfo(loginInfoBean);
        // 保存到文件
        boolean success = SxwMobileSSOUtil.saveOSSInfo(ssoDetailBean);
        if (success) {
            LogUtil.d("保存单点登录信息成功");
            if (BuildConfig.DEBUG) {
                SSODetailBean detailBean = SxwMobileSSOUtil.getSSODetailBean(true);
                LogUtil.d(JSON.toJSONString(detailBean));
                if (detailBean != null) {
                    LoginResponse tokenBean = detailBean.getTokenBean();
                    if (tokenBean != null) {
                        LogUtil.d("Token = " + tokenBean.getToken());
                        LogUtil.d("RefreshToken = " + tokenBean.getRefreshToken());
                    }
                    UserInfoResponse userInfo = detailBean.getUserInfoResponse();
                    if (userInfo != null) {
                        LogUtil.d("userInfo = " + JSON.toJSONString(userInfo));
                    }
                    LoginInfoBean loginInfo = detailBean.getLoginInfo();
                    if (loginInfo != null) {
                        LogUtil.d("Account = " + loginInfo.getAccount());
                        LogUtil.d("Password = " + loginInfo.getPwd());
                    }
                }
            }
        }

    }

    @Override
    public void cacheListData(TestListRequest request, List<BlankBean> list) {
        // TODO 根据自己的业务处理
    }
}
