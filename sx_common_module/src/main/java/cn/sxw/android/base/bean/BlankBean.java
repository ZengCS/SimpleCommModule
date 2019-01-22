package cn.sxw.android.base.bean;

import cn.sxw.android.base.mvp.BaseBean;

/**
 * Created by ZengCS on 2017/9/6.
 * E-mail:zcs@sxw.cn
 * Add:成都市天府软件园E3-3F
 */

public class BlankBean extends BaseBean {
    private int id;
    private String pic;
    private String name;
    private String desc;
    private boolean newest;

    public BlankBean() {
    }

    public BlankBean(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public boolean isNewest() {
        return newest;
    }

    public void setNewest(boolean newest) {
        this.newest = newest;
    }
}
