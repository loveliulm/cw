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
import java.math.BigDecimal;
import java.util.Date;
import com.qifu.framework.web.domain.BaseEntity;

/**
 * 利润规则对象 book_report_profit_rule
 *
 *  YM
 * 09-11
 */
@Data
@EqualsAndHashCode
@NoArgsConstructor
@Accessors(chain = true)
@TableName("book_report_profit_rule")
public class BookReportProfitRule implements Serializable {

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
    private char sign;

    /** 取数规则 */
    @Excel(name = "取数规则")
    private ReportDateRuleEnum dataRule;

    /** 利润表id */
    @Excel(name = "利润表id")
    private Long profitId;

    /** 删除标准 */
    private String delFlag;

    /** $column.columnComment */
    private Date createTime;

    /** $column.columnComment */
    private Date updateTime;

    /*本年累计金额-不包括结转的凭证*/
    private BigDecimal yearTotal;

    /*本期数（本月发生额-不包括结转的凭证）*/
    private BigDecimal currentPeriodHappen;

    /*上期金额（上期的余额-企业会计准则）*/
    private BigDecimal previousPeriodBalance;

}
