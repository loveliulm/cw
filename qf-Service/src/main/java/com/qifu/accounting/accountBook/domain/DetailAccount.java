package com.qifu.accounting.accountBook.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.qifu.accounting.common.enums.BalanceDirectionEnum;
import com.qifu.framework.aspectj.lang.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 *  yangyang
 * @date 2020/7/20 15:39
 */
@Data
public class DetailAccount {

    /*凭证日期*/
    @Excel(name = "日期" ,dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date voucherDate;

    /*账期*/
    @JsonFormat(pattern = "yyyy-MM")
    private Date yearPeriod;

    /*凭证字号*/
    @Excel(name = "凭证字号")
    private String voucherCode;

    /*凭证id*/
    private Long voucherId;

    /*摘要*/
    @Excel(name = "摘要")
    private String summary;

    /*借方金额*/
    @Excel(name = "借方")
    private BigDecimal debitTotal;

    /*贷方金额*/
    @Excel(name = "贷方")
    private BigDecimal creditTotal;

    /*余额方向*/
    private BalanceDirectionEnum balanceDirection;

    /*余额方向：借贷*/
    @Excel(name = "方向")
    private BalanceDirectionEnum balanceDirectionText;

    /*余额*/
    @Excel(name = "余额")
    private BigDecimal balance;

}
