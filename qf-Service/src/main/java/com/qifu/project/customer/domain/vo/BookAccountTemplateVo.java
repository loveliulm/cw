package com.qifu.project.customer.domain.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.qifu.framework.aspectj.lang.annotation.Excel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 *  yangyang
 * @date 2020/7/4 10:49
 */
@Data
@EqualsAndHashCode
@NoArgsConstructor
@Accessors(chain = true)
@TableName("book_account_template")
public class BookAccountTemplateVo {
    @TableId(value = "account_template_id", type = IdType.AUTO)
    private Long accountTemplateId;

    /** 会计制度名称 */
    @Excel(name = "会计制度名称")
    private String name;
}
