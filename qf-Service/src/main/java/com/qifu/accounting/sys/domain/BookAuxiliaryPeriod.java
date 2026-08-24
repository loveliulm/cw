package com.qifu.accounting.sys.domain;

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
 * 辅助核算月末余额对象 book_auxiliary_period
 *
 *  YM
 * 09-07
 */
@Data
@EqualsAndHashCode
@NoArgsConstructor
@Accessors(chain = true)
@TableName("book_auxiliary_period")
public class BookAuxiliaryPeriod implements Serializable {

private static final long serialVersionUID=1L;


    /** $column.columnComment */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 辅助核算id */
    @Excel(name = "辅助核算id")
    private Long auxiliaryId;

    /** 账期 */
    @Excel(name = "账期" , width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date yearPeriod;

    /** 期初余额 */
    @Excel(name = "期初余额")
    private Long periodInitBalance;

    /** 期末余额 */
    @Excel(name = "期末余额")
    private Double periodEndBalance;

    /** $column.columnComment */
    private String delFlag;

    /** $column.columnComment */
    private Date createTime;

    /** $column.columnComment */
    private Date updateTime;
}
