package com.qifu.accounting.report.domain.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 *  yangyang
 * @date 2020/9/23 16:13
 */
@Data
public class FirstPageFinancial {

    private String subjectName;

    private BigDecimal balance;

}
