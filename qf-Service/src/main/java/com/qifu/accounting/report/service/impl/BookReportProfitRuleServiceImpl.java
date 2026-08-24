package com.qifu.accounting.report.service.impl;

import com.qifu.accounting.common.factory.ReportAssetsFactory;
import com.qifu.accounting.common.factory.ReportProfitFactory;
import com.qifu.accounting.report.domain.BookReportAssetsRule;
import com.qifu.accounting.report.domain.vo.ReportAssetsParams;
import com.qifu.accounting.report.domain.vo.ReportProfitParams;
import com.qifu.accounting.report.domain.vo.ReportProfitVo;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qifu.accounting.report.mapper.BookReportProfitRuleMapper;
import com.qifu.accounting.report.domain.BookReportProfitRule;
import com.qifu.accounting.report.service.IBookReportProfitRuleService;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 利润规则Service业务层处理
 *
 *  YM
 * 09-11
 */
@Service
public class BookReportProfitRuleServiceImpl extends ServiceImpl<BookReportProfitRuleMapper, BookReportProfitRule> implements IBookReportProfitRuleService {

    @Resource
    private BookReportProfitRuleMapper reportProfitRuleMapper;

    @Resource
    private ReportProfitFactory reportProfitFactory;

    /**
     * 利润表-规则查询
     * @param params
     * @return
     */
    public List<BookReportProfitRule> getList(ReportProfitParams params){
        List<BookReportProfitRule> list = reportProfitRuleMapper.getList(params.getProfitId());
        for (BookReportProfitRule rule : list) {
            params.setSubjectCode(rule.getSubjectCode());
            params.setSubjectCode(rule.getSubjectCode());
            BigDecimal yearTotal = reportProfitFactory.getYearHappenTotal(params);
            BigDecimal currentPeriodHappen = reportProfitFactory.getCurrentPeriodHappen(params);
            BigDecimal previousPeriodBalance = reportProfitFactory.getPreviousPeriodBalance(params);
            rule.setYearTotal(yearTotal);
            rule.setCurrentPeriodHappen(currentPeriodHappen);
            rule.setPreviousPeriodBalance(previousPeriodBalance);
        }
        return list;
    }


    public Map balanceSheet(ReportProfitParams params){
        BigDecimal yearTotal = reportProfitFactory.getYearHappenTotal(params);
        BigDecimal currentPeriodHappen = reportProfitFactory.getCurrentPeriodHappen(params);
        BigDecimal previousPeriodBalance = reportProfitFactory.getPreviousPeriodBalance(params);
        Map<Object,Object> result = new HashMap<>();
        result.put("yearTotal",yearTotal);
        result.put("currentPeriodHappen",currentPeriodHappen);
        result.put("previousPeriodBalance",previousPeriodBalance);
        return result;
    }

    /**
     * 利润表-规则修改
     * @param reportProfitVo
     */
    public void edit(ReportProfitVo reportProfitVo){
        //删除老数据
        reportProfitRuleMapper.deleteProfitRule(reportProfitVo.getProfitId());
        List<BookReportProfitRule> list = reportProfitVo.getDetails();
        for (BookReportProfitRule reportProfitRule : list) {
            reportProfitRule.setProfitId(reportProfitVo.getProfitId());
            reportProfitRule.setCreateTime(new Date());
            reportProfitRule.setUpdateTime(new Date());
            reportProfitRuleMapper.insert(reportProfitRule);
        }
    }
}
