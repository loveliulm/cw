package com.qifu.accounting.accountBook.domain.vo;

import lombok.Data;

import java.util.Date;

/**
 * 辅助核算明细账
 *  yangyang
 * @date 2020/9/8 10:37
 */
@Data
public class AuxiliaryAccParamsVo {

    /*科目id*/
    private Long subjectId;

    /*账套id*/
    private Long bookId;

    /*开始账期*/
    private Date startPeriod;

    /*结束账期*/
    private Date endPeriod;

    /*当前账期*/
    private Date currentPeriod;

    /*辅助核算类型*/
    private Long auxiliaryTypeId;

    /*辅助核算id*/
    private Long auxiliaryId;

    /*发生额是否为零*/
    private Boolean showBalNotZero;
}
