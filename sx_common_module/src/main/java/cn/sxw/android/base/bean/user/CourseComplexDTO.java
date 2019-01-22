package cn.sxw.android.base.bean.user;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ZengCS on 2018/2/6.
 * E-mail:zcs@sxw.cn
 * Add:成都市天府软件园E3-3F
 */

public class CourseComplexDTO implements Serializable {
    private TermComplexDTO termComplexDTO;
    private SubjectSimpleDTO subjectSimpleDTO;
    private List<ClassComplexDTO> classComplexDTOS;

    public TermComplexDTO getTermComplexDTO() {
        return termComplexDTO;
    }

    public void setTermComplexDTO(TermComplexDTO termComplexDTO) {
        this.termComplexDTO = termComplexDTO;
    }

    public SubjectSimpleDTO getSubjectSimpleDTO() {
        return subjectSimpleDTO;
    }

    public void setSubjectSimpleDTO(SubjectSimpleDTO subjectSimpleDTO) {
        this.subjectSimpleDTO = subjectSimpleDTO;
    }

    public List<ClassComplexDTO> getClassComplexDTOS() {
        return classComplexDTOS;
    }

    public void setClassComplexDTOS(List<ClassComplexDTO> classComplexDTOS) {
        this.classComplexDTOS = classComplexDTOS;
    }
}
