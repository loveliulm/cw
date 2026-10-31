package com.qifu.accounting.carryover.mapper;

import com.qifu.accounting.carryover.domain.BookCarryover;
import com.qifu.accounting.carryover.domain.BookCarryoverRule;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qifu.accounting.carryover.domain.BookCarryoverRuleModel;
import com.qifu.accounting.common.enums.BalanceDirectionEnum;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * 结转规则Mapper接口
 *
 *  YM
 * 07-23
 */
@Repository
public interface BookCarryoverRuleMapper extends BaseMapper<BookCarryoverRule> {

    void insertInitCarryOverRule(@Param("list")List<BookCarryoverRuleModel> list, @Param("carryover")BookCarryover carryover);


    List<BookCarryoverRule> getListByCarryoverId(Long carryoverId);

    BookCarryoverRule getLastCarryoverByAutoBalance(@Param("carryoverId")Long carryoverId);

    void deleteByCarryoverId(Long carryoverId);

    void resetSalesCostPercentage(@Param("carryoverId") Long carryoverId,@Param("percentage") BigDecimal percentage);
}
