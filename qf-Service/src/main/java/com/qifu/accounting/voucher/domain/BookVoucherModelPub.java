package com.qifu.accounting.voucher.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.qifu.accounting.common.enums.SectionEnum;
import com.qifu.accounting.common.enums.VoucherModelType;
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
 * 凭证模板公用对象 book_voucher_model_pub
 *
 *  YM
 * 08-07
 */
@Data
@EqualsAndHashCode
@NoArgsConstructor
@Accessors(chain = true)
@TableName("book_voucher_model_pub")
public class BookVoucherModelPub implements Serializable {

private static final long serialVersionUID=1L;


    /** 凭证模板id */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 账套id */
    @Excel(name = "账套id")
    private Long templateId;

    /** 名称 */
    @Excel(name = "名称")
    private String name;

    /** $column.columnComment */
    @Excel(name = "名称")
    private SectionEnum section;

    /** 模板说明 */
    @Excel(name = "模板说明")
    private String remark;

    private VoucherModelType type;
}
