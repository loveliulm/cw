package com.qifu.project.base.service;

import com.qifu.project.base.domain.BaseServiceType;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qifu.project.base.domain.vo.BaseServiceTypeVo;
import com.qifu.project.base.domain.vo.ServiceClassVo;
import com.qifu.project.base.domain.vo.ServiceTypeVo;

import java.util.List;

/**
 * Service接口
 *
 *
 * 06-17
 */
public interface IBaseServiceTypeService extends IService<BaseServiceType> {

    List<BaseServiceTypeVo> selectServiceTypelist();

    List<ServiceClassVo> selectServiceTypelistName();

    Long getServiceClassId(Long serviceTypeId);
}
