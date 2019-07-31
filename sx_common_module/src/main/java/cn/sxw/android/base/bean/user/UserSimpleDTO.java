package cn.sxw.android.base.bean.user;

import java.io.Serializable;

/**
 * Created by ZengCS on 2018/2/6.
 * E-mail:zcs@sxw.cn
 * Add:成都市天府软件园E3-3F
 */

public class UserSimpleDTO implements Serializable {

    /**
     * studentRoll : 510704200011124624
     * userType : STUDENT
     * provinceId : 510000
     * idnumber : 510704200011124624
     * phoneNumber : 15520581792
     * portraitUrl :
     * areaId : 17
     * id : 834006
     * accountId :
     * regionId : 12
     * name : 何柯
     * teaching :
     * sxwNumber : 80028
     * cityId : 1
     */

    private String studentRoll;
    private String userType;
    private String provinceId;
    private String idnumber;
    private String phoneNumber;
    private String portraitUrl;
    private String areaId;
    private String id;
    private String accountId;
    private String regionId;
    private String name;
    private String teaching;
    private String cityId;
    private String sxwNumber;

    public String getStudentRoll() {
        return studentRoll;
    }

    public void setStudentRoll(String studentRoll) {
        this.studentRoll = studentRoll;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public String getIdnumber() {
        return idnumber;
    }

    public void setIdnumber(String idnumber) {
        this.idnumber = idnumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPortraitUrl() {
        return portraitUrl;
    }

    public void setPortraitUrl(String portraitUrl) {
        this.portraitUrl = portraitUrl;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTeaching() {
        return teaching;
    }

    public void setTeaching(String teaching) {
        this.teaching = teaching;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getSxwNumber() {
        return sxwNumber;
    }

    public void setSxwNumber(String sxwNumber) {
        this.sxwNumber = sxwNumber;
    }
}
