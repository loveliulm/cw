package com.qifu.accounting.report.domain;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.qifu.framework.aspectj.lang.annotation.Excel;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 现金流量模板对象 book_report_cash_model
 *
 *  YM
 * 09-11
 */
@Data
@EqualsAndHashCode
@NoArgsConstructor
@Accessors(chain = true)
@TableName("book_report_cash_model")
public class BookReportCashModel implements Serializable {

private static final long serialVersionUID=1L;


    /** $column.columnComment */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 会计制度id */
    @Excel(name = "会计制度id")
    private Long templateId;

    /** 项目 */
    @Excel(name = "项目")
    private String cashName;

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
    private String cashTitle;

}
