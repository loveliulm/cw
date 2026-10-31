package com.qifu.accounting.sys.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.qifu.accounting.common.enums.BalanceDirectionEnum;
import com.qifu.framework.aspectj.lang.annotation.Excel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

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
public class BookSubject implements Serializable {

private static final long serialVersionUID=1L;


    /** 科目id */
    @TableId(value = "subject_id", type = IdType.AUTO)
    private Long subjectId;

    /** 科目名称 */
    @Excel(name = "科目名称")
    @NotBlank(message = "科目名称不能为空")
    @Size(min = 0, max = 16, message = "科目名称长度不能超过16个字符")
    private String name;

    /** 科目名称全称 */
    @Excel(name = "科目名称")
    private String fullName;

    /** 科目编码 */
    @Excel(name = "科目编码")
    private String code;

    /** 账套id */
    @Excel(name = "账套id")
    private Long bookId;

    /** 根id */
    @Excel(name = "根id")
    private Long subjectModelId;

    /** 科目类型id */
    @Excel(name = "科目类型id")
    private Long typeId;

    /** 科目类目id */
    @Excel(name = "科目类目id")
    private Long categoryId;

    /** 科目余额 */
    @Excel(name = "科目余额")
    private BigDecimal balance;

    /** 科目余额启用停用 */
    @Excel(name = "科目余额启用停用")
    private Boolean balanceChecked;

    /** 余额方向：关键字 */
    @Excel(name = "余额方向：关键字")
    private BalanceDirectionEnum balanceDirection;

    /** 余额方向：借贷 */
    @Excel(name = "余额方向：借贷")
    private BalanceDirectionEnum balanceDirectionText;

    /** 期初余额 */
    @Excel(name = "期初余额")
    private BigDecimal balanceInit;

    /** 期初余额启用停用 */
    @Excel(name = "期初余额启用停用")
    private Boolean balanceInitChecked;

    /** 期初余额时间 */
    @Excel(name = "期初余额时间" , width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date balanceInitDate;

    /** 本年累计贷方 */
    @Excel(name = "本年累计贷方")
    private BigDecimal credit;

    /** 本年累计贷方的启用停用 */
    @Excel(name = "本年累计贷方的启用停用")
    private Boolean creditChecked;

    /** 本年累计借方 */
    @Excel(name = "本年累计借方")
    private BigDecimal debit;

    /** 本年累计借方启用停用 */
    @Excel(name = "本年累计借方启用停用")
    private Boolean debitChecked;

    private BigDecimal yearBalance;

    /** 已启用，已停用 */
    @Excel(name = "已启用，已停用")
    private Boolean enable;

    /** 是否可删除 */
    @Excel(name = "是否可删除")
    private Boolean deletable;

    /** 是否是模板科目 */
    @Excel(name = "是否是模板科目")
    private Boolean isModel;

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

    /*是否可以修改*/
    private Boolean isEdit;

    /** 层级 */
    @Excel(name = "层级")
    private Byte level;

    /** 是否是最儿子节点 */
    private Boolean isLeaf;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    public void setNewLevel(){
        if(this.code.length() == 4){
            this.level = 1;
        }else if(this.code.length() == 6){
            this.level = 2;
        }else if(this.code.length() == 8){
            this.level = 3;
        }else if(this.code.length() == 10){
            this.level = 4;
        }else {
            this.level = null;
        }
    }

    public void setNewYearBalance(){
        BigDecimal balanceInit1 = this.balanceInit == null ? new BigDecimal(0.00) : this.balanceInit;
        BigDecimal debit1 = this.debit == null ? new BigDecimal(0.00) : this.debit;
        BigDecimal credit1 = this.credit == null ? new BigDecimal(0.00) : this.credit;
        if(BalanceDirectionEnum.贷.name().equals(this.balanceDirectionText.name())){
            this.yearBalance = balanceInit1.add(debit1).subtract(credit1);
        }
        if(BalanceDirectionEnum.借.name().equals(this.balanceDirectionText.name())){
            this.yearBalance = balanceInit1.add(credit1).subtract(debit1);
        }
    }
}
