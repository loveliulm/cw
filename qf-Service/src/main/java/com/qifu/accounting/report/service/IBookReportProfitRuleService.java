package com.qifu.accounting.report.service;

import com.qifu.accounting.report.domain.BookReportProfitRule;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qifu.accounting.report.domain.vo.ReportAssetsParams;
import com.qifu.accounting.report.domain.vo.ReportProfitParams;
import com.qifu.accounting.report.domain.vo.ReportProfitVo;

import java.util.List;
import java.util.Map;

/**
 * 利润规则Service接口
 *
 *  YM
 * 09-11
 */
public interface IBookReportProfitRuleService extends IService<BookReportProfitRule> {

    List<BookReportProfitRule> getList(ReportProfitParams params);

    Map balanceSheet(ReportProfitParams params);

    void edit(ReportProfitVo reportProfitVo);
}
