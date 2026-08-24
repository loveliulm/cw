package com.qifu.accounting.report.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.qifu.framework.aspectj.lang.annotation.Excel;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 现金流量对象 book_report_cash
 *
 *  YM
 * 09-11
 */
@Data
@EqualsAndHashCode
@NoArgsConstructor
@Accessors(chain = true)
@TableName("book_report_cash")
public class BookReportCash implements Serializable {

private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 会计制度id */
    private Long bookId;

    /** 项目 */
    @Excel(name = "项目")
    private String cashName;

    /** 行次 */
    @Excel(name = "行次")
    private Integer rowNum;

    /** 层级 */
    private Integer level;

    /** 是否显示规则 */
    private Boolean isRule;

    private String cashTitle;

    private Date createTime;

    private Date updateTime;

    /*本年累计金额-不包括结转的凭证*/
    @Excel(name = "本年累计金额")
    private BigDecimal yearTotal;

    /*本期数（本月发生额-不包括结转的凭证）*/
    @Excel(name = "本期金额")
    private BigDecimal currentPeriodHappen;

    /*上期金额（上期的余额-企业会计准则）*/
    private BigDecimal previousPeriodBalance;

}
