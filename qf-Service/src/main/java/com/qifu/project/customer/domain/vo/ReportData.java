package com.qifu.project.customer.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 报表统计数据
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportData {
    /**1月*/
    private Integer january;
    /**2月*/
    private Integer february;
    /**3月*/
    private Integer march;
    /**4月*/
    private Integer april;
    /**5月*/
    private Integer may;
    /**6月*/
    private Integer june;
    /**7月*/
    private Integer july;
    /**8月*/
    private Integer august;
    /**9月*/
    private Integer september;
    /**10月*/
    private Integer october;
    /**11月*/
    private Integer november;
    /**12月*/
    private Integer december;

    /**
     * 获取数组
     * @return
     */
    public Integer[] getArray(){
        Integer[] array = new Integer[12];
        array[0] = this.january;
        array[1] = this.february;
        array[2] = this.march;
        array[3] = this.april;
        array[4] = this.may;
        array[5] = this.june;
        array[6] = this.july;
        array[7] = this.august;
        array[8] = this.september;
        array[9] = this.october;
        array[10] = this.november;
        array[11] = this.december;
        return array;
    }
}
