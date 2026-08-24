package com.qifu.accounting.report.domain.vo;

import com.qifu.common.utils.DateUtils;
import lombok.Data;

import java.util.Date;

/**
 *  yangyang
 * @date 2020/9/3 15:07
 */
@Data
public class ReportAssetsParams {

    /*账套id*/
    private Long bookId;

    /*科目编码*/
    private String subjectCode;

    /*当前账期*/
    private Date currentPeriod;

    /*true:重分类 false:不重分类*/
    private Boolean isClassify;

    /*上一年十二月账期*/
    private Date lastYearPeriod;

    /*行次*/
    private Integer rowNum;


    public void resetLastYearPeriod(){
        this.lastYearPeriod = DateUtils.getLastYearPeriod(this.currentPeriod);
    }
}
