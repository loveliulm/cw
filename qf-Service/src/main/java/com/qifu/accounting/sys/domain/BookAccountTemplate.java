package com.qifu.accounting.sys.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.qifu.framework.aspectj.lang.annotation.Excel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 会计制度对象 book_account_template
 *
 *  YM
 * 07-03
 */
@Data
@EqualsAndHashCode
@NoArgsConstructor
@Accessors(chain = true)
@TableName("book_account_template")
public class BookAccountTemplate implements Serializable {

private static final long serialVersionUID=1L;


    /** 会计制度id */
    @TableId(value = "account_template_id", type = IdType.AUTO)
    private Long accountTemplateId;

    /** 会计制度名称 */
    @Excel(name = "会计制度名称")
    private String name;

    private Boolean status;
}
