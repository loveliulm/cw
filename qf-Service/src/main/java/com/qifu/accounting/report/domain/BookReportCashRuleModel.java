package com.qifu.accounting.report.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.qifu.accounting.common.enums.ReportDateRuleEnum;
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
 * 现金流量规则模板对象 book_report_profit_rule_model
 *
 *  YM
 * 09-11
 */
@Data
@EqualsAndHashCode
@NoArgsConstructor
@Accessors(chain = true)
@TableName("book_report_cash_rule_model")
public class BookReportCashRuleModel implements Serializable {

private static final long serialVersionUID=1L;


    /** $column.columnComment */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 科目编码 */
    @Excel(name = "科目编码")
    private String subjectCode;

    /** $column.columnComment */
    @Excel(name = "科目编码")
    private String subject;

    /** 运算符号 */
    @Excel(name = "运算符号")
    private String sign;

    /** 取数规则 */
    @Excel(name = "取数规则")
    private ReportDateRuleEnum dataRule;

    /** 利润表id */
    @Excel(name = "利润表id")
    private Long cashId;
}
