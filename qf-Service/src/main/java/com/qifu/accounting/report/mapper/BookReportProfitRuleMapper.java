package com.qifu.accounting.report.mapper;

import com.qifu.accounting.report.domain.BookReportProfitRule;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qifu.accounting.report.domain.BookReportProfitRuleModel;
import com.qifu.accounting.report.domain.vo.ReportProfitVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 利润规则Mapper接口
 *
 *  YM
 * 09-11
 */
@Repository
public interface BookReportProfitRuleMapper extends BaseMapper<BookReportProfitRule> {
    void initInsetReportProfitRule(@Param("profitRuleModels") List<BookReportProfitRuleModel> profitRuleModels,@Param("profitId") Long profitId);

    List<BookReportProfitRule> getList(Long profitId);

    void deleteProfitRule(Long profitId);
}
