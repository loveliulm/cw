package com.qifu.project.customer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qifu.project.customer.domain.TCustomerServiceType;

/**
 *  yangyang
 * @date 2020/6/18 11:16
 */
public interface ITCustomerServiceTypeService extends IService<TCustomerServiceType> {
    int removeByCustomerId(Long customerId);
}
