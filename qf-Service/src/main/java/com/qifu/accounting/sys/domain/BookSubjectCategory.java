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
 * 科目类目对象 book_subject_category
 *
 *  YM
 * 07-03
 */
@Data
@EqualsAndHashCode
@NoArgsConstructor
@Accessors(chain = true)
@TableName("book_subject_category")
public class BookSubjectCategory implements Serializable {

private static final long serialVersionUID=1L;


    @TableId(value = "category_id", type = IdType.AUTO)
    private Long categoryId;

    /** 科目类目名称 */
    @Excel(name = "科目类目名称")
    private String name;

    /** 科目类型id */
    @Excel(name = "科目类型id")
    private Long typeId;
}
