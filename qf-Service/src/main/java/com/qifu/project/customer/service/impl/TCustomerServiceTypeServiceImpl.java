package com.qifu.project.customer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qifu.project.customer.domain.TCustomerServiceType;
import com.qifu.project.customer.mapper.TCustomerServiceTypeMapper;
import com.qifu.project.customer.service.ITCustomerServiceTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *  yangyang
 * @date 2020/6/18 11:17
 */
@Service
public class TCustomerServiceTypeServiceImpl extends ServiceImpl<TCustomerServiceTypeMapper, TCustomerServiceType> implements ITCustomerServiceTypeService {
    @Autowired
    private TCustomerServiceTypeMapper serviceTypeMapper;

    public int removeByCustomerId(Long customerId){
        int i = serviceTypeMapper.removeByCustomerId(customerId);
        return i;
    }
}
