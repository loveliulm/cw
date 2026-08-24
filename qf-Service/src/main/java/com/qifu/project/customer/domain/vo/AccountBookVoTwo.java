package com.qifu.project.customer.domain.vo;

import lombok.Data;

/**
 * 公司账套使用情况
 */
@Data
public class AccountBookVoTwo {
    /**使用中*/
    private Integer occupied;
    /**停用*/
    private Integer disable;
    /**回收站*/
    private Integer recycled;

    public AccountBookVoTwo(Integer occupied, Integer disable, Integer recycled) {
        this.occupied = occupied;
        this.disable = disable;
        this.recycled = recycled;
    }
}
