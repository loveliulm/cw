package com.qifu.project.park.domain.vo;

import com.qifu.framework.aspectj.lang.annotation.Excel;
import com.qifu.project.park.domain.ParkAddress;
import lombok.Data;

import java.util.Date;

/**
 * vo
 */
@Data
public class ParkAddressVo extends ParkAddress {

    /** 合同id */
    private Long contractId;

    /** 合同编号 */
    private String contractNo;

    /** 合同开始时间 */
    private Date contractStartDate;

    /** 合同结束时间 */
    private Date contractEndDate;

    /** 客户id */
    private Long customerId;

    /** 客户姓名 */
    private String customerName;

    /** 到期时间 */
    private Date expirationTime;

    /** 企业现状 */
    private Integer accountingStatus;

    /** 企业年报 */
    private Integer annualReports;

    /** 联系人 */
    private String representative;

    /** 所属业务员 */
    private String belongingSalesperson;

    /** 收款账户 */
    private String bankAcount;

    /** 收款时间 */
    private String collectionDate;

    /** 收款金额 */
    private String collectionAmount;

}
