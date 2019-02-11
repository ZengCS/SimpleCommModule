package com.zcs.android.lib.sketch.bean;

/**
 * Created by ZengCS on 2017/9/7.
 * E-mail:zcs@sxw.cn
 * Add:成都市天府软件园E3-3F
 */
public class EmptyEntity extends BaseBean {
    private long id;

    public EmptyEntity() {
    }

    public EmptyEntity(int id) {
        this.id = id;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

}
