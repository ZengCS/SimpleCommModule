package cn.sxw.android.base.account;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;

import cn.sxw.android.base.bean.user.AreaDTO;
import cn.sxw.android.base.bean.user.ClassComplexDTO;
import cn.sxw.android.base.bean.user.ClassSimpleDTO;
import cn.sxw.android.base.bean.user.CourseComplexDTO;
import cn.sxw.android.base.bean.user.GradeComplexDTO;
import cn.sxw.android.base.bean.user.SubjectSimpleDTO;
import cn.sxw.android.base.bean.user.TermComplexDTO;
import cn.sxw.android.base.bean.user.UserInfoResponse;
import cn.sxw.android.base.bean.user.UserSimpleDTO;
import cn.sxw.android.base.cache.SharedPreferencesUtil;
import cn.sxw.android.base.net.bean.LocalTokenCache;
import cn.sxw.android.base.okhttp.HttpManager;
import cn.sxw.android.base.okhttp.response.LoginResponse;
import cn.sxw.android.base.utils.LogUtil;

/**
 * 学生登录信息管理工具
 *
 * @author ZengCS
 * @since 2016年7月21日10:20:17
 */
public class SAccountUtil {
    private static UserInfoResponse userInfoResponse;
    private static boolean isUpdated = true;// 是否有更新
    public static final String KEY_LOGIN_CACHE_INFO = "KEY_LOGIN_CACHE_INFO";

    /**
     * 缓存登录信息
     *
     * @param userInfoResponse
     */
    public static void saveLoginInfo(UserInfoResponse userInfoResponse) {
        String accountJson = JSON.toJSONString(userInfoResponse);
        SharedPreferencesUtil.setParam(AccountKeys.IS_LOGIN, true);
        SharedPreferencesUtil.setParam(AccountKeys.ACCOUNT_INFO, accountJson);
        isUpdated = true;
    }

    /**
     * 用户是否已登录
     */
    public static boolean hasLogin() {
        UserInfoResponse loginedAccount = getLoginedAccount();
        if (loginedAccount == null) {
            return false;
        }
        String localCacheToken = LocalTokenCache.getLocalCacheToken();
        if (TextUtils.isEmpty(localCacheToken)) {
            return false;
        }
        return true;
    }

    /**
     * 获取当前已登录的用户信息
     *
     * @return 已登录的用户对象
     */
    public static UserInfoResponse getLoginedAccount() {
        if (userInfoResponse != null) {
            return userInfoResponse;
        }

        boolean isLogin = (boolean) SharedPreferencesUtil.getParam(AccountKeys.IS_LOGIN, false);
        if (!isLogin) {// 尚未登录
            userInfoResponse = null;
        }

        String accountJson = (String) SharedPreferencesUtil.getParam(AccountKeys.ACCOUNT_INFO, "");
        if (!TextUtils.isEmpty(accountJson)) {// 已登录
            try {
                userInfoResponse = JSON.parseObject(accountJson, UserInfoResponse.class);
            } catch (Exception e) {
                userInfoResponse = null;
            }
        } else {
            userInfoResponse = null;
        }

        return userInfoResponse;
    }

    // ------------------------------ 公用方法 ------------------------------

    /**
     * 获取学生姓名
     */
    public static String getStudentName() {
        getLoginedAccount();
        if (userInfoResponse != null) {
            UserSimpleDTO userSimpleDTO = userInfoResponse.getUserSimpleDTO();
            if (userSimpleDTO != null)
                return userSimpleDTO.getName();
        }
        return "学生姓名-未知";
    }

    /**
     * 获取头像地址
     */
    public static String getPortraitPath() {
        getLoginedAccount();
        if (userInfoResponse != null) {
            UserSimpleDTO userSimpleDTO = userInfoResponse.getUserSimpleDTO();
            if (userSimpleDTO != null)
                return userSimpleDTO.getPortraitUrl();
        }
        return "";
    }

    /**
     * 获取用户ID
     */
    public static String getUserId() {
        getLoginedAccount();
        if (userInfoResponse != null) {
            if (userInfoResponse != null) {
                UserSimpleDTO userSimpleDTO = userInfoResponse.getUserSimpleDTO();
                if (userSimpleDTO != null)
                    return userSimpleDTO.getId();
            }
        }
        return "";
    }

    /**
     * 获取工号
     */
    public static String getTeaching() {
        getLoginedAccount();
        if (userInfoResponse != null) {
            UserSimpleDTO userSimpleDTO = userInfoResponse.getUserSimpleDTO();
            if (userSimpleDTO != null)
                return userSimpleDTO.getTeaching();
        }
        return "";
    }

    /**
     * 获取身份证号
     */
    public static String getIdNumber() {
        getLoginedAccount();
        if (userInfoResponse != null) {
            UserSimpleDTO userSimpleDTO = userInfoResponse.getUserSimpleDTO();
            if (userSimpleDTO != null)
                return userSimpleDTO.getIdnumber();
        }
        return "身份证号-未知";
    }

    /**
     * 获取电话号码
     */
    public static String getPhoneNum() {
        getLoginedAccount();
        if (userInfoResponse != null) {
            UserSimpleDTO userSimpleDTO = userInfoResponse.getUserSimpleDTO();
            if (userSimpleDTO != null)
                return userSimpleDTO.getPhoneNumber();
        }
        return "电话号码-未知";
    }

    /**
     * 获取区域ID
     */
    public static String getCityId() {
        getLoginedAccount();
        if (userInfoResponse != null) {
            UserSimpleDTO userSimpleDTO = userInfoResponse.getUserSimpleDTO();
            if (userSimpleDTO != null)
                return userSimpleDTO.getCityId();
        }
        return "000000";
    }

    /**
     * 获取区域ID
     */
    public static String getRegionId() {
        getLoginedAccount();
        if (userInfoResponse != null) {
            UserSimpleDTO userSimpleDTO = userInfoResponse.getUserSimpleDTO();
            if (userSimpleDTO != null)
                return userSimpleDTO.getRegionId();
        }
        return "000000";
    }

    /**
     * 获取区域ID
     */
    public static String getAreaId() {
        getLoginedAccount();
        if (userInfoResponse != null) {
            UserSimpleDTO userSimpleDTO = userInfoResponse.getUserSimpleDTO();
            if (userSimpleDTO != null)
                return userSimpleDTO.getAreaId();
        }
        return "";
    }

    /**
     * 获取学校名称
     */
    public static String getSchoolName() {
        getLoginedAccount();
        if (userInfoResponse != null) {
            AreaDTO areaDTO = userInfoResponse.getAreaDTO();
            if (areaDTO != null)
                return areaDTO.getName();
        }
        return "学校-未知";
    }

    /**
     * 获取学段ID
     */
    public static String getPeriodId() {
        getLoginedAccount();
        if (userInfoResponse != null) {
            GradeComplexDTO gradeComplexDTO = getGradeComplexDTO(null);
            if (gradeComplexDTO != null)
                return gradeComplexDTO.getPeriodId();
        }
        return "";
    }

    /**
     * 获取学段名称
     */
    public static String getPeriodName() {
        getLoginedAccount();
        if (userInfoResponse != null) {
            GradeComplexDTO gradeComplexDTO = getGradeComplexDTO(null);
            if (gradeComplexDTO != null)
                return gradeComplexDTO.getPeriodName();
        }
        return "学段-未知";
    }

    /**
     * 获取学段名称
     */
    public static String getPeriodNameByClassId(String classId) {
        getLoginedAccount();
        if (userInfoResponse != null) {
            GradeComplexDTO gradeComplexDTO = getGradeComplexDTO(classId);
            if (gradeComplexDTO != null)
                return gradeComplexDTO.getPeriodName();
        }
        return "学段-未知";
    }

    /**
     * 获取年级ID
     */
    public static String getGradeId() {
        getLoginedAccount();
        if (userInfoResponse != null) {
            GradeComplexDTO gradeComplexDTO = getGradeComplexDTO(null);
            if (gradeComplexDTO != null)
                return gradeComplexDTO.getGradeId();
        }
        return "";
    }

    public static String getGradeIdByClassId(String classId) {
        getLoginedAccount();
        if (userInfoResponse != null) {
            GradeComplexDTO gradeComplexDTO = getGradeComplexDTO(classId);
            if (gradeComplexDTO != null)
                return gradeComplexDTO.getGradeId();
        }
        return "";
    }

    /**
     * 获取年级名称
     */
    public static String getGradeName() {
        getLoginedAccount();
        if (userInfoResponse != null) {
            GradeComplexDTO gradeComplexDTO = getGradeComplexDTO(null);
            if (gradeComplexDTO != null)
                return gradeComplexDTO.getGradeName();
        }
        return "学段-未知";
    }

    /**
     * 获取年级名称
     */
    public static String getGradeNameByClassId(String classId) {
        getLoginedAccount();
        if (userInfoResponse != null) {
            GradeComplexDTO gradeComplexDTO = getGradeComplexDTO(classId);
            if (gradeComplexDTO != null)
                return gradeComplexDTO.getGradeName();
        }
        return "学段-未知";
    }


    /**
     * 获取学期ID
     */
    public static String getTermId() {
        getLoginedAccount();
        if (userInfoResponse != null) {
            TermComplexDTO termComplexDTO = getTermComplexDTO();
            if (termComplexDTO != null)
                return termComplexDTO.getId();
        }
        return "";
    }

    /**
     * 获取学年名称
     */
    public static String getTermYearName() {
        getLoginedAccount();
        if (userInfoResponse != null) {
            TermComplexDTO termComplexDTO = getTermComplexDTO();
            if (termComplexDTO != null)
                return termComplexDTO.getTermYearName();
        }
        return "学年-未知";
    }

    /**
     * 获取学年ID
     */
    public static String getTermYearId() {
        getLoginedAccount();
        if (userInfoResponse != null) {
            TermComplexDTO termComplexDTO = getTermComplexDTO();
            if (termComplexDTO != null)
                return termComplexDTO.getTermYearId();
        }
        return "";
    }

    /**
     * 获取科目ID
     */
    public static String getSubjectId() {
        getLoginedAccount();
        if (userInfoResponse != null) {
            SubjectSimpleDTO subjectSimpleDTO = getSubjectSimpleDTO(null);
            if (subjectSimpleDTO != null)
                return subjectSimpleDTO.getId();
        }
        return "";
    }

    /**
     * 获取科目ID
     */
    public static String getSubjectIdByClassId(String classId) {
        getLoginedAccount();
        if (userInfoResponse != null) {
            SubjectSimpleDTO subjectSimpleDTO = getSubjectSimpleDTO(classId);
            if (subjectSimpleDTO != null)
                return subjectSimpleDTO.getId();
        }
        return "";
    }

    /**
     * 获取科目名称
     */
    public static String getSubjectName() {
        getLoginedAccount();
        if (userInfoResponse != null) {
            SubjectSimpleDTO subjectSimpleDTO = getSubjectSimpleDTO(null);
            if (subjectSimpleDTO != null)
                return subjectSimpleDTO.getName();
        }
        return "科目-未知";
    }

    /**
     * 获取科目名称
     */
    public static String getSubjectNameByClassId(String classId) {
        getLoginedAccount();
        if (userInfoResponse != null) {
            SubjectSimpleDTO subjectSimpleDTO = getSubjectSimpleDTO(classId);
            if (subjectSimpleDTO != null)
                return subjectSimpleDTO.getName();
        }
        return "科目-未知";
    }

    /**
     * 获取班级名称
     */
    public static String getClassName() {
        ClassSimpleDTO classSimpleDTO = getClassSimpleDTO();

        if (classSimpleDTO == null)
            return "班级-未知";
        else
            return classSimpleDTO.getName();
    }

    /**
     * 获取班级ID
     */
    public static String getClassId() {
        ClassSimpleDTO classSimpleDTO = getClassSimpleDTO();
        if (classSimpleDTO == null)
            return "";
        else
            return classSimpleDTO.getId();
    }

    /**
     * 获取班级列表
     */
    public static ClassSimpleDTO getClassSimpleDTO() {
        getLoginedAccount();
        if (userInfoResponse != null) {
            try {
                return userInfoResponse.getClassComplexDTO().getClassSimpleDTO();
            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.e(e);
                return null;
            }
        }
        return null;
    }

    // ------------------------------ 内部方法 ------------------------------
    private static SubjectSimpleDTO getSubjectSimpleDTO(String classId) {
        try {
            if (TextUtils.isEmpty(classId)) {
                return userInfoResponse.getCourseComplexDTOS().get(0).getSubjectSimpleDTO();
            } else {
                for (CourseComplexDTO courseComplexDTO : userInfoResponse.getCourseComplexDTOS()) {
                    for (ClassComplexDTO classComplexDTO : courseComplexDTO.getClassComplexDTOS()) {
                        if (classId.equals(classComplexDTO.getClassSimpleDTO().getId())) {
                            return courseComplexDTO.getSubjectSimpleDTO();
                        }
                    }
                }
            }

        } catch (Exception e) {
            return null;
        }
        return null;
    }

    private static TermComplexDTO getTermComplexDTO() {
        try {
            return userInfoResponse.getCourseComplexDTOS().get(0).getTermComplexDTO();
        } catch (Exception e) {
            return null;
        }
    }

    private static GradeComplexDTO getGradeComplexDTO(String classId) {
        try {
            if (TextUtils.isEmpty(classId)) {
                return userInfoResponse.getCourseComplexDTOS().get(0).getClassComplexDTOS().get(0).getGradeComplexDTO();
            } else {
                for (CourseComplexDTO courseComplexDTO : userInfoResponse.getCourseComplexDTOS()) {
                    for (ClassComplexDTO classComplexDTO : courseComplexDTO.getClassComplexDTOS()) {
                        if (classId.equals(classComplexDTO.getClassSimpleDTO().getId())) {
                            return classComplexDTO.getGradeComplexDTO();
                        }
                    }
                }
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }
    // ------------------------------ 操作 ------------------------------

    /**
     * 执行注销
     */
    public static void logout() {
        // 清空用户信息缓存
        SharedPreferencesUtil.setParam(AccountKeys.IS_LOGIN, false);
        SharedPreferencesUtil.setParam(AccountKeys.ACCOUNT_INFO, "");
        // 清空Token缓存
        LocalTokenCache.cleanTokenCache();
        isUpdated = true;
        userInfoResponse = null;
    }

    public static void clearCacheLoginInfo() {
        SharedPreferencesUtil.setParam(KEY_LOGIN_CACHE_INFO, "");
    }

    /**
     * 更新头像地址
     *
     * @param url 新的头像地址
     */
    public static void updateAvatar(String url) {
        if (userInfoResponse != null && userInfoResponse.getUserSimpleDTO() != null) {
            userInfoResponse.getUserSimpleDTO().setPortraitUrl(url);
            String accountJson = JSON.toJSONString(userInfoResponse);
            SharedPreferencesUtil.setParam(AccountKeys.IS_LOGIN, true);
            SharedPreferencesUtil.setParam(AccountKeys.ACCOUNT_INFO, accountJson);
            isUpdated = true;
        }
    }

    /**
     * 移除用户信息
     */
    public static void removeAccount() {
        userInfoResponse = null;
        isUpdated = true;
    }

    public static boolean isUpdated() {
        return isUpdated;
    }

    public static void setUpdated(boolean isUpdated) {
        SAccountUtil.isUpdated = isUpdated;
    }

    public static void saveOfflineLogin(boolean offline) {
        SharedPreferencesUtil.setParam("IS_OFFLINE_LOGIN", offline);
    }

    public static boolean isOfflineLogin() {
        return SharedPreferencesUtil.getBoolean("IS_OFFLINE_LOGIN", false);
    }

    /**
     * 同步Token信息
     */
    public static void syncTokenInfo(LoginResponse loginResponse) {
        // 缓存TOKEN
        LocalTokenCache.setLocalCacheToken(loginResponse.getToken());
        LocalTokenCache.setLocalCacheRefreshToken(loginResponse.getRefreshToken());
        // 同步TOKEN
        HttpManager.getInstance().setTokenHeader(loginResponse.getToken());
        HttpManager.getInstance().setRefreshToken(loginResponse.getRefreshToken());
    }

    /**
     * 检测版本
     */
    public static String getCheckVersionInfo() {
        return SharedPreferencesUtil.getString("CHECK_VERSION_RESULT", "");
    }

    public static void saveCheckVersionInfo(String versionInfo) {
        SharedPreferencesUtil.setParam("CHECK_VERSION_RESULT", versionInfo);
    }

    public static void clearVersionInfo() {
        SharedPreferencesUtil.setParam("CHECK_VERSION_RESULT", "");
    }
}
