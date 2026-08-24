package com.qifu.accounting.accountBook.domain.vo;

import lombok.Data;

/**
 *  yangyang
 * @date 2020/9/10 13:59
 */
@Data
public class AuxiliaryTypeVo {

    private Long auxiliaryTypeId;

    private String auxiliaryTypeName;

    public AuxiliaryTypeVo( Long auxiliaryTypeId,String auxiliaryTypeName){
        this.auxiliaryTypeId = auxiliaryTypeId;
        this.auxiliaryTypeName = auxiliaryTypeName;
    }
}
