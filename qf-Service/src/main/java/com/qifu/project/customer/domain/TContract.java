package com.qifu.project.customer.domain;

import com.qifu.project.common.enums.CollectionStatusEnum;
import lombok.*;
import lombok.experimental.Accessors;
import com.qifu.framework.aspectj.lang.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 【合同】对象 t_contract
 *
 *  YM
 * 06-18
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@TableName("t_contract")
public class TContract implements Serializable {

    private static final long serialVersionUID=1L;

    /** 合同id */
    @TableId(value = "contract_id", type = IdType.AUTO)
    private Long contractId;

    /** 合同编号 */
    @Excel(name = "合同编号")
    private String contractNo;

    /** 二级部门（公司id） */
    @Excel(name = "部门id",type = Excel.Type.IMPORT)
    private Long deptId;

    /** 客户id */
    @Excel(name = "客户id",type = Excel.Type.IMPORT)
    private Long customerId;

    /** 客户姓名 */
    @Excel(name = "客户姓名")
    private String customerName;

    /** 服务类型id */
    @Excel(name = "服务类型id",type = Excel.Type.IMPORT)
    private Long serviceTypeId;

    /** 服务类型名称 */
    @Excel(name = "服务类型名称")
    private String serviceTypeName;

    /** 服务项目id */
    @Excel(name = "服务项目id",type = Excel.Type.IMPORT)
    private Long serviceItemId;

    /** 服务项目 */
    @Excel(name = "服务项目")
    private String serviceItemName;

    /** 合同开始时间 */
    @Excel(name = "合同开始时间" , width = 30, dateFormat = "yyyy-MM-dd")
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date contractStartDate;

    /** 合同结束时间 */
    @Excel(name = "合同结束时间" , width = 30, dateFormat = "yyyy-MM-dd")
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date contractEndDate;

    /** 付款方式:一次性付款，分期付款 */
    @Excel(name = "付款方式",readConverterExp = "1=一次性付款,2=分期付款")
    private Integer payType;

    /** 金额 */
    @Excel(name = "金额")
    private BigDecimal receivedMoney;

    /** 签单人id */
    @Excel(name = "签单人id",type = Excel.Type.IMPORT)
    private Long signStaffUserId;

    /** 签单人 */
    @Excel(name = "签单人")
    private String signStaffName;

    /** $column.columnComment */
    private Date createTime;

    /** $column.columnComment */
    private Date updateTime;

    /** 创建人 */
    private String createBy;

    /** 已到期,即将到期,未到期 */
    private String status;

    /** 0：正常2：删除 */
    private String  delFlag;

    /** 备注 */
    @Excel(name = "备注")
    private String remark;
}
