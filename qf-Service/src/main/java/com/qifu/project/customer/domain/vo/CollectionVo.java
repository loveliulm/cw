package com.qifu.project.customer.domain.vo;

import com.qifu.framework.aspectj.lang.annotation.Excel;
import com.qifu.project.common.enums.CollectionStatusEnum;
import com.qifu.project.customer.domain.TCollection;
import lombok.Data;

/**
 *  yangyang
 * @date 2020/6/28 15:49
 */
@Data
public class CollectionVo extends TCollection {


    /*记账会计*/
    private String bookkeepingAccounting;

    /*付款方式:一次性付款，分期付款 */
    private String payType;

    /*1个月内属于即将到期 '已到期','即将到期','未到期'*/
    private CollectionStatusEnum status;

    /*年*/
    private Integer year;

    /*月*/
    private Integer month;
}
