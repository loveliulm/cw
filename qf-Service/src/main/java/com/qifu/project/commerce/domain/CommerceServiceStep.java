package com.qifu.project.commerce.domain;

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
 * 工商服务步骤对象 t_commerce_service_step
 * 
 *  YM
 * 08-14
 */
@Data
@EqualsAndHashCode
@NoArgsConstructor
@Accessors(chain = true)
@TableName("t_commerce_service_step")
public class    CommerceServiceStep implements Serializable {

private static final long serialVersionUID=1L;


    /** id */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 工商服务任务_id */
    @Excel(name = "工商服务任务_id")
    private Long commerceServiceId;

    /** 名称 */
    @Excel(name = "名称")
    private String stepName;

    /** 顺序 */
    @Excel(name = "顺序")
    private Integer stepOrder;

    /** 描述 */
    @Excel(name = "描述")
    private String stepDescribe;

    /** 资料 */
    @Excel(name = "资料")
    private String stepData;

    /** 交付件 */
    @Excel(name = "交付件")
    private String stepOutput;

    /** 完成时间 */
    @Excel(name = "完成时间" , width = 30, dateFormat = "yyyy-MM-dd")
    private Date completeTime;

    /** 执行人 */
    @Excel(name = "执行人")
    private Long operatorUserId;

    /** 状态 */
    @Excel(name = "状态")
    private Integer stepStatus;

    /**备注*/
    private String remark;

}
