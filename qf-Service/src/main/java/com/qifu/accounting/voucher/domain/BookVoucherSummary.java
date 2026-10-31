package com.qifu.accounting.voucher.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
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
 * 摘要对象 book_voucher_summary
 *
 *  YM
 * 07-17
 */
@Data
@EqualsAndHashCode
@NoArgsConstructor
@Accessors(chain = true)
@TableName("book_voucher_summary")
public class BookVoucherSummary implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 摘要 */
    @Excel(name = "摘要")
    private String summary;

    /** 是否默认 */
    @Excel(name = "是否默认")
    private Boolean isDefault;

    /** 账套id */
    @Excel(name = "账套id")
    private Long bookId;
}
