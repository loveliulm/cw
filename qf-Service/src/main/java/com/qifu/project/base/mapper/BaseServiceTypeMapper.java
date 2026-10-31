package com.qifu.project.base.mapper;

import com.qifu.project.base.domain.BaseServiceType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qifu.project.base.domain.vo.BaseServiceTypeVo;
import com.qifu.project.base.domain.vo.ServiceClassVo;
import com.qifu.project.base.domain.vo.ServiceTypeVo;

import java.util.List;

/**
 * Mapper接口
 *
 *
 * 06-17
 */
public interface BaseServiceTypeMapper extends BaseMapper<BaseServiceType> {

    List<BaseServiceTypeVo> selectServiceTypelist();


    List<ServiceClassVo>  serviceClassList();

    List<ServiceTypeVo>  selectServiceTypelistName(Long serviceClassId);

    Long getServiceClassId(Long serviceTypeId);
}
