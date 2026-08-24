package com.qifu.project.commerce.mapper;

import com.qifu.project.commerce.domain.CommerceServiceStep;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qifu.project.commerce.domain.vo.CommerceServiceStepVo;

import java.util.List;

/**
 * 工商服务步骤Mapper接口
 *
 *  YM
 * 08-14
 */
public interface CommerceServiceStepMapper extends BaseMapper<CommerceServiceStep> {

    /**
     * 获取列表
     * @param commerceServiceStep
     * @return
     */
    List<CommerceServiceStepVo> getList(CommerceServiceStep commerceServiceStep);
}
