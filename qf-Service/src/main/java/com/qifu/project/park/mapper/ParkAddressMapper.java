package com.qifu.project.park.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qifu.project.park.domain.ParkAddress;
import com.qifu.project.park.domain.vo.ParkAddressVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 园区地址Mapper接口
 */
public interface ParkAddressMapper extends BaseMapper<ParkAddress> {

    List<Long> selectParkAddressContract();

    void doParkContract(@Param("id") Long id, @Param("contractId") Long contractId);

    void deleteParkContract(Long id);

    Long getContractId(Long id);

}
