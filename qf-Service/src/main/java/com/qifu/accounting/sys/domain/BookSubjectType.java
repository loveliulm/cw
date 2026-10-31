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
 * 科目类型对象 book_subject_type
 *
 *  YM
 * 07-03
 */
@Data
@EqualsAndHashCode
@NoArgsConstructor
@Accessors(chain = true)
@TableName("book_subject_type")
public class BookSubjectType implements Serializable {

private static final long serialVersionUID=1L;


    @TableId(value = "type_id", type = IdType.AUTO)
    private Long typeId;

    /** $column.columnComment */
    @Excel(name = "科目类型名称")
    private String name;

    /*会计制度*/
    private Long accountTemplateId;
}
