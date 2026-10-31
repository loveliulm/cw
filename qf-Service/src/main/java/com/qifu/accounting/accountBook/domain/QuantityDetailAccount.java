package com.qifu.accounting.accountBook.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.qifu.accounting.common.enums.BalanceDirectionEnum;
import com.qifu.framework.aspectj.lang.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 数量金额明细账
 *  yangyang
 * @date 2020/9/10 9:59
 */
@Data
public class QuantityDetailAccount {
    /*凭证日期*/
    @Excel(name = "日期" ,dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date voucherDate;

    /*凭证字号*/
    @Excel(name = "凭证字号")
    private String voucherCode;

    /*凭证id*/
    private Long voucherId;

    /*摘要*/
    @Excel(name = "摘要")
    private String summary;

    /*借方发生额(数量)*/
    @Excel(name = "借方发生额(数量)")
    private Integer debitQuantity;

    /*借方发生额(单价)*/
    @Excel(name = "借方发生额(单价)")
    private BigDecimal debitPrice;

    /*借方发生额(金额)*/
    @Excel(name = "借方发生额(金额)")
    private BigDecimal debitTotal;

    /*贷方发生额(数量)*/
    @Excel(name = "贷方发生额(数量)")
    private Integer creditQuantity;

    /*贷方发生额(单价)*/
    @Excel(name = "贷方发生额(单价)")
    private BigDecimal creditPrice;

    /*贷方发生额(金额)*/
    @Excel(name = "贷方发生额(金额)")
    private BigDecimal creditTotal;

    /*余额方向*/
    private BalanceDirectionEnum balanceDirection;

    /*余额(方向)*/
    @Excel(name = "余额(方向)")
    private BalanceDirectionEnum balanceDirectionText;

    /*余额(数量)*/
    @Excel(name = "余额(数量)")
    private Integer balanceQuantity;

    /*余额(单价)*/
    @Excel(name = "余额(单价)")
    private BigDecimal balancePrice;

    /*余额(金额)*/
    @Excel(name = "余额(金额)")
    private BigDecimal balance;
}
