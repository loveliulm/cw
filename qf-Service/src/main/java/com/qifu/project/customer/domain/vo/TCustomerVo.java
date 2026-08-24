package com.qifu.project.customer.domain.vo;

import com.qifu.framework.aspectj.lang.annotation.Excel;
import com.qifu.project.customer.domain.TCustomer;
import lombok.Getter;
import lombok.Setter;

/**
 *  yangyang
 * @date 2020/6/17 15:44
 */
@Getter
@Setter
public class TCustomerVo extends TCustomer {
    /**
     * 会计主管
     */
    @Excel(name = "会计主管")
    private String managerAccounting;

    /**
     * 记账会计
     */
    @Excel(name = "记账会计")
    private String bookkeepingAccounting;

    /**
     * 会计助理
     */
    @Excel(name = "会计助理")
    private String assistantAccounting;

    private Boolean isCreateAccount;

    /**
     * 服务类型名
     */
    @Excel(name = "服务类型")
    private String tCustomerServiceTypesName;

    /**开户银行名称*/
    private String bankName;

    /**银行账户*/
    private String bankAcount;

    /**开票人*/
    private String drawer;

    /**收款人*/
    private String payee;

    /**复核人*/
    private String reviewer;
}
