package com.qifu.accounting.carryover.service;

import com.qifu.accounting.carryover.domain.BookCarryoverRule;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 结转规则Service接口
 *
 *  YM
 * 07-23
 */
public interface IBookCarryoverRuleService extends IService<BookCarryoverRule> {

    List<BookCarryoverRule> getList(BookCarryoverRule bookCarryoverRule);
}
