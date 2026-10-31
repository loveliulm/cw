package com.qifu.project.customer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qifu.project.customer.domain.TCustomer;
import com.qifu.project.customer.domain.vo.ReportData;
import com.qifu.project.customer.domain.vo.TCustomerVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 客户Mapper接口
 *
 *
 * 06-15
 */
@Repository
public interface TCustomerMapper extends BaseMapper<TCustomer> {

    List<TCustomerVo> customerList(TCustomer customer);

    int deleteCustomerByIds(Long[] customerIds);

    TCustomerVo customerById(@Param("id") long id);

    /**
     * 首页报表统计
     * @param customer
     * @return
     */
    ReportData selectByAccount(TCustomer customer);

    /**
     * 根据客户姓名查找
     * @param customerName
     * @return
     */
    TCustomer selectByName(String customerName);
}
