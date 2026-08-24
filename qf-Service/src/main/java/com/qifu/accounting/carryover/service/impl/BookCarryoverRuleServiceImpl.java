package com.qifu.accounting.carryover.service.impl;

import com.qifu.accounting.carryover.domain.BookCarryoverRuleModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qifu.accounting.carryover.mapper.BookCarryoverRuleMapper;
import com.qifu.accounting.carryover.domain.BookCarryoverRule;
import com.qifu.accounting.carryover.service.IBookCarryoverRuleService;

import java.util.List;

/**
 * 结转规则Service业务层处理
 *
 *  YM
 * 07-23
 */
@Service
public class BookCarryoverRuleServiceImpl extends ServiceImpl<BookCarryoverRuleMapper, BookCarryoverRule> implements IBookCarryoverRuleService {

    @Autowired
    private BookCarryoverRuleMapper carryoverRuleMapper;

    public List<BookCarryoverRule> getList(BookCarryoverRule bookCarryoverRule){
        return carryoverRuleMapper.getListByCarryoverId(bookCarryoverRule.getCarryoverId());
    }
}
