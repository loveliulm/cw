package com.qifu.accounting.report.mapper;

import com.qifu.accounting.report.domain.BookReportProfitRuleModel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 利润规则模板Mapper接口
 *
 *  YM
 * 09-11
 */
@Repository
public interface BookReportProfitRuleModelMapper extends BaseMapper<BookReportProfitRuleModel> {

    List<BookReportProfitRuleModel> getListByProfitId(Long profitId);
}
