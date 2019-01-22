package cn.sxw.android.base.bean.user;

import java.io.Serializable;

/**
 * Created by ZengCS on 2018/2/6.
 * E-mail:zcs@sxw.cn
 * Add:成都市天府软件园E3-3F
 */

public class ClassComplexDTO implements Serializable {
    private ClassSimpleDTO classSimpleDTO;
    private GradeComplexDTO gradeComplexDTO;

    public ClassSimpleDTO getClassSimpleDTO() {
        return classSimpleDTO;
    }

    public void setClassSimpleDTO(ClassSimpleDTO classSimpleDTO) {
        this.classSimpleDTO = classSimpleDTO;
    }

    public GradeComplexDTO getGradeComplexDTO() {
        return gradeComplexDTO;
    }

    public void setGradeComplexDTO(GradeComplexDTO gradeComplexDTO) {
        this.gradeComplexDTO = gradeComplexDTO;
    }
}
