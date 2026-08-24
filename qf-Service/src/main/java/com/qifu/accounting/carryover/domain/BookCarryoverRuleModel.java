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
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 结转规则模板对象 book_carryover_rule_model
 *
 *  YM
 * 07-23
 */
@Data
@EqualsAndHashCode
@NoArgsConstructor
@Accessors(chain = true)
@TableName("book_carryover_rule_model")
public class BookCarryoverRuleModel implements Serializable {

private static final long serialVersionUID=1L;

    @TableId(value = "model_rule_id", type = IdType.AUTO)
    private Long modelRuleId;

    private Long modelId;

    /* 结转code */
    private CarryoverCodeEnum code;

    /** 摘要 */
    @Excel(name = "摘要")
    private String summary;

    /** 方向 */
    private BalanceDirectionEnum direction;

    /** 方向text */
    private BalanceDirectionEnum directionText;

    /** 科目编码 */
    @Excel(name = "科目编码")
    private String subjectCode;

    /** 科目 */
    @Excel(name = "科目")
    private String subject;

    /** 取数来源科目编码 */
    private String originSubjectCode;

    /** 取数来源科目 */
    @Excel(name = "取数来源科目")
    private String originSubject;

    /** 取值类型：自动平衡,贷方发生额,借方发生额,贷方余额,借方余额,期初余额,期末余额 */
    @Excel(name = "取值类型")
    private DateRuleEnum dateRule;

    /** 百分比% */
    @Excel(name = "百分比%")
    private BigDecimal percentage;
}
