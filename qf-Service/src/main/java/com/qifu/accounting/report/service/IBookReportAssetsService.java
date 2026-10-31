package com.qifu.accounting.report.service;

import com.qifu.accounting.report.domain.BookReportAssets;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qifu.accounting.report.domain.vo.ReportAssetsParams;

import java.util.List;

/**
 * 资产负债模板Service接口
 *
 *  YM
 * 09-03
 */
public interface IBookReportAssetsService extends IService<BookReportAssets> {
    void initInsetReportAssets(Long bookId, Long templateId);

    List<BookReportAssets> getList(ReportAssetsParams params);

}
