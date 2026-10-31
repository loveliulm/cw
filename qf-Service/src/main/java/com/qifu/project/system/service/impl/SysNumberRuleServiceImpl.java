package com.qifu.project.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qifu.project.system.mapper.SysNumberRuleMapper;
import com.qifu.project.system.domain.SysNumberRule;
import com.qifu.project.system.service.ISysNumberRuleService;

import java.util.List;

/**
 * 编号规则Service业务层处理
 *
 *
 * 06-18
 */
@Service
public class SysNumberRuleServiceImpl extends ServiceImpl<SysNumberRuleMapper, SysNumberRule> implements ISysNumberRuleService {

    @Autowired
    private SysNumberRuleMapper sysNumberRuleMapper;

    @Override
    public SysNumberRule findByRuleType(Integer type) {
        QueryWrapper<SysNumberRule> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SysNumberRule::getRuleType, type);
        SysNumberRule sysNumberRule = sysNumberRuleMapper.selectOne(queryWrapper);
        return sysNumberRule;
    }
}
