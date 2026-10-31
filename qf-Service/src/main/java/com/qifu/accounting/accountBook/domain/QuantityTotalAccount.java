package com.qifu.accounting.accountBook.domain;

import com.qifu.framework.aspectj.lang.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 数量金额总账
 *  yangyang
 * @date 2020/9/10 13:41
 */
@Data
public class QuantityTotalAccount {

    private Long subjectId;

    /*科目编码*/
    @Excel(name = "科目编码")
    private String subjectCode;

    /*科目名称*/
    @Excel(name = "科目名称")
    private String subjectName;

    /*科目计量单位*/
    @Excel(name = "单位")
    private String subjectUnit;

    /*科目层级*/
    private Integer level;

    /*期初余额(数量)*/
    @Excel(name = "期初余额(数量)")
    private Integer quantityStartBalance;

    /*期初余额(单价)*/
    @Excel(name = "期初余额(单价)")
    private BigDecimal priceStartBalance;

    /*期初余额*/
    @Excel(name = "期初余额(金额)")
    private BigDecimal startBalance;


    /*借方发生额(数量)*/
    @Excel(name = "借方发生额(数量)")
    private Integer quantityPeriodDebit;

    /*借方发生额(单价)*/
    @Excel(name = "借方发生额(单价)")
    private BigDecimal pricePeriodDebit;

    /*借方发生额(金额)*/
    @Excel(name = "借方发生额(金额)")
    private BigDecimal periodDebit;

    /*贷方发生额(数量)*/
    @Excel(name = "贷方发生额(数量)")
    private Integer quantityPeriodCredit;

    /*贷方发生额(单价)*/
    @Excel(name = "贷方发生额(单价)")
    private BigDecimal pricePeriodCredit;

    /*贷方发生额(金额)*/
    @Excel(name = "贷方发生额(金额)")
    private BigDecimal periodCredit;

    /*本年累计借方(单价)*/
    @Excel(name = "本年累计借方(数量)")
    private Integer quantityYearDebit;

    /*本年累计借方(单价)*/
    @Excel(name = "本年累计借方(单价)")
    private BigDecimal priceYearDebit;


    /*本年累计借方(金额)*/
    @Excel(name = "本年累计借方(金额)")
    private BigDecimal yearDebit;

    /*本年累计借方(单价)*/
    @Excel(name = "本年累计借方(数量)")
    private Integer quantityYearCredit;

    /*本年累计借方(单价)*/
    @Excel(name = "本年累计借方(单价)")
    private BigDecimal priceYearCredit;

    /*本年累计贷方(金额)*/
    @Excel(name = "本年累计贷方(金额)")
    private BigDecimal yearCredit;

    /*期末余额(单价)*/
    @Excel(name = "期末余额(数量)")
    private Integer quantityEndBalance;

    /*期末余额(单价)*/
    @Excel(name = "期末余额(单价)")
    private BigDecimal priceEndBalance;

    /*期末余额*/
    @Excel(name = "期末余额(金额)")
    private BigDecimal endBalance;
}
