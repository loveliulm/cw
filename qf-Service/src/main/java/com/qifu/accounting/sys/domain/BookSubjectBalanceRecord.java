package com.qifu.accounting.sys.domain;

import com.qifu.accounting.common.enums.BalancePurposeEnum;
import com.qifu.common.utils.SecurityUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.qifu.framework.aspectj.lang.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 科目余额明细对象 book_subject_balance_record
 *
 *  YM
 * 07-08
 */
@Data
@EqualsAndHashCode
@Accessors(chain = true)
@TableName("book_subject_balance_record")
public class BookSubjectBalanceRecord implements Serializable {

private static final long serialVersionUID=1L;

    public BookSubjectBalanceRecord(){};

    public BookSubjectBalanceRecord(Long subjectId, BalancePurposeEnum purpose, String remark, BigDecimal oldBalance,BigDecimal newBalance,BigDecimal balance){
        this.subjectId = subjectId;
        this.purpose = purpose;
        this.remark = remark;
        this.oldBalance = oldBalance;
        this.newBalance = newBalance;
        this.balance = balance;
        this.createTime = new Date();
        this.createUserId = SecurityUtils.userId();
    }
    public BookSubjectBalanceRecord(Long subjectId, BalancePurposeEnum purpose, BigDecimal oldBalance,BigDecimal newBalance,BigDecimal balance){
        this.subjectId = subjectId;
        this.purpose = purpose;
        this.oldBalance = oldBalance;
        this.newBalance = newBalance;
        this.balance = balance;
        this.createTime = new Date();
        this.createUserId = SecurityUtils.userId();
    }


    /** $column.columnComment */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long subjectId;

    /** 用途 */
    @Excel(name = "用途")
    private BalancePurposeEnum purpose;

    /** 备注 */
    @Excel(name = "备注")
    private String remark;

    /** 以前金额 */
    @Excel(name = "以前金额")
    private BigDecimal oldBalance;

    /** 现在金额 */
    @Excel(name = "现在金额")
    private BigDecimal newBalance;

    /** 金额 */
    @Excel(name = "金额")
    private BigDecimal balance;

    /** 创建时间 */
    private Date createTime;

    /** 创建人id */
    @Excel(name = "创建人id")
    private Long createUserId;
}
