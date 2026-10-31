package com.qifu.project.customer.mapper;

import com.qifu.project.customer.domain.TCustomerServiceType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 【请填写功能名称】Mapper接口
 *
 *  YM
 * 06-18
 */
@Repository
public interface TCustomerServiceTypeMapper extends BaseMapper<TCustomerServiceType> {
    TCustomerServiceType[] getListByCustomerId(@Param("customerId") Long customerId);

    int removeByCustomerId(Long customerId);
}
