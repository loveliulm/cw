package com.qifu.accounting.voucher.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.qifu.accounting.common.enums.BalanceDirectionEnum;
import com.qifu.accounting.common.enums.FiledCodeEnum;
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
 * 凭证模板明细对象 book_voucher_model_record
 *
 *  YM
 * 07-16
 */
@Data
@EqualsAndHashCode
@NoArgsConstructor
@Accessors(chain = true)
@TableName("book_voucher_model_record")
public class BookVoucherModelRecord implements Serializable {

private static final long serialVersionUID=1L;


    /** 模板明细id */
    @TableId(value = "model_record_id", type = IdType.AUTO)
    private Long modelRecordId;

    /** 模板id */
    @Excel(name = "模板id")
    private Long modelId;

    /** 摘要 */
    @Excel(name = "摘要")
    private String summary;

    /** 科目id */
    @Excel(name = "科目id")
    private Long subjectId;

    /** 余额方向：关键字 */
    @Excel(name = "余额方向：关键字")
    private BalanceDirectionEnum balanceDirection;

    /** 余额方向：关键字 */
    @Excel(name = "余额方向：关键字")
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
    @Excel(name = "辅助核算供应商id")
    private Long supplierId;

    /** 辅助核算客户id */
    @Excel(name = "辅助核算客户id")
    private Long customerId;

    /** 辅助核算项目id */
    @Excel(name = "辅助核算项目id")
    private Long projectId;

    /** 辅助核算部门id */
    @Excel(name = "辅助核算部门id")
    private Long departmentId;

    /** 辅助核算人员id */
    @Excel(name = "辅助核算人员id")
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
    private Date createTime;

    /** 更新时间 */
    @Excel(name = "更新时间" , width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /** 删除字段0：正常1：删除 */
    private String delFlag;

    /*计提工资、发放工资的取数来源code*/
    private FiledCodeEnum filedCode;

    /*计提工资、发放工资的取数来源*/
    private FiledCodeEnum filedText;
}
