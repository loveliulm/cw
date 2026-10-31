package com.qifu.accounting.report.domain;

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
import java.math.BigDecimal;
import java.util.Date;
import com.qifu.framework.web.domain.BaseEntity;

/**
 * 利润模板对象 book_report_profit_model
 *
 *  YM
 * 09-11
 */
@Data
@EqualsAndHashCode
@NoArgsConstructor
@Accessors(chain = true)
@TableName("book_report_profit_model")
public class BookReportProfitModel implements Serializable {

private static final long serialVersionUID=1L;


    /** $column.columnComment */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 会计制度id */
    @Excel(name = "会计制度id")
    private Long templateId;

    /** 项目 */
    @Excel(name = "项目")
    private String profitName;

    /** 行次 */
    @Excel(name = "行次")
    private Integer rowNum;

    /** 层级 */
    @Excel(name = "层级")
    private Integer level;

    /** 是否显示规则 */
    @Excel(name = "是否显示规则")
    private Boolean isRule;

    /** $column.columnComment */
    @Excel(name = "是否显示规则")
    private String profitTitle;

}
