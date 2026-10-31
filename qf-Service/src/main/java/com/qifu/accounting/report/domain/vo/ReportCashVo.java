package com.qifu.accounting.report.domain.vo;

import com.qifu.accounting.report.domain.BookReportCashRule;
import lombok.Data;

import java.util.List;

/**
 *  yangyang
 * @date 2020/9/15 17:27
 */
@Data
public class ReportCashVo {
    private Long cashId;

    private List<BookReportCashRule> details;
}
