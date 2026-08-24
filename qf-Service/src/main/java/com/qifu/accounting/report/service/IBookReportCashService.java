package com.qifu.accounting.report.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qifu.accounting.report.domain.BookReportCash;
import com.qifu.accounting.report.domain.vo.ReportCashParams;

/**
 * 现金流量Service接口
 *
 *  YM
 * 09-11
 */
public interface IBookReportCashService extends IService<BookReportCash> {


    void initInsetReportCash(Long bookId, Long templateId);

    List<BookReportCash> getList(ReportCashParams params);
}
