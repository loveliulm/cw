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
import java.math.BigDecimal;
import java.util.Date;
import com.qifu.framework.web.domain.BaseEntity;

/**
 * 科目账期期末余额对象 book_subject_period
 *
 *  YM
 * 08-17
 */
@Data
@EqualsAndHashCode
@NoArgsConstructor
@Accessors(chain = true)
@TableName("book_subject_period")
public class BookSubjectPeriod implements Serializable {

private static final long serialVersionUID=1L;


    /** id */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 科目id */
    @Excel(name = "科目id")
    private Long subjectId;

    /** 期初余额*/
    private BigDecimal periodInitBalance;

    /** 期末余额 */
    @Excel(name = "期末余额")
    private BigDecimal periodEndBalance;

    /** 账期 */
    @Excel(name = "账期" , width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date yearPeriod;

    /** 删除字段 */
    private String delFlag;

    /** 创建时间 */
    private Date createTime;

    /** 更新时间 */
    private Date updateTime;
}
