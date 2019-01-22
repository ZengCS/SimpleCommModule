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
    private ClassComplexDTO classComplexDTO;
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
}
