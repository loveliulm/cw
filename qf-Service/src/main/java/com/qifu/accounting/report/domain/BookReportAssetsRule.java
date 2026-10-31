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
 * 资产负债规则对象 book_report_assets_rule
 *
 *  YM
 * 09-03
 */
@Data
@EqualsAndHashCode
@NoArgsConstructor
@Accessors(chain = true)
@TableName("book_report_assets_rule")
public class BookReportAssetsRule implements Serializable {

private static final long serialVersionUID=1L;


    /** id */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 科目编码 */
    @Excel(name = "科目编码")
    private String subjectCode;

    /** 科目 */
    @Excel(name = "科目")
    private String subject;

    /** 运算符号 */
    @Excel(name = "运算符号")
    private char sign;

    /** 取数规则 */
    @Excel(name = "取数规则")
    private ReportDateRuleEnum dataRule;

    /** 期末余额 */
    @Excel(name = "期末余额")
    private BigDecimal endBalance;

    /** 年初余额 */
    @Excel(name = "年初余额")
    private BigDecimal yearInitBalance;

    /** 资产负债表行次 */
    @Excel(name = "资产负债表行次")
    private Integer rowNum;

    /** 账套id */
    private Long bookId;

    /*true:重分类 false:不重分类*/
    private Boolean isClassify;

    /** 正常：0 删除2*/
    private String delFlag;

    /** 创建时间 */
    private Date createTime;

    /** 更新时间 */
    private Date updateTime;
}
