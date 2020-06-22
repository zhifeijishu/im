package com.tksflysun.im.common.qo;

import java.io.Serializable;

public class Page implements Serializable {
    private static final long serialVersionUID = 1L;
    private long nowPage = 1;
    private long totalRecord = 0;
    private long totalPage = 1;
    private long pageSize = 10;// mysql 使用
    private long startIndex = 0;// mysql 使用 : select * from user limit
    // startIndex,pageSize
    private long beginNo = 1;// oracle 使用 : SELECT * FROM
    // (SELECT A.*, ROWNUM RN FROM
    // (SELECT * FROM user)
    // A WHERE ROWNUM <= #endNo#)
    // WHERE RN >= #beginNo#
    private long endNo = 10; // oracle 使用

    public Page() {
        super();
    }

    public Page(long pageSize) {
        super();
        this.pageSize = pageSize;
        this.endNo = pageSize;
    }

    public Page(long pageSize, String p) {
        super();
        this.pageSize = pageSize;
        this.endNo = pageSize;
    }

    public Page(String p, long nowPage, long totalRecord, long totalPage, long pageSize, long startIndex, long beginNo,
        long endNo) {
        super();
        this.nowPage = nowPage;
        this.totalRecord = totalRecord;
        this.totalPage = totalPage;
        this.pageSize = pageSize;
        this.startIndex = startIndex;
        this.beginNo = beginNo;
        this.endNo = endNo;
    }

    public long getNowPage() {
        return nowPage;
    }

    public void setNowPage(long nowPage) {
        this.nowPage = nowPage;
        if (nowPage == 0)
            nowPage = 1;
        startIndex = (nowPage - 1) * pageSize;
        beginNo = (nowPage - 1) * pageSize + 1;
        endNo = nowPage * pageSize;
    }

    public long getTotalRecord() {
        return totalRecord;
    }

    public void setTotalRecord(long totalRecord) {
        this.totalRecord = totalRecord;
        if (nowPage == 0)
            this.setNowPage(1);
        totalPage = (totalRecord + pageSize - 1) / pageSize;
        // if (nowPage > totalPage)
        // setNowPage(totalPage);
        if (nowPage < 1)
            this.setNowPage(1);
    }

    public long getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(long totalPage) {
        this.totalPage = totalPage;
    }

    public long getPageSize() {
        return pageSize;
    }

    public void setPageSize(long pageSize) {
        this.pageSize = pageSize;
    }

    public long getBeginNo() {
        return beginNo;
    }

    public void setBeginNo(long beginNo) {
        this.beginNo = beginNo;
    }

    public long getEndNo() {
        return endNo;
    }

    public void setEndNo(long endNo) {
        this.endNo = endNo;
    }

    public long getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(long startIndex) {
        this.startIndex = startIndex;
    }
}
