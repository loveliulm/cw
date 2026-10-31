package com.qifu.accounting.report.domain.vo;

import com.qifu.common.utils.DateUtils;
import lombok.Data;

import java.util.Date;

/**
 *  yangyang
 * @date 2020/9/15 16:56
 */
@Data
public class ReportProfitParams {
    /*利润表id*/
    private Long profitId;

    /*账套id*/
    private Long bookId;

    /*科目编码*/
    private String subjectCode;

    private Date startPeriod;

    private Date endPeriod;

    /*当前账期*/
    private Date currentPeriod;

    /*月报/季报*/
    private String type;

    /*上一年十二月账期*/
    private Date lastYearPeriod;



    public void resetPeriod(Date currentPeriod){
        if("月报".equals(type)){
            this.startPeriod = this.currentPeriod;
            this.endPeriod = this.currentPeriod;
        }
        if("季报".equals(type)){
            int currentYear = DateUtils.getYear(currentPeriod);
            int currentMonth = DateUtils.getMonth(currentPeriod);
            int year = DateUtils.getYear(this.getCurrentPeriod());
            int num = DateUtils.getMonth(this.currentPeriod);
            switch (num){
                case 1:
                    String startDate = year + "-" + 01 + "-" + 01;
                    int month = 3;
                    if(currentYear == year && currentMonth < 3){
                        month = currentMonth;
                    }
                    String endDate = year + "-" + month + "-" + 01;
                    this.startPeriod = DateUtils.dateTime(DateUtils.YYYY_MM_DD,startDate);
                    this.endPeriod = DateUtils.dateTime(DateUtils.YYYY_MM_DD,endDate);
                    break;
                case 2:
                    startDate = year + "-" + 04 + "-" + 01;
                    month = 6;
                    if(currentYear == year && currentMonth < 3){
                        month = currentMonth;
                    }
                    endDate = year + "-" + month + "-" + 01;
                    this.startPeriod = DateUtils.dateTime(DateUtils.YYYY_MM_DD,startDate);
                    this.endPeriod = DateUtils.dateTime(DateUtils.YYYY_MM_DD,endDate);
                    break;
                case 3:
                    startDate = year + "-" + 07 + "-" + 01;
                    month = 9;
                    if(currentYear == year && currentMonth < 3){
                        month = currentMonth;
                    }
                    endDate = year + "-" + month + "-" + 01;
                    this.startPeriod = DateUtils.dateTime(DateUtils.YYYY_MM_DD,startDate);
                    this.endPeriod = DateUtils.dateTime(DateUtils.YYYY_MM_DD,endDate);
                    break;
                case 4:
                    startDate = year + "-" + 10 + "-" + 01;
                    month = 12;
                    if(currentYear == year && currentMonth < 3){
                        month = currentMonth;
                    }
                    endDate = year + "-" + month + "-" + 01;
                    this.startPeriod = DateUtils.dateTime(DateUtils.YYYY_MM_DD,startDate);
                    this.endPeriod = DateUtils.dateTime(DateUtils.YYYY_MM_DD,endDate);
                    break;
            }
        }
        this.lastYearPeriod = DateUtils.getLastYearPeriod(this.currentPeriod);
    }
}
