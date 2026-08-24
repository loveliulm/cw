package com.qifu.project.system.domain;

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
 * 编号规则对象 sys_number_rule
 * 
 *
 * 06-18
 */
@Data
@EqualsAndHashCode
@TableName("sys_number_rule")
public class SysNumberRule implements Serializable {

private static final long serialVersionUID=1L;


    /** 规则id */
    @TableId(value = "rule_id", type = IdType.AUTO)
    private Long ruleId;

    /** 规则名称 */
    @Excel(name = "规则名称")
    private String ruleName;

    /** 规则类型 */
    @Excel(name = "规则类型")
    private Integer ruleType;

    /** 前缀 */
    @Excel(name = "前缀")
    private String prefix;

    /** 状态 */
    @Excel(name = "状态")
    private Integer status;

    /** 排序 */
    @Excel(name = "排序")
    private Integer sortNum;

    /** 描述 */
    @Excel(name = "描述")
    private String descn;

    /** 创建时间 */
    private Date createTime;

    /** 更新时间 */
    private Date updateTime;
}
