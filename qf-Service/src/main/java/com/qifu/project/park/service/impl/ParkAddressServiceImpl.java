package com.qifu.project.park.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qifu.common.utils.SecurityUtils;
import com.qifu.project.customer.domain.TContract;
import com.qifu.project.customer.mapper.TContractMapper;
import com.qifu.project.park.domain.ParkAddress;
import com.qifu.project.park.mapper.ParkAddressMapper;
import com.qifu.project.park.service.IParkAddressService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 园区地址Service业务层处理
 */
@Service
public class ParkAddressServiceImpl extends ServiceImpl<ParkAddressMapper, ParkAddress> implements IParkAddressService {

    @Resource
    private ParkAddressMapper parkAddressMapper;

    @Resource
    private TContractMapper contractMapper;

    @Override
    public List<TContract> getContractList() {
        TContract contract = new TContract();
        contract.setServiceTypeId(1L);
        contract.setDeptId(SecurityUtils.companyId());
        // 查询账号全部的合同
        List<TContract> list = contractMapper.selectContract(contract);

        // 查询已经绑定的合同
        List<Long> contractIds = parkAddressMapper.selectParkAddressContract();

        if (contractIds != null && contractIds.size() > 0) {
            if (list != null && list.size() > 0) {
                list = list.stream().filter(info -> !contractIds.contains(info.getContractId())).collect(Collectors.toList());
            }
        }

        return list;
    }

    @Override
    public void doParkContract(Long id, Long contractId) {
        parkAddressMapper.doParkContract(id, contractId);
    }

    @Override
    public void deleteParkContract(Long id) {
        parkAddressMapper.deleteParkContract(id);
    }

    @Override
    public Long getContractId(Long id) {
        return parkAddressMapper.getContractId(id);
    }

}
