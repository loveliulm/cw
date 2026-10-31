package com.qifu.project.base.domain;

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
 * 服务流程对象 base_service_process
 *
 *
 * 06-23
 */
@Data
@EqualsAndHashCode
@NoArgsConstructor
@Accessors(chain = true)
@TableName("base_service_process")
public class BaseServiceProcess implements Serializable {

private static final long serialVersionUID=1L;


    /** 流程id */
    @TableId(value = "service_process_id", type = IdType.AUTO)
    private Long serviceProcessId;

    /** 流程名称 */
    @Excel(name = "流程名称")
    private String serviceProcessName;

    /** 预计耗时/h */
    @Excel(name = "预计耗时/h")
    private Long estimatedTime;

    /** 服务项目id */
    @Excel(name = "服务项目id")
    private Long serviceItemId;

    /** 顺序 */
    @Excel(name = "顺序")
    private Long number;

    /*描述*/
    private String processDescribe;

    /*资料*/
    private String processData;

    /*交付件*/
    private String processOutput;
}
