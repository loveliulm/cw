package com.qifu.accounting.voucher.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.qifu.accounting.common.enums.SectionEnum;
import com.qifu.accounting.common.enums.VoucherModelType;
import com.qifu.accounting.voucher.domain.vo.BookVoucherModelRecordVo;
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

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 凭证模板对象 book_voucher_model
 *
 *  YM
 * 07-16
 */
@Data
@EqualsAndHashCode
@NoArgsConstructor
@Accessors(chain = true)
@TableName("book_voucher_model")
public class BookVoucherModel implements Serializable {

private static final long serialVersionUID=1L;


    /** 凭证模板id */
    @TableId(value = "model_id", type = IdType.AUTO)
    private Long modelId;

    /** 账套id */
    @Excel(name = "账套id")
    private Long bookId;

    /** 附件数量 */
    @Excel(name = "附件数量")
    private Long attachmentCount;

    /** 是否保证金额 */
    @Excel(name = "是否保存金额")
    private Boolean isAmount;

    /** 名称 */
    @Excel(name = "名称")
    @NotBlank(message = "名称不能为空")
    @Size(min = 0, max = 30, message = "名称长度不能超过30个字符")
    private String name;

    /** 创建时间 */
    private Date createTime;

    /** 更新时间 */
    private Date updateTime;

    /** 创建人id */
    @Excel(name = "创建人id")
    private Long createUserId;

    /** 0：正常1：删除 */
    private String delFlag;

    private VoucherModelType type;

    private SectionEnum section;

    private String remark;


    /*----临时字段----*/

    @TableField(exist = false)
    List<BookVoucherModelRecord> details;

    @TableField(exist = false)
    List<BookVoucherModelRecordVo> detail;

    @TableField(exist = false)
    private BigDecimal creditTotal;

    @TableField(exist = false)
    private BigDecimal debitTotal;
}
