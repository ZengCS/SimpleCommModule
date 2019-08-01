package cn.sxw.android.base.bean.user;

import java.io.Serializable;

/**
 * Created by ZengCS on 2018/2/6.
 * E-mail:zcs@sxw.cn
 * Add:成都市天府软件园E3-3F
 */

public class ChildDTO implements Serializable {
    private boolean defaultChild;
    private int parentType;
    private String parentTypeName;
    private UserInfoResponse userInfoDTO;

    public boolean isDefaultChild() {
        return defaultChild;
    }

    public void setDefaultChild(boolean defaultChild) {
        this.defaultChild = defaultChild;
    }

    public int getParentType() {
        return parentType;
    }

    public void setParentType(int parentType) {
        this.parentType = parentType;
    }

    public String getParentTypeName() {
        return parentTypeName;
    }

    public void setParentTypeName(String parentTypeName) {
        this.parentTypeName = parentTypeName;
    }

    public UserInfoResponse getUserInfoDTO() {
        return userInfoDTO;
    }

    public void setUserInfoDTO(UserInfoResponse userInfoDTO) {
        this.userInfoDTO = userInfoDTO;
    }
}
