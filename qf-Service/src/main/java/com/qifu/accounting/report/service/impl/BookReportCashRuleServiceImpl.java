package com.qifu.accounting.report.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qifu.accounting.common.factory.ReportCashFactory;
import com.qifu.accounting.report.domain.BookReportCashRule;
import com.qifu.accounting.report.domain.vo.ReportCashParams;
import com.qifu.accounting.report.domain.vo.ReportCashVo;
import com.qifu.accounting.report.mapper.BookReportCashRuleMapper;
import com.qifu.accounting.report.service.IBookReportCashRuleService;

/**
 * 现金流量规则Service业务层处理
 *
 *  YM
 * 09-11
 */
@Service
public class BookReportCashRuleServiceImpl extends ServiceImpl<BookReportCashRuleMapper, BookReportCashRule> implements IBookReportCashRuleService {

    @Resource
    private BookReportCashRuleMapper reportCashRuleMapper;

    @Resource
    private ReportCashFactory reportCashFactory;

    /**
     * 利润表-规则查询
     * @param params
     * @return
     */
    public List<BookReportCashRule> getList(ReportCashParams params){
        List<BookReportCashRule> list = reportCashRuleMapper.getList(params.getCashId());
        for (BookReportCashRule rule : list) {
            params.setSubjectCode(rule.getSubjectCode());
            params.setSubjectCode(rule.getSubjectCode());
            BigDecimal yearTotal = reportCashFactory.getYearHappenTotal(params);
            BigDecimal currentPeriodHappen = reportCashFactory.getCurrentPeriodHappen(params);
            BigDecimal previousPeriodBalance = reportCashFactory.getPreviousPeriodBalance(params);
            rule.setYearTotal(yearTotal);
            rule.setCurrentPeriodHappen(currentPeriodHappen);
            rule.setPreviousPeriodBalance(previousPeriodBalance);
        }
        return list;
    }


    public Map balanceSheet(ReportCashParams params){
        BigDecimal yearTotal = reportCashFactory.getYearHappenTotal(params);
        BigDecimal currentPeriodHappen = reportCashFactory.getCurrentPeriodHappen(params);
        BigDecimal previousPeriodBalance = reportCashFactory.getPreviousPeriodBalance(params);
        Map<Object,Object> result = new HashMap<>();
        result.put("yearTotal",yearTotal);
        result.put("currentPeriodHappen",currentPeriodHappen);
        result.put("previousPeriodBalance",previousPeriodBalance);
        return result;
    }

    /**
     * 利润表-规则修改
     * @param reportCashVo
     */
    public void edit(ReportCashVo reportCashVo){
        //删除老数据
        reportCashRuleMapper.deleteCashRule(reportCashVo.getCashId());
        List<BookReportCashRule> list = reportCashVo.getDetails();
        for (BookReportCashRule reportCashRule : list) {
            reportCashRule.setCashId(reportCashVo.getCashId());
            reportCashRule.setCreateTime(new Date());
            reportCashRule.setUpdateTime(new Date());
            reportCashRuleMapper.insert(reportCashRule);
        }
    }
}
