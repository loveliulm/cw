package com.qifu.project.commerce.domain.vo;

import com.qifu.framework.aspectj.lang.annotation.Excel;
import com.qifu.project.commerce.domain.CommerceService;
import com.qifu.project.commerce.domain.CommerceServiceStep;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class CommerceServiceVo extends CommerceService {

    /**
     * 客户名称
     */
    @Excel(name = "客户名称")
    private String customerName;

    /**资料*/
    private String processData;

    /**服务类型名称*/
    private String serviceTypeName;

    /**服务项目*/
    private String serviceItemName;

    /**派工*/
    private String operatorUserName;

    /**合同编号*/
    @Excel(name = "合同编号")
    private String contractNo;

    /** 合同开始时间 */
    private Date contractStartDate;

    /** 合同结束时间 */
    private Date contractEndDate;

    /** 联系人(法定代表人) */
    private String representative;

    /** 联系电话 */
    private String normalContactPhone;

    // 步骤列表
    private int nowStep;
    private List<CommerceServiceStep> serviceStepList;

}
