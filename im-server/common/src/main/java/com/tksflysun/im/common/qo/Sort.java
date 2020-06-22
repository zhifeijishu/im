package com.tksflysun.im.common.qo;

import javax.servlet.http.HttpServletRequest;

/**
 * 排序类
 * 
 * @author
 */
public class Sort {
    public static final String ORDER_ASC = "asc"; // 正序排列

    public static final String ORDER_DESC = "desc"; // 逆序排列

    public static final String PARAM_SORT = "sort";

    public static final String PARAM_ORDER = "order";

    private String order; // 排序方式

    private String sort; // 排序字段

    public Sort() {
        super();
    }

    public Sort(HttpServletRequest req) {
        super();
        init(req);
    }

    private void init(HttpServletRequest req) {
        String sort_ = req.getParameter(PARAM_SORT);
        String order_ = req.getParameter(PARAM_ORDER);
        setSort(sort_);
        setOrder(order_);
    }

    /**
     * 排序 方式
     * 
     * @see Sort#ORDER_ASC
     * @see Sort#ORDER_DESC
     * @return
     */
    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    /**
     * 排序字段
     * 
     * @return
     */
    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

}
