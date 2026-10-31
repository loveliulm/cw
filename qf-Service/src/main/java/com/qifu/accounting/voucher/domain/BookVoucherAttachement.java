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
 * 凭证附件对象 book_voucher_attachement
 *
 *  YM
 * 07-13
 */
@Data
@EqualsAndHashCode
@NoArgsConstructor
@Accessors(chain = true)
@TableName("book_voucher_attachement")
public class BookVoucherAttachement implements Serializable {

private static final long serialVersionUID=1L;


    /** $column.columnComment */
    @TableId(value = "attachment_id", type = IdType.AUTO)
    private Long attachmentId;

    /** $column.columnComment */
    @Excel(name = "${comment}" , readConverterExp = "$column.readConverterExp()")
    private Long voucherId;

    /** 路径 */
    @Excel(name = "路径")
    private String name;

    /** 创建时间 */
    private Date createTime;
}
