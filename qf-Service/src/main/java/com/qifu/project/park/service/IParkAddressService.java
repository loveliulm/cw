package com.qifu.project.park.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qifu.project.customer.domain.TContract;
import com.qifu.project.park.domain.ParkAddress;

import java.util.List;

/**
 * 园区地址Service接口
 */
public interface IParkAddressService extends IService<ParkAddress> {

    List<TContract> getContractList();

    void doParkContract(Long id, Long contractId);

    void deleteParkContract(Long id);

    Long getContractId(Long id);

}
