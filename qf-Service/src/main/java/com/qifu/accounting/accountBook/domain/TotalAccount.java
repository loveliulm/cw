package com.qifu.accounting.accountBook.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.qifu.accounting.common.enums.BalanceDirectionEnum;
import com.qifu.framework.aspectj.lang.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 *  yangyang
 * @date 2020/8/27 13:48
 */
@Data
public class TotalAccount {
    /*账期*/
    @Excel(name = "账期" , width = 30, dateFormat = "yyyy-MM")
    @JsonFormat(pattern = "yyyy-MM")
    private Date yearPeriod;

    /*摘要*/
    @Excel(name = "摘要")
    private String summary;

    /*借方金额*/
    @Excel(name = "借方")
    private BigDecimal debit;

    /*贷方金额*/
    @Excel(name = "贷方")
    private BigDecimal credit;

    /*余额方向*/
    private BalanceDirectionEnum balanceDirection;

    /*余额方向：借贷*/
    @Excel(name = "方向")
    private BalanceDirectionEnum balanceDirectionText;

    /*余额*/
    @Excel(name = "余额")
    private BigDecimal balance;

}
