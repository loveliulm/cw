package com.qifu.project.base.mapper;

import com.qifu.project.base.domain.BaseServiceItem;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qifu.project.base.domain.vo.ServiceItemVo;

import java.util.List;

/**
 * 服务项目Mapper接口
 *
 *
 * 06-19
 */
public interface BaseServiceItemMapper extends BaseMapper<BaseServiceItem> {

    List<ServiceItemVo> serviceTypeItemList(Long serviceTypeId);

}
