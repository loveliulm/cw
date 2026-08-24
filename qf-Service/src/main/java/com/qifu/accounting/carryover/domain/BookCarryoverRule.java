package com.qifu.accounting.carryover.domain;

import com.qifu.accounting.common.enums.BalanceDirectionEnum;
import com.qifu.accounting.common.enums.CarryoverCodeEnum;
import com.qifu.accounting.common.enums.DateRuleEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import com.qifu.framework.aspectj.lang.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 结转规则对象 book_carryover_rule
 *
 *  YM
 * 07-23
 */
@Data
@EqualsAndHashCode
@NoArgsConstructor
@Accessors(chain = true)
@TableName("book_carryover_rule")
public class BookCarryoverRule implements Serializable {

private static final long serialVersionUID=1L;


    /** 规则id */
    @TableId(value = "rule_id", type = IdType.AUTO)
    private Long ruleId;

    /*结转id:isModel为true时为0，其他情况有对应的值*/
    private Long carryoverId;

    /** 账套id */
    private Long bookId;

    /*结转code */
    private CarryoverCodeEnum code;

    /** 摘要 */
    @NotBlank(message = "摘要不能为空")
    @Size(min = 0, max = 30, message = "摘要长度不能超过30个字符")
    private String summary;

    /** 方向 */
    private BalanceDirectionEnum direction;

    /** 方向text */
    private BalanceDirectionEnum directionText;

    /* 科目code */
    @NotNull(message = "科目编码不能为空")
    private String subjectCode;

    /*科目code name*/
    @NotNull(message = "科目不能为空")
    private String subject;

    /** 取数来源科目编码 */
    @NotNull(message = "取数来源科目编码不能为空")
    private String originSubjectCode;

    /** 取数来源科目 */
    @Excel(name = "取数来源科目")
    @NotNull(message = "取数来源科目不能为空")
    private String originSubject;

    /** 取值类型：自动平衡,贷方发生额,借方发生额,贷方余额,借方余额,期初余额,期末余额 */
    @Excel(name = "取值类型")
    private DateRuleEnum dateRule;

    /** 百分比% */
    private BigDecimal percentage;

    /** 创建时间 */
    private Date createTime;

    /** 更新时间 */
    private Date updateTime;

    /** 是否模板 */
    private Boolean isModel;

    /** 删除字段 */
    private String delFlag;
}
