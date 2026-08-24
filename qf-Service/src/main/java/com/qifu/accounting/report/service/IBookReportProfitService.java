package com.qifu.accounting.report.service;

import com.qifu.accounting.report.domain.BookReportProfit;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qifu.accounting.report.domain.vo.ReportAssetsParams;
import com.qifu.accounting.report.domain.vo.ReportProfitParams;

import java.util.List;

/**
 * 利润Service接口
 *
 *  YM
 * 09-11
 */
public interface IBookReportProfitService extends IService<BookReportProfit> {


    void initInsetReportProfit(Long bookId, Long templateId);

    List<BookReportProfit> getList(ReportProfitParams params);
}
