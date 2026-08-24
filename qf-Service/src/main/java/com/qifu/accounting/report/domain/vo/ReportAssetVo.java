package com.qifu.accounting.report.domain.vo;

import com.qifu.accounting.report.domain.BookReportAssetsRule;
import lombok.Data;

import java.util.List;

/**
 *  yangyang
 * @date 2020/9/3 16:18
 */
@Data
public class ReportAssetVo {
    private Long bookId;

    private Integer rowNum;

    private Boolean isClassify;

    private List<BookReportAssetsRule> details;
}
