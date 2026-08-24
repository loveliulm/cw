package com.qifu.accounting.accountBook.domain;

import com.qifu.framework.aspectj.lang.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;

/**
 *  yangyang
 * @date 2020/7/31 10:28
 */
@Data
public class VoucherTotal {
    private Long subjectId;

    @Excel(name = "科目编码")
    private String subjectCode;

    @Excel(name = "科目名称")
    private String subjectName;

    @Excel(name = "贷方金额")
    private BigDecimal credit;

    /** 借方金额 */
    @Excel(name = "借方金额")
    private BigDecimal debit;

    private Integer level;
}
