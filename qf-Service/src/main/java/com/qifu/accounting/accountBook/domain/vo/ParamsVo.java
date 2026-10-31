package com.qifu.accounting.accountBook.domain.vo;

import lombok.Data;

import java.util.Date;

/**
 *  yangyang
 * @date 2020/8/26 17:04
 */
@Data
public class ParamsVo {
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

    /*发生额是否为零*/
    private Boolean showBalNotZero;

    /*开始级次*/
    private Integer startLevel;

    /*结束级次*/
    private Integer endLevel;


    //科目编号规则：目前支持四级
    public static final Integer[] SUBJECT_CODE_RULE = {4,6,8,10};
}
