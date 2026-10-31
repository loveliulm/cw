package com.qifu.accounting.report.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qifu.accounting.report.domain.BookReportCashRule;
import com.qifu.accounting.report.domain.BookReportCashRuleModel;

/**
 * 现金流量规则Mapper接口
 *
 *  YM
 * 09-11
 */
@Repository
public interface BookReportCashRuleMapper extends BaseMapper<BookReportCashRule> {
    void initInsetReportCashRule(@Param("CashRuleModels") List<BookReportCashRuleModel> CashRuleModels,@Param("cashId") Long cashId);

    List<BookReportCashRule> getList(Long cashId);

    void deleteCashRule(Long cashId);
}
