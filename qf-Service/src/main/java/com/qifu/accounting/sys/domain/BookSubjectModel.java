package com.qifu.accounting.sys.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.qifu.accounting.common.enums.BalanceDirectionEnum;
import com.qifu.framework.aspectj.lang.annotation.Excel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 科目模板对象 book_subject_model
 *
 *  YM
 * 07-03
 */
@Data
@EqualsAndHashCode
@NoArgsConstructor
@Accessors(chain = true)
@TableName("book_subject_model")
public class BookSubjectModel implements Serializable {

private static final long serialVersionUID=1L;


    /** 科目模板id */
    @TableId(value = "model_id", type = IdType.AUTO)
    private Long modelId;

    /** 科目名称 */
    @Excel(name = "科目名称")
    private String name;

    /** 科目编码 */
    @Excel(name = "科目编码")
    private String code;

    /** 类型id */
    @Excel(name = "类型id")
    private Long typeId;

    /** 类目id */
    @Excel(name = "类目id")
    private Long categoryId;

    /** 余额方向：关键字 */
    @Excel(name = "余额方向：关键字")
    private BalanceDirectionEnum balanceDirection;

    /** 余额方向 */
    @Excel(name = "余额方向")
    private BalanceDirectionEnum balanceDirectionText;

    /** 层级 */
    @Excel(name = "层级")
    private Byte level;

    /*会计制度*/
    private Long accountTemplateId;

    /** 期初余额启用停用 */
    @Excel(name = "期初余额启用停用")
    private Boolean balanceInitChecked;

    /** 本年累计贷方的启用停用 */
    @Excel(name = "本年累计贷方的启用停用")
    private Boolean creditChecked;

    /** 本年累计借方启用停用 */
    @Excel(name = "本年累计借方启用停用")
    private Boolean debitChecked;

    /** 是否是最儿子节点 */
    private Boolean isLeaf;
}
