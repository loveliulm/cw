package com.qifu.accounting.common.enums;

/**
 *  yangyang
 * @date 2020/7/6 13:46
 */
public enum BalanceDirectionEnum {
    CREDIT,DEBIT,EQUALS,借,贷,平;

    public static BalanceDirectionEnum ralanceDirection(BalanceDirectionEnum value){
        if(BalanceDirectionEnum.DEBIT == value){
            return 借;
        }
        if(BalanceDirectionEnum.CREDIT == value){
            return 贷;
        }
        return null;
    }
}
