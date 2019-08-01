package cn.sxw.android.lib.mvp.ui.activity;

import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import cn.sxw.android.base.account.SAccountUtil;
import cn.sxw.android.base.bean.LoginInfoBean;
import cn.sxw.android.base.di.component.AppComponent;
import cn.sxw.android.base.okhttp.response.LoginResponse;
import cn.sxw.android.base.utils.SxwMobileSSOUtil;
import cn.sxw.android.lib.R;
import cn.sxw.android.lib.mvp.base.BaseActivityAdv;

/**
 * Created by ZengCS on 2019/8/1.
 * E-mail:zcs@sxw.cn
 * Add:成都市天府软件园E3-3F
 */
@EActivity(R.layout.activity_user_profile)
public class UserProfileActivity extends BaseActivityAdv {
    @ViewById(R.id.id_tv_user_profile)
    TextView textView;

    @AfterViews
    void init() {
        setTitle("个人中心");
        StringBuilder sb = new StringBuilder();
        String userType = SAccountUtil.getUserType();

        // 登录信息
        LoginInfoBean loginInfoBean = SxwMobileSSOUtil.getLoginInfoBean();
        if (loginInfoBean != null) {
            sb.append("账号：");
            sb.append(loginInfoBean.getAccount());
            sb.append("\n密码：");
            sb.append(loginInfoBean.getPwd());
            sb.append("\n-----------------------------------------\n");
        }
        // TOKEN信息
        LoginResponse loginResponse = SxwMobileSSOUtil.getLoginResponse();
        if (loginResponse != null) {
            sb.append("Token：");
            sb.append(loginResponse.getToken());
            sb.append("\nRefreshToken：");
            sb.append(loginResponse.getRefreshToken());
            sb.append("\n-----------------------------------------\n");
        }
        // 基础信息
        sb.append("用户类型：");
        sb.append(userType);
        sb.append("\n用户名：");
        sb.append(SAccountUtil.getName());
        sb.append("\n用户ID：");
        sb.append(SAccountUtil.getUserId());
        sb.append("\n手机号：");
        sb.append(SAccountUtil.getPhoneNum());
        sb.append("\n身份证号：");
        sb.append(SAccountUtil.getIdNumber());
        sb.append("\n头像地址：");
        sb.append(SAccountUtil.getPortraitUrl());
        sb.append("\n工号：");
        sb.append(SAccountUtil.getTeaching());
        sb.append("\n生学号：");
        sb.append(SAccountUtil.getSxwNumber());
        sb.append("\n学校名称：");
        sb.append(SAccountUtil.getSchoolName());
        sb.append("\n学校ID：");
        sb.append(SAccountUtil.getAreaId());
        sb.append("\n-----------------------------------------\n");
        switch (userType) {
            case "STUDENT":
                sb.append("班级名称：");
                sb.append(SAccountUtil.getClassName());
                sb.append("\n班级ID：");
                sb.append(SAccountUtil.getClassId());
                sb.append("\n年级名称：");
                sb.append(SAccountUtil.getGradeName());
                sb.append("\n年级ID：");
                sb.append(SAccountUtil.getGradeId());
                sb.append("\n学段名称：");
                sb.append(SAccountUtil.getPeriodName());
                sb.append("\n学段ID：");
                sb.append(SAccountUtil.getPeriodId());
                sb.append("\n年级级别名称：");
                sb.append(SAccountUtil.getGradeLevelName());
                sb.append("\n年级级别ID：");
                sb.append(SAccountUtil.getGradeLevelId());
                break;
            case "TEACHER":
//                sb.append("---->【getCourseComplexDTOList】：");
//                sb.append(JSON.toJSONString(SAccountUtil.getCourseComplexDTOList()));
                sb.append("---->【班级名称列表】：");
                sb.append(JSON.toJSONString(SAccountUtil.getClassNameList()));
                sb.append("\n\n---->【班级列表】：");
                sb.append(JSON.toJSONString(SAccountUtil.getClassList()));
                sb.append("\n\n---->【学科列表】：");
                sb.append(JSON.toJSONString(SAccountUtil.getSubjectList()));
                sb.append("\n\n---->【学期列表】：");
                sb.append(JSON.toJSONString(SAccountUtil.getTermList()));
                break;
            case "PARENT":
                sb.append("---->【默认孩子-姓名】：");
                sb.append(SAccountUtil.getDefaultChildName());
                sb.append("\n---->【默认孩子-ID】：");
                sb.append(SAccountUtil.getDefaultChildId());
                sb.append("\n\n---->【全部孩子姓名列表】：");
                sb.append(JSON.toJSONString(SAccountUtil.getChildNameList()));
                sb.append("\n\n---->【孩子列表】：");
                sb.append(JSON.toJSONString(SAccountUtil.getChildList()));
                sb.append("\n\n---->【默认孩子】：");
                sb.append(JSON.toJSONString(SAccountUtil.getDefaultChildUserSimpleDTO()));
                break;
        }

        textView.setText(sb.toString());
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    protected void getDataFromNet() {

    }
}
