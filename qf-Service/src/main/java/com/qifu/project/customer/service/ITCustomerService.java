package com.qifu.project.customer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qifu.project.customer.domain.TCustomer;
import com.qifu.project.customer.domain.vo.ReportData;
import com.qifu.project.customer.domain.vo.TCustomerVo;
import com.qifu.project.system.domain.SysUser;

import java.util.List;

/**
 * 客户Service接口
 *
 *
 * 06-15
 */
public interface ITCustomerService extends IService<TCustomer> {
    List<TCustomerVo> customerList(TCustomer customer);

    TCustomerVo customerById(long id);

    int deleteCustomerByIds(Long[] customerIds);

    List<SysUser> selectUserList(String roleKey, Long deptId);

    int createAccount(int customerId);

    TCustomer findFirstByDeptId(Long deptId);

    void add(TCustomerVo tCustomer);

    void edit(TCustomerVo tCustomer);

    void dispatch(TCustomer tCustomer);

    /**
     * 首页报表统计
     * @param customer
     * @return
     */
    ReportData selectByAccount(TCustomer customer);

    public String importCustomer(List<TCustomer> customerList, Boolean isUpdateSupport, String operName);

    /**
     * 校验编码是否唯一
     */
    String checkCodeUnique(String code);

    /**
     * 效验公司的客户名称是否唯一
     * @param tCustomer
     * @return
     */
    String checkNameUnique(TCustomer tCustomer);
}
