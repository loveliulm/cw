package com.qifu.project.base.service.impl;

import com.qifu.project.base.domain.vo.ServiceItemVo;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qifu.project.base.mapper.BaseServiceItemMapper;
import com.qifu.project.base.domain.BaseServiceItem;
import com.qifu.project.base.service.IBaseServiceItemService;

import javax.annotation.Resource;
import java.util.List;

/**
 * 服务项目Service业务层处理
 *
 *
 * 06-19
 */
@Service
public class BaseServiceItemServiceImpl extends ServiceImpl<BaseServiceItemMapper, BaseServiceItem> implements IBaseServiceItemService {
    @Resource
    private BaseServiceItemMapper serviceItemMapper;
    public List<ServiceItemVo> serviceTypeItemList(Long serviceTypeId){
        return serviceItemMapper.serviceTypeItemList(serviceTypeId);
    }

}
