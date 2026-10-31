package com.qifu.accounting.sys.domain.vo;

import lombok.Data;

/**
 *  yangyang
 * @date 2020/7/13 17:48
 */
@Data
public class BookAuxiliaryVo {

    private Long auxiliaryId;

    /** 名称 */
    private String name;

    private String auxiliaryType;

    private String auxiliaryTypeText;
}
