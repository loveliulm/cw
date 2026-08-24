package com.qifu.accounting.sys.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.qifu.framework.aspectj.lang.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 *  yangyang
 * @date 2020/9/25 9:19
 */
@Data
public class BookSubjectPeriodTemporary {

    /** id */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 科目id */
    @Excel(name = "科目id")
    private Long subjectId;

    /** 期末余额 */
    @Excel(name = "期末余额")
    private BigDecimal periodBalance;

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
