package cn.sxw.android.base.account;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;

import java.util.List;

import cn.sxw.android.base.bean.LoginInfoBean;
import cn.sxw.android.base.bean.SSODetailBean;
import cn.sxw.android.base.bean.user.AreaDTO;
import cn.sxw.android.base.bean.user.ChildDTO;
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
import cn.sxw.android.base.utils.JListKit;
import cn.sxw.android.base.utils.LogUtil;
import cn.sxw.android.base.utils.SxwMobileSSOUtil;

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
     * @param newInfo
     */
    public static void saveLoginInfo(UserInfoResponse newInfo) {
        userInfoResponse = newInfo;
        String accountJson = JSON.toJSONString(newInfo);
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
    // **************** UserSimpleDTO 1 ****************
    public static String getUserType() {
        getLoginedAccount();
        if (userInfoResponse != null) {
            UserSimpleDTO userSimpleDTO = userInfoResponse.getUserSimpleDTO();
            if (userSimpleDTO != null)
                return userSimpleDTO.getUserType();
        }
        return "";
    }

    /**
     * 获取学生姓名
     */
    public static String getStudentName() {
        return getName();
    }

    public static String getTeacherName() {
        return getName();
    }

    public static String getParentName() {
        return getName();
    }

    /**
     * 获取用户姓名
     */
    public static String getName() {
        getLoginedAccount();
        if (userInfoResponse != null) {
            UserSimpleDTO userSimpleDTO = userInfoResponse.getUserSimpleDTO();
            if (userSimpleDTO != null)
                return userSimpleDTO.getName();
        }
        return "未知";
    }

    /**
     * 获取生学号
     */
    public static String getSxwNumber() {
        getLoginedAccount();
        if (userInfoResponse != null) {
            UserSimpleDTO userSimpleDTO = userInfoResponse.getUserSimpleDTO();
            if (userSimpleDTO != null)
                return userSimpleDTO.getSxwNumber();
        }
        return "";
    }

    /**
     * 获取头像地址
     */
    public static String getPortraitPath() {
        return getPortraitUrl();
    }

    /**
     * 获取头像地址
     */
    public static String getPortraitUrl() {
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
            UserSimpleDTO userSimpleDTO = userInfoResponse.getUserSimpleDTO();
            if (userSimpleDTO != null)
                return userSimpleDTO.getId();
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
     * 获取省份ID
     */
    public static String getProvinceId() {
        getLoginedAccount();
        if (userInfoResponse != null) {
            UserSimpleDTO userSimpleDTO = userInfoResponse.getUserSimpleDTO();
            if (userSimpleDTO != null)
                return userSimpleDTO.getProvinceId();
        }
        return "000000";
    }

    /**
     * 获取城市ID
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
    // **************** UserSimpleDTO 2 ****************

    // **************** AreaDTO 1 ****************

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
    // **************** AreaDTO 2 ****************


    // **************** GradeComplexDTO 1 ****************

    /**
     * 获取学段名称
     */
    public static String getPeriodName() {
        // GradeComplexDTO gradeComplexDTO = getGradeComplexDTO();
        GradeComplexDTO gradeComplexDTO = getAdministrationGradeComplexDTO();
        if (gradeComplexDTO != null) {
            return gradeComplexDTO.getPeriodName();
        }
        return "";
    }

    public static String getPeriodId() {
        // GradeComplexDTO gradeComplexDTO = getGradeComplexDTO();
        GradeComplexDTO gradeComplexDTO = getAdministrationGradeComplexDTO();
        if (gradeComplexDTO != null) {
            return gradeComplexDTO.getPeriodId();
        }
        return "";
    }
    // **************** GradeComplexDTO 2 ****************

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
        GradeComplexDTO gradeComplexDTO = getAdministrationGradeComplexDTO();
        // GradeComplexDTO gradeComplexDTO = getGradeComplexDTO();
        if (gradeComplexDTO != null) {
            return gradeComplexDTO.getGradeName();
        }
        return "";
    }

    public static String getGradeId() {
        // GradeComplexDTO gradeComplexDTO = getGradeComplexDTO();
        GradeComplexDTO gradeComplexDTO = getAdministrationGradeComplexDTO();
        if (gradeComplexDTO != null) {
            return gradeComplexDTO.getGradeId();
        }
        return "";
    }

    private static GradeComplexDTO getGradeComplexDTO() {
        getLoginedAccount();
        if (userInfoResponse != null) {
            ClassComplexDTO classComplexDTO = userInfoResponse.getClassComplexDTO();
            if (classComplexDTO != null) {
                return classComplexDTO.getGradeComplexDTO();
            }
        }
        return null;
    }

    /**
     * 获取年级名称
     */
    public static String getGradeLevelName() {
        // GradeComplexDTO gradeComplexDTO = getGradeComplexDTO();
        GradeComplexDTO gradeComplexDTO = getAdministrationGradeComplexDTO();
        if (gradeComplexDTO != null) {
            return gradeComplexDTO.getGradeLevelName();
        }
        return "";
    }

    public static String getGradeLevelId() {
        // GradeComplexDTO gradeComplexDTO = getGradeComplexDTO();
        GradeComplexDTO gradeComplexDTO = getAdministrationGradeComplexDTO();
        if (gradeComplexDTO != null) {
            return gradeComplexDTO.getGradeLevelId();
        }
        return "";
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
    public static List<SubjectSimpleDTO> getSubjectList() {
        List<CourseComplexDTO> courseComplexDTOList = getCourseComplexDTOList();
        if (JListKit.isNotEmpty(courseComplexDTOList)) {
            List<SubjectSimpleDTO> subjectSimpleDTOList = JListKit.newArrayList();
            for (CourseComplexDTO dto : courseComplexDTOList) {
                subjectSimpleDTOList.add(dto.getSubjectSimpleDTO());
            }
            return subjectSimpleDTOList;
        }
        return null;
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
    public static List<String> getSubjectNameList() {
        List<CourseComplexDTO> courseComplexDTOList = getCourseComplexDTOList();
        return null;
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
     * 获取班级名称-行政班级
     */
    public static String getClassName() {
        ClassSimpleDTO classSimpleDTO = getClassSimpleDTO();

        if (classSimpleDTO == null)
            return getAdministrationClassName();
        else
            return classSimpleDTO.getName();
    }

    /**
     * @return 学生的行政班级名称
     */
    private static String getAdministrationClassName() {
        ClassSimpleDTO classSimpleDTO = getAdministrationClassSimpleDTO();
        if (classSimpleDTO != null)
            return classSimpleDTO.getName();
        return "";
    }

    /**
     * @return 学生行政班级ID
     */
    private static String getAdministrationClassId() {
        ClassSimpleDTO classSimpleDTO = getAdministrationClassSimpleDTO();
        if (classSimpleDTO != null)
            return classSimpleDTO.getId();
        return "";
    }

    /**
     * @return 行政班级详情
     */
    public static ClassSimpleDTO getAdministrationClassSimpleDTO() {
        ClassComplexDTO dto = getAdministrationClassComplexDTO();
        if (dto != null) {
            return dto.getClassSimpleDTO();
        }
        return null;
    }

    /**
     * @return 行政班级科目信息
     */
    public static GradeComplexDTO getAdministrationGradeComplexDTO() {
        ClassComplexDTO dto = getAdministrationClassComplexDTO();
        if (dto != null) {
            return dto.getGradeComplexDTO();
        }
        return null;
    }

    /**
     * @return 行政班级对象
     */
    public static ClassComplexDTO getAdministrationClassComplexDTO() {
        List<ClassComplexDTO> studentClassList = getStudentClassList();
        if (!JListKit.isEmpty(studentClassList)) {
            for (ClassComplexDTO dto : studentClassList) {
                ClassSimpleDTO classSimpleDTO = dto.getClassSimpleDTO();
                if (classSimpleDTO != null && classSimpleDTO.isAdministrationClass())
                    return dto;
            }
        }
        return null;
    }

    /**
     * @return 学生的教学班级列表
     */
    public static List<ClassComplexDTO> getStudentTeachClassList() {
        List<ClassComplexDTO> studentClassList = getStudentClassList();
        if (JListKit.isNotEmpty(studentClassList)) {
            List<ClassComplexDTO> teachList = JListKit.newArrayList();
            for (ClassComplexDTO dto : studentClassList) {
                ClassSimpleDTO classSimpleDTO = dto.getClassSimpleDTO();
                if (classSimpleDTO != null && !classSimpleDTO.isAdministrationClass()) {
                    teachList.add(dto);
                }
            }
            return teachList;
        }
        return JListKit.newArrayList();
    }

    /**
     * 是否是我在班级-学生&教师均可用
     *
     * @param classId 班级ID
     * @return true:是 false:否
     */
    public static boolean isMyClass(@NonNull String classId) {
        if (TextUtils.isEmpty(classId))
            return false;
        // 判断是不是学生
        List<ClassComplexDTO> studentClassList = getStudentClassList();
        if (JListKit.isNotEmpty(studentClassList)) {
            for (ClassComplexDTO dto : studentClassList) {
                ClassSimpleDTO classSimpleDTO = dto.getClassSimpleDTO();
                if (classSimpleDTO != null) {
                    if (classId.equals(classSimpleDTO.getId()))
                        return true;
                }
            }
        }
        // 判断是不是老师
        List<ClassComplexDTO> classList = getClassList();
        if (JListKit.isNotEmpty(classList)) {
            for (ClassComplexDTO dto : classList) {
                ClassSimpleDTO classSimpleDTO = dto.getClassSimpleDTO();
                if (classSimpleDTO != null) {
                    if (classId.equals(classSimpleDTO.getId()))
                        return true;
                }
            }
        }
        return false;
    }

    /**
     * 根据班级id获取班级名称-学生&教师
     *
     * @param classId 班级id
     * @return 班级名称
     */
    public static String getClassNameById(@NonNull String classId) {
        if (TextUtils.isEmpty(classId))
            return "";
        ClassComplexDTO classDTOById = getClassDTOById(classId);
        if (classDTOById != null) {
            ClassSimpleDTO classSimpleDTO = classDTOById.getClassSimpleDTO();
            if (classSimpleDTO != null) {
                return classSimpleDTO.getName();
            }
        }
        return "";
    }

    /**
     * 根据班级ID获取班级详情-学生&教师
     *
     * @param classId 班级ID
     * @return 班级详情DTO
     */
    public static ClassComplexDTO getClassDTOById(@NonNull String classId) {
        if (TextUtils.isEmpty(classId))
            return null;
        List<ClassComplexDTO> classList = getClassList();
        if (JListKit.isNotEmpty(classList)) {
            for (ClassComplexDTO dto : classList) {
                ClassSimpleDTO classSimpleDTO = dto.getClassSimpleDTO();
                if (classSimpleDTO != null && classId.equals(classSimpleDTO.getId())) {
                    return dto;
                }
            }
        }
        List<ClassComplexDTO> studentClassList = getStudentClassList();
        if (JListKit.isNotEmpty(studentClassList)) {
            for (ClassComplexDTO dto : studentClassList) {
                ClassSimpleDTO classSimpleDTO = dto.getClassSimpleDTO();
                if (classSimpleDTO != null && classId.equals(classSimpleDTO.getId())) {
                    return dto;
                }
            }
        }

        return null;
    }

    /**
     * @return 学生的所有班级列表
     */
    public static List<ClassComplexDTO> getStudentClassList() {
        getLoginedAccount();
        if (userInfoResponse != null) {
            try {
                return userInfoResponse.getClassComplexDTOS();
            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.e(e);
                return null;
            }
        }
        return null;
    }

    /**
     * 获取班级ID
     */
    public static String getClassId() {
        ClassSimpleDTO classSimpleDTO = getClassSimpleDTO();
        if (classSimpleDTO == null)
            return getAdministrationClassId();
        else
            return classSimpleDTO.getId();
    }

    /**
     * 获取班级列表-针对教师
     */
    public static List<ClassComplexDTO> getClassList() {
        List<CourseComplexDTO> list = getCourseComplexDTOList();
        if (JListKit.isNotEmpty(list)) {
            List<ClassComplexDTO> classList = JListKit.newArrayList();
            for (CourseComplexDTO dto : list) {
                List<ClassComplexDTO> classComplexDTOS = dto.getClassComplexDTOS();
                if (JListKit.isNotEmpty(classComplexDTOS))
                    classList.addAll(classComplexDTOS);
            }
            return classList;
        }
        return null;
    }

    /**
     * 获取教师行政班级列表
     *
     * @return 行政班级列表
     */
    public static List<ClassComplexDTO> getTeacherAdministrationClassList() {
        List<ClassComplexDTO> classList = getClassList();
        if (JListKit.isNotEmpty(classList)) {
            List<ClassComplexDTO> tempList = JListKit.newArrayList();
            for (ClassComplexDTO dto : classList) {
                ClassSimpleDTO classSimpleDTO = dto.getClassSimpleDTO();
                if (classSimpleDTO != null && classSimpleDTO.isAdministrationClass()) {
                    tempList.add(dto);
                }
            }
            return tempList;
        }
        return JListKit.newArrayList();
    }

    /**
     * 获取教师教学班级列表
     *
     * @return 教学班级列表
     */
    public static List<ClassComplexDTO> getTeacherTeachClassList() {
        List<ClassComplexDTO> classList = getClassList();
        if (JListKit.isNotEmpty(classList)) {
            List<ClassComplexDTO> tempList = JListKit.newArrayList();
            for (ClassComplexDTO dto : classList) {
                ClassSimpleDTO classSimpleDTO = dto.getClassSimpleDTO();
                if (classSimpleDTO != null && !classSimpleDTO.isAdministrationClass()) {
                    tempList.add(dto);
                }
            }
            return tempList;
        }
        return JListKit.newArrayList();
    }


    public static List<TermComplexDTO> getTermList() {
        List<CourseComplexDTO> list = getCourseComplexDTOList();
        if (JListKit.isNotEmpty(list)) {
            List<TermComplexDTO> termList = JListKit.newArrayList();
            for (CourseComplexDTO dto : list) {
                TermComplexDTO termComplexDTO = dto.getTermComplexDTO();
                if (termComplexDTO != null)
                    termList.add(termComplexDTO);
            }
            return termList;
        }
        return null;
    }

    /**
     * 获取班级名称列表
     * 教师专用
     */
    public static List<String> getClassNameList() {
        List<ClassComplexDTO> classList = getClassList();
        if (JListKit.isNotEmpty(classList)) {
            List<String> nameList = JListKit.newArrayList();
            for (ClassComplexDTO dto : classList) {
                ClassSimpleDTO classSimpleDTO = dto.getClassSimpleDTO();
                if (classSimpleDTO != null) {
                    nameList.add(classSimpleDTO.getName());
                }
            }
            return nameList;
        }
        return null;
    }

    public static List<CourseComplexDTO> getCourseComplexDTOList() {
        getLoginedAccount();
        if (userInfoResponse != null) {
            return userInfoResponse.getCourseComplexDTOS();
        }
        return null;
    }

    /**
     * 获取班级信息
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
    // ***************************** 孩子相关

    /**
     * 获取孩子列表
     */
    public static List<ChildDTO> getChildList() {
        getLoginedAccount();
        if (userInfoResponse != null) {
            return userInfoResponse.getChildDTOs();
        }
        return null;
    }

    public static List<UserSimpleDTO> getChildUserSimpleList() {
        List<ChildDTO> childList = getChildList();
        if (JListKit.isNotEmpty(childList)) {
            List<UserSimpleDTO> childUserList = JListKit.newArrayList();
            for (ChildDTO dto : childList) {
                UserInfoResponse userInfoDTO = dto.getUserInfoDTO();
                if (userInfoDTO != null) {
                    UserSimpleDTO userSimpleDTO = userInfoDTO.getUserSimpleDTO();
                    if (userSimpleDTO != null) {
                        childUserList.add(userSimpleDTO);
                    }
                }
            }
            return childUserList;
        }
        return null;
    }

    /**
     * 获取默认小孩
     */
    private static ChildDTO getDefaultChild() {
        List<ChildDTO> childList = getChildList();
        if (JListKit.isNotEmpty(childList)) {
            for (ChildDTO dto : childList) {
                if (dto.isDefaultChild()) {
                    return dto;
                }
            }
            return childList.get(0);// 如果没有设置默认孩子，这里返回第一个
        }
        return null;
    }

    /**
     * 获取默认小孩的用户信息
     */
    public static UserSimpleDTO getDefaultChildUserSimpleDTO() {
        ChildDTO defaultChild = getDefaultChild();
        if (defaultChild != null) {
            UserInfoResponse userInfoDTO = defaultChild.getUserInfoDTO();
            if (userInfoDTO != null) {
                return userInfoDTO.getUserSimpleDTO();
            }
        }
        return null;
    }

    /**
     * 获取默认小孩的名字
     */
    public static String getDefaultChildName() {
        UserSimpleDTO defaultChildUserSimpleDTO = getDefaultChildUserSimpleDTO();
        if (defaultChildUserSimpleDTO != null) {
            return defaultChildUserSimpleDTO.getName();
        }
        return "";
    }

    /**
     * 获取默认小孩ID
     */
    public static String getDefaultChildId() {
        UserSimpleDTO defaultChildUserSimpleDTO = getDefaultChildUserSimpleDTO();
        if (defaultChildUserSimpleDTO != null) {
            return defaultChildUserSimpleDTO.getId();
        }
        return "";
    }

    public static List<String> getChildNameList() {
        List<UserSimpleDTO> childUserSimpleList = getChildUserSimpleList();
        if (JListKit.isNotEmpty(childUserSimpleList)) {
            List<String> nameList = JListKit.newArrayList();
            for (UserSimpleDTO dto : childUserSimpleList) {
                nameList.add(dto.getName());
            }
            return nameList;
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


    /**
     * 保存完整的登录新
     *
     * @param loginInfoBean    账号密码
     * @param loginResponse    登录成功的token对象
     * @param userInfoResponse 用户完整信息
     */
    public static boolean cacheFullUserInfo(LoginInfoBean loginInfoBean, LoginResponse loginResponse, UserInfoResponse userInfoResponse) {
        // 1.同步Token缓存
        syncTokenInfo(loginResponse);
        // 2.把登录信息缓存到SP中,可通过SAccountUtil读取
        saveLoginInfo(userInfoResponse);
        // 3.************** 保存单点登录信息 **************
        SSODetailBean ssoDetailBean = new SSODetailBean();
        // 添加Token对象
        ssoDetailBean.setTokenBean(loginResponse);
        // 添加用户详细信息
        ssoDetailBean.setUserInfoResponse(userInfoResponse);
        // 添加账号密码缓存
        ssoDetailBean.setLoginInfo(loginInfoBean);
        // 保存到文件
        return SxwMobileSSOUtil.saveOSSInfo(ssoDetailBean);
    }
}
