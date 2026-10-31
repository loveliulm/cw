package com.qifu.project.base.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qifu.project.base.mapper.BaseServiceProcessMapper;
import com.qifu.project.base.domain.BaseServiceProcess;
import com.qifu.project.base.service.IBaseServiceProcessService;

import java.util.List;

/**
 * 服务流程Service业务层处理
 *
 *
 * 06-23
 */
@Service
public class BaseServiceProcessServiceImpl extends ServiceImpl<BaseServiceProcessMapper, BaseServiceProcess> implements IBaseServiceProcessService {
    @Autowired
    private BaseServiceProcessMapper processMapper;

    public List<BaseServiceProcess> selectServiceProcesslist(Long serviceItemId){
        List<BaseServiceProcess> list = processMapper.selectServiceProcesslist(serviceItemId);
        return list;
    }
}
