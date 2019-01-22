package cn.sxw.android.lib.mvp.model.empty;

import java.util.List;

import cn.sxw.android.base.bean.BlankBean;
import cn.sxw.android.base.bean.LoginInfoBean;
import cn.sxw.android.base.bean.user.UserInfoResponse;
import cn.sxw.android.base.mvp.IModel;
import cn.sxw.android.base.okhttp.response.LoginResponse;
import cn.sxw.android.lib.mvp.model.request.TestListRequest;

public interface IEmptyModel extends IModel {
    /**
     * 缓存列表数据
     */
    void cacheListData(TestListRequest request, List<BlankBean> list);

    void cacheLoginInfo(LoginInfoBean loginInfoBean, LoginResponse loginResponse, UserInfoResponse userInfoResponse);
}
