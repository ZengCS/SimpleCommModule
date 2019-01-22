package cn.sxw.android.base.bean.user;

import java.io.Serializable;

/**
 * Created by ZengCS on 2018/2/6.
 * E-mail:zcs@sxw.cn
 * Add:成都市天府软件园E3-3F
 */

public class ClassSimpleDTO implements Serializable {
    /**
     * id : 7f8f422d-88b0-47ca-83cd-8a06dd7cac8c
     * name : 高2016级109班
     */
    private String id;
    private String name;
    private int artOrScience;

    public int getArtOrScience() {
        return artOrScience;
    }

    public void setArtOrScience(int artOrScience) {
        this.artOrScience = artOrScience;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
