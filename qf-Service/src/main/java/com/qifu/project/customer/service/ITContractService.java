package com.qifu.project.customer.service;

import com.qifu.project.customer.domain.TContract;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 【请填写功能名称】Service接口
 *
 *  YM
 * 06-18
 */
public interface ITContractService extends IService<TContract> {

    TContract findFirstByDeptId(Long deptId);

    int deleteContractByIds(long[] ids);

    List<TContract> selectContract(TContract tContract);
}
