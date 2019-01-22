package cn.sxw.android.base.bean.user;

import java.io.Serializable;

/**
 * Created by ZengCS on 2018/2/6.
 * E-mail:zcs@sxw.cn
 * Add:成都市天府软件园E3-3F
 */

public class GradeComplexDTO implements Serializable {
    /**
     * gradeId : 30
     * gradeLevelName : 高二
     * periodName : 高中
     * gradeLevelId : 8184a605-6026-43ac-924b-e38b87b5d6a4
     * periodId : 4
     * graduatedGrade : false
     * yearLevel : 3
     * state : false
     * gradeName : 高2016级
     * year : 2016
     */

    private String gradeId;
    private String gradeLevelName;
    private String periodName;
    private String gradeLevelId;
    private String periodId;
    private boolean graduatedGrade;
    private int yearLevel;
    private boolean state;
    private String gradeName;
    private int year;

    public String getGradeId() {
        return gradeId;
    }

    public void setGradeId(String gradeId) {
        this.gradeId = gradeId;
    }

    public String getGradeLevelName() {
        return gradeLevelName;
    }

    public void setGradeLevelName(String gradeLevelName) {
        this.gradeLevelName = gradeLevelName;
    }

    public String getPeriodName() {
        return periodName;
    }

    public void setPeriodName(String periodName) {
        this.periodName = periodName;
    }

    public String getGradeLevelId() {
        return gradeLevelId;
    }

    public void setGradeLevelId(String gradeLevelId) {
        this.gradeLevelId = gradeLevelId;
    }

    public String getPeriodId() {
        return periodId;
    }

    public void setPeriodId(String periodId) {
        this.periodId = periodId;
    }

    public boolean isGraduatedGrade() {
        return graduatedGrade;
    }

    public void setGraduatedGrade(boolean graduatedGrade) {
        this.graduatedGrade = graduatedGrade;
    }

    public int getYearLevel() {
        return yearLevel;
    }

    public void setYearLevel(int yearLevel) {
        this.yearLevel = yearLevel;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
