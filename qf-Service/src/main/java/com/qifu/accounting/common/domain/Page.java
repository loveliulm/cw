package com.qifu.accounting.common.domain;

/**
 * 自定义分页
 *  yangyang
 * @date 2020/7/23 16:57
 */
public class Page {
    /*每页多少条数据*/
    private int pageSize = 20;

    /*起始行*/
    private int pageStart = 0;

    /*当前页码数*/
    private int pageNum = 1;


    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageStart() {
        this.pageStart = (this.pageNum - 1) * this.pageSize;
        return pageStart;
    }

    public void setPageStart(int pageStart) {
        this.pageStart = pageStart;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }
}
