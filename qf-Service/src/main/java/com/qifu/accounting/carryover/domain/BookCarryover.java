package com.qifu.accounting.carryover.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.qifu.accounting.common.enums.CarryoverCodeEnum;
import com.qifu.accounting.voucher.domain.BookVoucher;
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
import java.util.List;

import com.qifu.framework.web.domain.BaseEntity;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 结转对象 book_carryover
 *
 *  YM
 * 07-23
 */
@Data
@EqualsAndHashCode
@NoArgsConstructor
@Accessors(chain = true)
@TableName("book_carryover")
public class BookCarryover implements Serializable {

private static final long serialVersionUID=1L;


    /** 结转id */
    @TableId(value = "carryover_id", type = IdType.AUTO)
    private Long carryoverId;

    /** 账套id */
    private Long bookId;

    /** 结转code */
    private CarryoverCodeEnum code;

    /** 结转text */
    @NotBlank(message = "结转名称不能为空")
    @Size(min = 0, max = 30, message = "结转名称长度不能超过30个字符")
    private String text;

    /** 测算金额。默认：“？” */
    private String money;

    /** 是否是模板 */
    private Boolean isModel;

    /** 是否显示规则 */
    private Boolean showRule;

    /** 0:正常2:删除 */
    private String delFlag;

    /** 显示，隐藏 */
    @Excel(name = "显示，隐藏")
    private Boolean status;

    /** 结转损益字段：收入和损益是否分开，默认null */
    @Excel(name = "结转损益字段：收入和损益是否分开，默认null")
    private Boolean profitLossOpen;

    /** 创建时间 */
    private Date createTime;

    /** 更新时间 */
    private Date updateTime;

    /*结转规则明细*/
    @TableField(exist = false)
    @Valid
    private List<BookCarryoverRule> rules;

    /*借贷金额是否等于*/
    @TableField(exist = false)
    private Boolean isDBBalanceEquals = true;

    @TableField(exist = false)
    private BookVoucher voucher1;

    @TableField(exist = false)
    private BookVoucher voucher2;
}
