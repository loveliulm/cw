package com.qifu.accounting.voucher.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.qifu.accounting.common.enums.BalanceDirectionEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import com.qifu.framework.aspectj.lang.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 凭证明细对象 book_voucher_record
 *
 *  YM
 * 07-13
 */
@Data
@EqualsAndHashCode
@NoArgsConstructor
@Accessors(chain = true)
@TableName("book_voucher_record")
public class BookVoucherRecord implements Serializable {

private static final long serialVersionUID=1L;


    /** $column.columnComment */
    @TableId(value = "voucher_record_id", type = IdType.AUTO)
    private Long voucherRecordId;

    /** 凭证id */
    @Excel(name = "凭证id")
    private Long voucherId;

    /** 摘要 */
    @Excel(name = "摘要")
    @NotBlank(message = "摘要不能为空")
    @Size(min = 0, max = 30, message = "摘要长度不能超过30个字符")
    private String summary;

    /** 科目id */
    @Excel(name = "科目id")
    private Long subjectId;

    /** 余额方向：关键字 */
    @Excel(name = "余额方向：关键字")
    private BalanceDirectionEnum balanceDirection;

    /** 余额方向：借贷 */
    @Excel(name = "余额方向：借贷")
    private BalanceDirectionEnum balanceDirectionText;

    /** 借方金额 */
    @Excel(name = "借方金额")
    private BigDecimal debit;

    /** 贷方金额 */
    @Excel(name = "贷方金额")
    private BigDecimal credit;

    /** 单价 */
    @Excel(name = "单价")
    private BigDecimal price;

    /** 数量 */
    @Excel(name = "数量")
    private Long quantity;

    /** 数量核算单位 */
    @Excel(name = "数量核算单位")
    private String quantityUnit;

    /** 辅助核算供应商id */
    private Long supplierId;

    /** 辅助核算客户id */
    private Long customerId;

    /** 辅助核算项目id */
    private Long projectId;

    /** 辅助核算部门id */
    private Long departmentId;

    /** 辅助核算人员id */
    private Long personId;

    /** 辅助核算存货id */
    private Long stockId;

    /** 汇率 */
    @Excel(name = "汇率")
    private BigDecimal rete;

    /** 外汇金额 */
    @Excel(name = "外汇金额")
    private BigDecimal currentcy;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updateTime;

    private String delFlag;
}
