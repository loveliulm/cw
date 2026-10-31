package com.qifu.accounting.accountBook.domain.vo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 总账详细内容
 *  YM
 */
@Getter
@Setter
public class TotalAccContent {

    /**
     * 会计年期
     */
    private Date yearPeriod;

    /**
     * 凭证摘要
     */
    private String voucherRemark;

    /**
     * 外币借方发生额
     */
    private BigDecimal debitAmountForeign;
    /**
     * 本币借方发生额
     */
    private BigDecimal debitAmountLocal;
    /**
     * 外币贷方发生额
     */
    private BigDecimal creditAmountForeign;
    /**
     * 本币贷方发生额
     */
    private BigDecimal creditAmountLocal;
    /**
     * 科目借贷方向
     */
    private String subjectDirection;
    /**
     * 外币余额
     */
    private BigDecimal balanceForeign;
    /**
     * 本币余额
     */
    private BigDecimal balanceLocal;
}
