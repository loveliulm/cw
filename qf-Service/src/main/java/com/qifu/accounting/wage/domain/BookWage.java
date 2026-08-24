package com.qifu.accounting.wage.domain;

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
import java.math.BigDecimal;
import java.util.Date;
import com.qifu.framework.web.domain.BaseEntity;

/**
 * 工资对象 book_wage
 *
 *  YM
 * 07-22
 */
@Data
@EqualsAndHashCode
@NoArgsConstructor
@Accessors(chain = true)
@TableName("book_wage")
public class BookWage implements Serializable {

    private static final long serialVersionUID=1L;


    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 账套id */
    private Long bookId;

    /** 部门id */
    @Excel(name = "部门编号")
    private Long deptmentId;

    /** 部门名称 */
    @Excel(name = "部门名称")
    private String deptmentName;

    /** 人员id */
    @Excel(name = "人员编号")
    private Long personId;

    /** 人员名称 */
    @Excel(name = "人员名称")
    private String personName;

    /** 身份证号 */
    @Excel(name = "身份证号")
    private String identityCard;

    /** 工资 */
    @Excel(name = "工资")
    private BigDecimal wage;

    /** 奖金 */
    @Excel(name = "奖金")
    private BigDecimal bonus;

    /** 津贴 */
    @Excel(name = "津贴")
    private BigDecimal allowance;

    /** 补贴 */
    @Excel(name = "补贴")
    private BigDecimal subsidy;

    /** 缺勤扣款 */
    @Excel(name = "缺勤扣款")
    private BigDecimal absent;

    /** 应发工资 */
    @Excel(name = "应发工资")
    private BigDecimal grossPay;

    /** 基本养老保险 */
    @Excel(name = "基本养老保险")
    private BigDecimal endowmentInsurance;

    /** 基本医疗保险 */
    @Excel(name = "基本医疗保险")
    private BigDecimal medicareInsurance;

    /** 失业保险 */
    @Excel(name = "失业保险")
    private BigDecimal unemployedInsurance;

    /** 大病保险 */
    @Excel(name = "大病保险")
    private BigDecimal sicknessInsurance;

    /** 住房公积金 */
    @Excel(name = "住房公积金")
    private BigDecimal housingFund;

    /** 其他扣除 */
    @Excel(name = "其他扣除")
    private BigDecimal otherDeductions;

    /** 累计收入 */
    @Excel(name = "累计收入")
    private BigDecimal accIncome;

    /** 累计专项扣除 */
    @Excel(name = "累计专项扣除")
    private BigDecimal accSpecialDeduction;

    /** 累计子女教育扣除 */
    @Excel(name = "累计子女教育扣除")
    private BigDecimal accChildEduDeduction;

    /** 累计租房贷款利息扣除 */
    @Excel(name = "累计租房贷款利息扣除")
    private BigDecimal accHousingLoansDeduction;

    /** 累计住房租金支出扣除 */
    @Excel(name = "累计住房租金支出扣除")
    private BigDecimal accHousingRentDeduction;

    /** 累计赡养老人扣除 */
    @Excel(name = "累计赡养老人扣除")
    private BigDecimal accElderlyDeduction;

    /** 累计继续教育支出扣除 */
    @Excel(name = "累计继续教育支出扣除")
    private BigDecimal accContinuingEduDeduction;

    /** 累计专项附加扣除合计 */
    @Excel(name = "累计专项附加扣除合计")
    private BigDecimal accTotalSpecialDeduction;

    /** 累计其他扣除 */
    @Excel(name = "累计其他扣除")
    private BigDecimal accOtherDeduction;

    /** 累计应缴个税 */
    @Excel(name = "累计应缴个税")
    private BigDecimal accTaxPayable;

    /** 累计已缴个税 */
    @Excel(name = "累计已缴个税")
    private BigDecimal accTaxesPaid;

    /** 本月应缴个税 */
    @Excel(name = "本月应缴个税")
    private BigDecimal withHoldingTax;

    /** 实发工资 */
    @Excel(name = "实发工资")
    private BigDecimal netPay;

    /** 创建时间 */
    private Date createTime;

    /** 更新时间 */
    private Date updateTime;

    /** 账期 */
    @Excel(name = "账期" , width = 30, dateFormat = "yyyy-MM")
    @JsonFormat(pattern = "yyyy-MM")
    private Date yearPeriod;

    /** 0:正常2：删除 */
    private String delFlag;
}
