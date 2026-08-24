package com.qifu.accounting.report.service;

import com.qifu.accounting.report.domain.vo.ReportAssetVo;
import com.qifu.accounting.report.domain.vo.ReportAssetsParams;
import com.qifu.accounting.report.domain.BookReportAssetsRule;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.xml.crypto.Data;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 资产负债规则Service接口
 *
 *  YM
 * 09-03
 */
public interface IBookReportAssetsRuleService extends IService<BookReportAssetsRule> {

    List<BookReportAssetsRule> getList(ReportAssetsParams params);

    void edit(ReportAssetVo reportAssetVo);

    Map balanceSheet(BookReportAssetsRule rule, Date currentPeriod);
}
