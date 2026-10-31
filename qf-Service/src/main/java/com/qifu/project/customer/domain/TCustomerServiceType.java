package com.qifu.project.customer.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import com.qifu.framework.aspectj.lang.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.io.Serializable;
import java.util.Date;
import com.qifu.framework.web.domain.BaseEntity;

/**
 * 【请填写功能名称】对象 t_customer_service_type
 *
 *  YM
 * 06-18
 */
@Data
@EqualsAndHashCode
@NoArgsConstructor
@Accessors(chain = true)
@TableName("t_customer_service_type")
public class TCustomerServiceType implements Serializable {

private static final long serialVersionUID=1L;


    /** 客户id */
    @Excel(name = "客户id")
    private Long customerId;

    /** 服务类型id */
    @Excel(name = "服务类型id")
    private Long serviceTypeId;

    /** $column.columnComment */
    @Excel(name = "服务类型id")
    private String serviceTypeName;

    private Date createTime;
}
