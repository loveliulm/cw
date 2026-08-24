package com.qifu.accounting.voucher.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.qifu.accounting.common.enums.VoucherTypeEnum;
import com.qifu.accounting.voucher.domain.vo.BookVoucherRecordVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import com.qifu.framework.aspectj.lang.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 凭证总对象 book_voucher
 *
 *  YM
 * 07-13
 */
@Data
@EqualsAndHashCode
@NoArgsConstructor
@Accessors(chain = true)
@TableName("book_voucher")
public class BookVoucher implements Serializable {

private static final long serialVersionUID=1L;


    /** 凭证id */
    @TableId(value = "voucher_id", type = IdType.AUTO)
    private Long voucherId;

    private Long bookId;

    /** 凭证字 */
    @Excel(name = "凭证字")
    private String voucherWord;

    /** 凭证数 */
    @Excel(name = "凭证数")
    private Long voucherNo;

    /** 凭证编码 */
    @Excel(name = "凭证编码")
    private String voucherCode;

    /** 凭证日期 */
    @Excel(name = "凭证日期" , width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date voucherDate;

    /** 账期 */
    @JsonFormat(pattern = "yyyy-MM")
    @NotNull(message = "账期不能为空")
    private Date yearPeriod;

    /** 贷方金额合计 */
    @Excel(name = "贷方金额合计")
    @NotNull(message = "贷方金额合计不能为空")
    private BigDecimal creditTotal;

    /** 借方金额合计 */
    @Excel(name = "借方金额合计")
    @NotNull(message = "借方金额合计不能为空")
    private BigDecimal debitTotal;

    /** 合计金额大写 */
    @Excel(name = "合计金额大写")
    private String totalAmount;

    /** 附件数 */
    @Excel(name = "附件数")
    private Long attachmentCount;

    /** 凭证类型 */
    @Excel(name = "凭证类型")
    private VoucherTypeEnum voucherType;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updateTime;

    /** 创建人id */
    @Excel(name = "创建人id")
    private Long createUserId;

    /** 创建人姓名 */
    @Excel(name = "创建人姓名")
    private String createUserName;

    private String delFlag;

    /* 审核字段，0:未审核，1：已审核 */
    private Integer status;

    private Long typeClassId;

    /*凭证明细*/
    @TableField(exist = false)
    @Valid
    private List<BookVoucherRecord> details;

    /*凭证明细*/
    @TableField(exist = false)
    private List<BookVoucherRecordVo> detail;

    public void setVoucherCode(){
        this.voucherCode = this.voucherWord + '-' +voucherNo;
    }
}
