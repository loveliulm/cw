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
import java.util.Date;
import com.qifu.framework.web.domain.BaseEntity;

/**
 * 凭证模板明细公用对象 book_voucher_model_record_pub
 *
 *  YM
 * 08-07
 */
@Data
@EqualsAndHashCode
@NoArgsConstructor
@Accessors(chain = true)
@TableName("book_voucher_model_record_pub")
public class BookVoucherModelRecordPub implements Serializable {

private static final long serialVersionUID=1L;


    /** 模板明细id */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 模板id */
    @Excel(name = "模板id")
    private Long pubId;

    /** 摘要 */
    @Excel(name = "摘要")
    private String summary;

    /** 科目id */
    @Excel(name = "科目id")
    private String subjectCode;

    /** 余额方向：关键字 */
    @Excel(name = "余额方向：关键字")
    private BalanceDirectionEnum balanceDirection;

    @Excel(name = "余额方向：关键字")
    private BalanceDirectionEnum balanceDirectionText;

    /*计提工资、发放工资的取数来源code*/
    private FiledCodeEnum filedCode;

    /*计提工资、发放工资的取数来源*/
    private FiledCodeEnum filedText;
}
