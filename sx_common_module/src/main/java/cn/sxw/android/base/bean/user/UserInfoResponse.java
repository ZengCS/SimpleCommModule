package cn.sxw.android.base.bean.user;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ZengCS on 2018/2/6.
 * E-mail:zcs@sxw.cn
 * Add:成都市天府软件园E3-3F
 */

public class UserInfoResponse implements Serializable {
    private UserSimpleDTO userSimpleDTO;
    private AreaDTO areaDTO;
    // 普通模式下-只有一个行政班级
    private ClassComplexDTO classComplexDTO;
    // 走班排课模式下-有多个班级
    private List<ClassComplexDTO> classComplexDTOS;
    // 当用户是教师时，此列表才有值
    private List<CourseComplexDTO> courseComplexDTOS;
    // 当用户是家长时,此列表才有值
    private List<ChildDTO> childDTOs;

    public ClassComplexDTO getClassComplexDTO() {
        return classComplexDTO;
    }

    public void setClassComplexDTO(ClassComplexDTO classComplexDTO) {
        this.classComplexDTO = classComplexDTO;
    }

    public UserSimpleDTO getUserSimpleDTO() {
        return userSimpleDTO;
    }

    public void setUserSimpleDTO(UserSimpleDTO userSimpleDTO) {
        this.userSimpleDTO = userSimpleDTO;
    }

    public AreaDTO getAreaDTO() {
        return areaDTO;
    }

    public void setAreaDTO(AreaDTO areaDTO) {
        this.areaDTO = areaDTO;
    }

    public List<CourseComplexDTO> getCourseComplexDTOS() {
        return courseComplexDTOS;
    }

    public void setCourseComplexDTOS(List<CourseComplexDTO> courseComplexDTOS) {
        this.courseComplexDTOS = courseComplexDTOS;
    }

    public List<ChildDTO> getChildDTOs() {
        return childDTOs;
    }

    public void setChildDTOs(List<ChildDTO> childDTOs) {
        this.childDTOs = childDTOs;
    }

    public List<ClassComplexDTO> getClassComplexDTOS() {
        return classComplexDTOS;
    }

    public void setClassComplexDTOS(List<ClassComplexDTO> classComplexDTOS) {
        this.classComplexDTOS = classComplexDTOS;
    }
}
