package com.qifu.accounting.report.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qifu.accounting.report.domain.BookReportCashRuleModel;

/**
 * 现金流量规则模板Mapper接口
 *
 *  YM
 * 09-11
 */
@Repository
public interface BookReportCashRuleModelMapper extends BaseMapper<BookReportCashRuleModel> {

    List<BookReportCashRuleModel> getListByCashId(Long cashId);
}
