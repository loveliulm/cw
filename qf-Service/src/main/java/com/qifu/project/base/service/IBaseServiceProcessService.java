package com.qifu.project.base.service;

import com.qifu.project.base.domain.BaseServiceProcess;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 服务流程Service接口
 *
 *
 * 06-23
 */
public interface IBaseServiceProcessService extends IService<BaseServiceProcess> {

    List<BaseServiceProcess> selectServiceProcesslist(Long serviceTypeId);
}
