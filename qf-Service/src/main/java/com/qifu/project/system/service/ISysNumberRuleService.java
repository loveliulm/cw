package com.qifu.project.system.service;

import com.qifu.project.system.domain.SysNumberRule;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 编号规则Service接口
 *
 *
 * 06-18
 */
public interface ISysNumberRuleService extends IService<SysNumberRule> {

    SysNumberRule findByRuleType (Integer type);

}
