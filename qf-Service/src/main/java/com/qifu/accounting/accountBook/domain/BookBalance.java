package com.qifu.accounting.accountBook.domain;

import com.qifu.framework.aspectj.lang.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 科目余额
 *  yangyang
 * @date 2020/8/28 14:35
 */
@Data
public class BookBalance {
    private Long subjectId;

    /*科目编码*/
    @Excel(name = "科目编码")
    private String subjectCode;

    /*科目名称*/
    @Excel(name = "科目名称")
    private String subjectName;

    /*科目层级*/
    private Integer level;

    /*期初余额借方*/
    @Excel(name = "期初余额借方")
    private BigDecimal startDebit;

    /*期初余额贷方*/
    @Excel(name = "期初余额贷方")
    private BigDecimal startCredit;

    /*发生额借方*/
    @Excel(name = "发生额借方")
    private BigDecimal periodDebit;

    /*发生额贷方*/
    @Excel(name = "发生额贷方")
    private BigDecimal periodCredit;

    /*本年累计借方*/
    @Excel(name = "本年累计发生额借方")
    private BigDecimal yearDebit;

    /*本年累计贷方*/
    @Excel(name = "本年累计发生额贷方")
    private BigDecimal yearCredit;

    /*期末余额借方*/
    @Excel(name = "期末余额借方")
    private BigDecimal endDebit;

    /*期末余额贷方*/
    @Excel(name = "期末余额贷方")
    private BigDecimal endCredit;
}
