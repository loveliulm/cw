package com.qifu.accounting.report.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qifu.accounting.report.domain.BookReportCashRule;
import com.qifu.accounting.report.domain.vo.ReportCashParams;
import com.qifu.accounting.report.domain.vo.ReportCashVo;

/**
 * 现金流量规则Service接口
 *
 *  YM
 * 09-11
 */
public interface IBookReportCashRuleService extends IService<BookReportCashRule> {

    List<BookReportCashRule> getList(ReportCashParams params);

    Map balanceSheet(ReportCashParams params);

    void edit(ReportCashVo reportProfitVo);
}
