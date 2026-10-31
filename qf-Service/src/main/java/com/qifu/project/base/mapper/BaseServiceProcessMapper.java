package com.qifu.project.base.mapper;

import com.qifu.project.base.domain.BaseServiceProcess;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 服务流程Mapper接口
 *
 *
 * 06-23
 */
@Repository
public interface BaseServiceProcessMapper extends BaseMapper<BaseServiceProcess> {

    List<BaseServiceProcess> selectServiceProcesslist(Long serviceItemId);
}
