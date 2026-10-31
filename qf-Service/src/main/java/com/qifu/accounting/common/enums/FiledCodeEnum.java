package com.qifu.accounting.common.enums;

/**
 *  yangyang
 * @date 2020/8/7 9:08
 */
public enum FiledCodeEnum {
    MEDICARE_INSURANCE,ENDOWMENT_INSURANCE,UNEMPLOYED_INSURANCE,SICKNESS_INSURANCE,HOUSING_FUND,WITH_HOLDING_TAX,NET_PAY,GROSS_PAY,OTHER_DEDUCTIONS,
    基本养老保险费,基本医疗保险费,失业保险费,大病保险,住房公积金,本月应缴个税,实发工资,应发工资,其他扣除;

    public static FiledCodeEnum getFiledText(FiledCodeEnum code){
        FiledCodeEnum filedText = null;
        switch (code){
            case MEDICARE_INSURANCE:
                filedText = 基本养老保险费;
                break;
            case ENDOWMENT_INSURANCE:
                filedText = 基本医疗保险费;
                break;
            case UNEMPLOYED_INSURANCE:
                filedText = 失业保险费;
                break;
            case SICKNESS_INSURANCE:
                filedText = 大病保险;
                break;
            case HOUSING_FUND:
                filedText = 住房公积金;
                break;
            case WITH_HOLDING_TAX:
                filedText = 本月应缴个税;
                break;
            case NET_PAY:
                filedText = 实发工资;
                break;
            case GROSS_PAY:
                filedText = 应发工资;
                break;
            case OTHER_DEDUCTIONS:
                filedText = 其他扣除;
                break;
        }
        return filedText;
    }
}
