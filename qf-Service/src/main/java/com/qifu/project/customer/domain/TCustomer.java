package com.qifu.project.customer.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.qifu.project.system.domain.SysUser;
import lombok.*;
import lombok.experimental.Accessors;
import com.qifu.framework.aspectj.lang.annotation.Excel;
import com.qifu.framework.aspectj.lang.annotation.Excel.Type;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import com.qifu.framework.web.domain.BaseEntity;

/**
 * 客户对象 t_customer
 *
 *
 *
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@TableName(value="t_customer")
public class TCustomer implements Serializable {

    private static final long serialVersionUID=1L;


    /** 客户id */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 客户编码 */
    @Excel(name = "客户编码", type = Type.EXPORT)
    private String customerCode;

    /** 客户名称 */
    @Excel(name = "客户名称")
    private String customerName;

    /** 服务类型 1.代账服务 2.工商注册 3.工商变更 4.工商注销 5.代账+工商*/
//    @Excel(name = "服务类型")
    private Integer serviceType;

    /** 社会统一信用码 */
    @Excel(name = "社会统一信用码",type = Type.IMPORT)
    private String unifiedSocialCreditCode;

    /** 纳税性质 */
    @Excel(name = "纳税性质",readConverterExp = "1=一般纳税人,2=小规模纳税人")
    private Integer vatType;

    /** 法定代表人 */
    @Excel(name = "法定代表人",type = Type.IMPORT)
    private String representative;

    /** 成立日期 */
    @Excel(name = "成立日期",type = Type.IMPORT,dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date establishmentDate;

    /** 注册地址 */
    @Excel(name = "注册地址",type = Type.IMPORT)
    private String registrationAddress;

    /*会计主管id*/
    private Long managerAccountingUserId;

    /*记账会计id*/
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long bookkeepingAccountingUserId;

    /*会计助理id*/
    private Long assistantAccountingUserId;

    /** 账套状态 */
    @Excel(name = "账套状态",type = Type.EXPORT)
    private Integer accountingStatus;

    /** 联系人电话 */
    @Excel(name = "联系人电话")
    private String normalContactPhone;

    /** 法人身份证 */
    @Excel(name = "法人身份证",type = Type.IMPORT)
    private String representativeCard;

    /** 备注 */
    private String remark;

    /** 创建人 */
    private String createBy;

    /** 创建时间 */
    private Date createTime;

    /** 更新时间 */
    private Date updateTime;

    /*0：启用 1：停用*/
    private String status;

    /*删除标志（0代表存在 2代表删除）*/
    private String delFlag;

    /** 是否办理税盘 */
    @Excel(name = "是否办理税盘",readConverterExp = "0=是,1=否")
    private Integer isopenTaxplate;//is_open_tax_plate

    /** 国税密码 */
    @Excel(name = "国税密码")
    private String countryTaxPass;

    /** 个税申报密码 */
    @Excel(name = "个税申报密码")
    private String incomeTaxPass;

    /** 部门id */
    private Long deptId;

    /** 是否开通电子开票（0：未开通，1：开通） */
    private Integer isOpenInvoice;

//    /**电子票开票限额*/
//    private BigDecimal electronicInvoiceQuota;
//
//    /**电子票分票金额*/
//    private BigDecimal electronicDividedInvoiceQuota;
//
//    /**专票开票限额*/
//    private BigDecimal specialInvoiceQuota;
//
//    /**专票分票金额*/
//    private BigDecimal specialDividedInvoiceQuota;
//
//    /**普票开票限额*/
//    private BigDecimal generalInvoiceQuota;
//
//    /**普票分票金额*/
//    private BigDecimal generalDividedInvoiceQuota;

    /*服务类型*/
    @TableField(exist = false)
    private TCustomerServiceType [] tCustomerServiceTypes;

    @TableField(exist = false)
    @Excel(name = "代理记账",type = Type.IMPORT,readConverterExp = "0=是,1=否",combo = {"是","否"})
    private String voucher;

    @TableField(exist = false)
    @Excel(name = "工商注册",type = Type.IMPORT,readConverterExp = "0=是,1=否",combo = {"是","否"})
    private String register;

    @TableField(exist = false)
    @Excel(name = "工商变更",type = Type.IMPORT,readConverterExp = "0=是,1=否",combo = {"是","否"})
    private String change;

    @TableField(exist = false)
    @Excel(name = "工商注销",type = Type.IMPORT,readConverterExp = "0=是,1=否",combo = {"是","否"})
    private String logoff;
}
