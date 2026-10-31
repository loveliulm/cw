package com.qifu.accounting.sys.domain.vo;

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
import java.math.BigDecimal;

/**
 * 科目对象 book_subject
 *
 *  YM
 * 07-03
 */
@Data
@EqualsAndHashCode
@NoArgsConstructor
@Accessors(chain = true)
@TableName("book_subject")
public class BookSubjectVo implements Serializable {

private static final long serialVersionUID=1L;


    /** 科目id */
    @TableId(value = "subject_id", type = IdType.AUTO)
    private Long subjectId;

    /** 科目名称 */
    @Excel(name = "科目名称")
    private String name;

    private String fullName;

    private String subjectNameBy;

    /** 科目编码 */
    @Excel(name = "科目编码")
    private String code;

    /** 科目类型id */
    @Excel(name = "科目类型id")
    private Long typeId;

    /** 科目类目id */
    @Excel(name = "科目类目id")
    private Long categoryId;

    /** 是否是模板科目 */
    @Excel(name = "是否是模板科目")
    private Boolean isModel;

    /** 余额方向：关键字 */
    @Excel(name = "余额方向：关键字")
    private BalanceDirectionEnum balanceDirection;

    /** 是否现金流量项目 */
    @Excel(name = "是否现金流量项目")
    private Boolean isCashFlow;

    /** 数量核算启用停用 */
    @Excel(name = "数量核算启用停用")
    private Boolean isQuantity;

    /** 数量核算单位 */
    @Excel(name = "数量核算单位")
    private String quantityUnit;

    /** 是否启用辅组核算 */
    @Excel(name = "是否启用辅组核算")
    private Boolean isAuxiliary;

    /** 辅组核算：是否供应商 */
    @Excel(name = "辅组核算：是否供应商")
    private Boolean isSupplier;

    /** 辅组核算：是否项目 */
    @Excel(name = "辅组核算：是否项目")
    private Boolean isProject;

    /** 辅组核算：是否存货 */
    @Excel(name = "辅组核算：是否存货")
    private Boolean isStock;

    /** 辅组核算：是否部门 */
    @Excel(name = "辅组核算：是否部门")
    private Boolean isDepartment;

    /** 辅组核算：是否客户 */
    @Excel(name = "辅组核算：是否客户")
    private Boolean isCustomer;

    /** 辅组核算：是否个人 */
    @Excel(name = "辅组核算：是否个人")
    private Boolean isPerson;

    /** 是否启用外币 */
    @Excel(name = "是否启用外币")
    private Boolean isForCurrency;

    private Boolean isLeaf;

}
