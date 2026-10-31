package com.qifu.accounting.sys.domain.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 *  yangyang
 * @date 2020/9/9 17:13
 */
@Data
public class TrialBalance {

    /*期初余额*/
    private String node1 = "期初余额（综合本位币）";

    /*累计发生额*/
    private String node2 = "累计发生额（综合本位币）";

    /*期初余额借方金额*/
    private BigDecimal debitTotal1;

    /*本年累计借方金额*/
    private BigDecimal debitTotal2;

    /*期初余额贷方金额*/
    private BigDecimal creditTotal1;

    /*本年累计贷方金额*/
    private BigDecimal creditTotal2;

    /*期初余额差额*/
    private BigDecimal imbalance1;

    /*本年累计差额*/
    private BigDecimal imbalance2;

    /*是否平衡*/
    private Boolean isImbalance;
}
