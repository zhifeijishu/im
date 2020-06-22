package com.tksflysun.im.common.qo;

/**
 * @author 查询qo
 */
public class Qo {

    private Page page;

    public Sort getSort() {
        return sort;
    }

    public void setSort(Sort sort) {
        this.sort = sort;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    private Sort sort;

}
