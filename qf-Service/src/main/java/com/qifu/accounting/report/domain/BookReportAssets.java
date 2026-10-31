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
 * 资产负债模板对象 book_report_assets_model
 *
 *  YM
 * 09-03
 */
@Data
@EqualsAndHashCode
@NoArgsConstructor
@Accessors(chain = true)
@TableName("book_report_assets_model")
public class BookReportAssets implements Serializable {

private static final long serialVersionUID=1L;


    /** $column.columnComment */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 账套id */
    private Long bookId;

    /** 资产名称 */
    @Excel(name = "资产")
    private String assetsName;

    /** 行次 */
    @Excel(name = "行次")
    private Integer assetsRowNum;

    /** 期末余额 */
    @Excel(name = "期末余额")
    private BigDecimal assetsEndBalance;

    /** 年初余额 */
    @Excel(name = "年初余额")
    private BigDecimal assetsYearInitBalance;

    /** 资产层级 */
    private Integer assetsLevel;

    private String assetsTitle;

    /** 是否显示资产规则 */
    private Boolean isAssetsRule;

    /** 负债和所有者权益名称 */
    @Excel(name = "负债和所有者权益")
    private String debtName;

    /** 负债行次 */
    @Excel(name = "行次")
    private Integer debtRowNum;

    /** 负债期末余额 */
    @Excel(name = "期末余额")
    private BigDecimal debtEndBalance = BigDecimal.ZERO;

    /** 负债年初余额 */
    @Excel(name = "年初余额")
    private BigDecimal debtYearInitBalance  = BigDecimal.ZERO;

    /** 负债层级 */
    private Integer debtLevel;

    private String debtTitle;

    /** 是否显示负债规则 */
    private Boolean isDebtRule;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

}
