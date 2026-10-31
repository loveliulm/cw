package com.qifu.accounting.accountBook.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.qifu.accounting.common.enums.BalanceDirectionEnum;
import com.qifu.framework.aspectj.lang.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 *  yangyang
 * @date 2020/9/9 9:28
 */
@Data
public class AuxiliaryTotalAccount {

    private Long auxiliaryId;

    /*辅助核算名称*/
    @Excel(name = "辅助核算名称")
    private String auxiliaryName;

    @Excel(name = "辅助核算编码")
    private String auxiliaryCode;

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
