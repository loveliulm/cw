package com.qifu.project.commerce.service;

import com.qifu.framework.web.domain.AjaxResult;
import com.qifu.project.commerce.domain.CommerceServiceStep;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qifu.project.commerce.domain.vo.CommerceServiceStepVo;

import java.util.List;

/**
 * 工商服务步骤Service接口
 *
 *  YM
 * 08-14
 */
public interface ICommerceServiceStepService extends IService<CommerceServiceStep> {

    /**
     * 开始服务
     * @param commerceServiceId 工商服务id
     */
    AjaxResult add(Long commerceServiceId);

    /**
     * 获取列表
     * @param commerceServiceStep
     * @return
     */
    List<CommerceServiceStepVo> getList(CommerceServiceStep commerceServiceStep);
}
