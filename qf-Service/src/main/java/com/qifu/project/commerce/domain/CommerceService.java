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
 * 工商服务任务对象 t_commerce_service
 * 
 *  YM
 * 08-14
 */
@Data
@EqualsAndHashCode
@NoArgsConstructor
@Accessors(chain = true)
@TableName("t_commerce_service")
public class CommerceService implements Serializable {

private static final long serialVersionUID=1L;


    /** id */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 合同id */
    @Excel(name = "合同id")
    private Long contractId;

    /** 创建时间 */
    private Date createTime;

    /** 添加人 */
    private Long createBy;

    /** 执行人id */
    @Excel(name = "执行人id")
    private Long operatorUserId;

    /** 任务状态 */
    @Excel(name = "任务状态")
    private Integer status;

    /** 任务进度 */
    @Excel(name = "任务进度")
    private String taskStatus;

    /** 修改时间 */
    private Date updateTime;

    /** 修改人id */
    private Long updateBy;

    /** 备注 */
    @Excel(name = "备注")
    private String remark;

    /** 删除 */
    private String isDel;

    /**服务状态（0：正常服务，2：停止服务）*/
    private Integer serviceStatus;
}
