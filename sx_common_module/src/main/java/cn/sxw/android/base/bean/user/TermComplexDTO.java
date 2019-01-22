package cn.sxw.android.base.bean.user;

import java.io.Serializable;

/**
 * Created by ZengCS on 2018/2/6.
 * E-mail:zcs@sxw.cn
 * Add:成都市天府软件园E3-3F
 */

public class TermComplexDTO implements Serializable {
    /**
     * id : 6ab12499-a071-4c86-b99c-2aea85a3fb1e
     * termYearId : 4ec7e004-bf52-4595-bfd0-a4c38a2111d8
     * termYearName : 2017-2018学年
     * startDate : 2018-02-01T08:00:00
     * endDate : 2018-08-01T07:59:59
     */
    private String id;
    private String termYearId;
    private String termYearName;
    private String startDate;
    private String endDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTermYearId() {
        return termYearId;
    }

    public void setTermYearId(String termYearId) {
        this.termYearId = termYearId;
    }

    public String getTermYearName() {
        return termYearName;
    }

    public void setTermYearName(String termYearName) {
        this.termYearName = termYearName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
