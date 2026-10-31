package com.qifu.project.commerce.domain.vo;

import lombok.Data;

/**
 * 工商服务统计数据
 */
@Data
public class StatisticalData {

    /**总数*/
    private int amount;
    /**未完成*/
    private int unfinished;
    /**收款未结*/
    private int uncleared;
}
