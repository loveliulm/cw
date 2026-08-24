package com.qifu.project.customer.mapper;

import com.qifu.project.customer.domain.TContract;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 【请填写功能名称】Mapper接口
 *
 *  YM
 * 06-18
 */
@Repository
public interface TContractMapper extends BaseMapper<TContract> {

    int deleteContractByIds(long[] ids);

    List<TContract> selectContract(TContract tContract);

    //定時任務
    int updateStatusByEndDate(TContract tContract);
}
