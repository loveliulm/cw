package com.qifu.project.base.service;

import com.qifu.project.base.domain.BaseServiceItem;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qifu.project.base.domain.vo.ServiceItemVo;

import java.util.List;

/**
 * 服务项目Service接口
 *
 *
 * 06-19
 */
public interface IBaseServiceItemService extends IService<BaseServiceItem> {
    List<ServiceItemVo> serviceTypeItemList(Long serviceTypeId);

}
