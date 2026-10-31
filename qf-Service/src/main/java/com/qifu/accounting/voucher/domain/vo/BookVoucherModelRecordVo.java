package com.qifu.accounting.voucher.domain.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.qifu.accounting.common.enums.BalanceDirectionEnum;
import com.qifu.accounting.common.enums.FiledCodeEnum;
import com.qifu.accounting.sys.domain.vo.BookAuxiliaryVo;
import com.qifu.framework.aspectj.lang.annotation.Excel;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 凭证明细对象 book_voucher_record
 *
 *  YM
 * 07-13
 */
@Data
public class BookVoucherModelRecordVo implements Serializable {

    private static final long serialVersionUID=1L;

    /** 模板明细id */
    @TableId(value = "model_record_id", type = IdType.AUTO)
    private Long modelRecordId;

    /** 模板id */
    @Excel(name = "模板id")
    private Long modelId;

    /** 摘要 */
    private String summary;

    /** 科目id */
    private Long subjectId;

    private String subjectName;

    private String subjectNameBy;

    /** 余额方向：关键字 */
    private BalanceDirectionEnum balanceDirection;

    /** 余额方向：借贷 */
    private BalanceDirectionEnum balanceDirectionText;

    /** 借方金额 */
    private BigDecimal debit;

    /** 贷方金额 */
    private BigDecimal credit;

    /** 单价 */
    private BigDecimal price;

    /** 数量 */
    private Long quantity;

    /** 数量核算单位 */
    @Excel(name = "数量核算单位")
    private String quantityUnit;

    /** 汇率 */
    private BigDecimal rete;

    /** 外汇金额 */
    private BigDecimal currentcy;

    private String currencyCode;

    /*计提工资、发放工资的取数来源code*/
    private FiledCodeEnum filedCode;

    /*计提工资、发放工资的取数来源*/
    private FiledCodeEnum filedText;

    private List<BookAuxiliaryVo> auxiliarys;
}
