package cn.sxw.android.base.net.bean;

/**
 * Created by ZengCS on 2018/1/29.
 * E-mail:zcs@sxw.cn
 * Add:成都市天府软件园E3-3F
 */

public class BasePageBean {
    private int currentPage;
    private int pageSize;
    private int totalCount;
    private int totalPage;

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
}
