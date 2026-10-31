package com.qifu.accounting.carryover.mapper;

import com.qifu.accounting.carryover.domain.BookCarryoverRuleModel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 结转规则模板Mapper接口
 *
 *  YM
 * 07-23
 */
@Repository
public interface BookCarryoverRuleModelMapper extends BaseMapper<BookCarryoverRuleModel> {

    List<BookCarryoverRuleModel> selectListByModelId(Long carryoverModelId);
}
