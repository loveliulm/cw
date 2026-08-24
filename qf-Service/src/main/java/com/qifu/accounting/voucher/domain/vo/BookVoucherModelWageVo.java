package com.qifu.accounting.voucher.domain.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.qifu.accounting.common.enums.SectionEnum;
import com.qifu.accounting.common.enums.VoucherModelType;
import com.qifu.accounting.voucher.domain.BookVoucherModelRecord;
import com.qifu.accounting.voucher.domain.BookVoucherRecord;
import com.qifu.framework.aspectj.lang.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 *  yangyang
 * @date 2020/8/7 11:11
 */
@Data
public class BookVoucherModelWageVo {
    /** 凭证模板id */
    @TableId(value = "model_id", type = IdType.AUTO)
    private Long modelId;

    /** 账套id */
    @Excel(name = "账套id")
    private Long bookId;

    /** 名称 */
    @Excel(name = "名称")
    private String name;

    /** 更新时间 */
    private Date updateTime;

    @TableField(exist = false)
    List<BookVoucherModelWageRecordVo> detail;

    /*凭证明细*/
    @TableField(exist = false)
    private List<BookVoucherRecord> details;

    private VoucherModelType type;

    private SectionEnum section;

    private String remark;

    @TableField(exist = false)
    private BigDecimal creditTotal;

    @TableField(exist = false)
    private BigDecimal debitTotal;

    @TableField(exist = false)
    private Boolean isAuxiliaryFinished;
}
