package com.qifu.accounting.report.domain.vo;

import lombok.Data;

import java.util.Date;

/**
 * 首页报表参数
 *  yangyang
 * @date 2020/9/21 10:37
 */
@Data
public class FirstPageParams {
    private Date yearPeriod;

    private Long bookId;
}
