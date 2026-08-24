package com.qifu.accounting.report.service.impl;

import com.qifu.accounting.common.factory.ReportAssetsFactory;
import com.qifu.accounting.report.domain.vo.ReportAssetVo;
import com.qifu.accounting.report.domain.vo.ReportAssetsParams;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qifu.accounting.report.mapper.BookReportAssetsRuleMapper;
import com.qifu.accounting.report.domain.BookReportAssetsRule;
import com.qifu.accounting.report.service.IBookReportAssetsRuleService;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 资产负债规则Service业务层处理
 *
 *  YM
 * 09-03
 */
@Service
public class BookReportAssetsRuleServiceImpl extends ServiceImpl<BookReportAssetsRuleMapper, BookReportAssetsRule> implements IBookReportAssetsRuleService {

    @Resource
    private BookReportAssetsRuleMapper reportAssetsRuleMapper;

    @Resource
    private ReportAssetsFactory reportAssetsFactory;


    /**
     * 资产负债表-规则查询
     * @param params
     * @return
     */
    public List<BookReportAssetsRule> getList(ReportAssetsParams params){
        List<BookReportAssetsRule> list = reportAssetsRuleMapper.getList(params);
        for (BookReportAssetsRule bookReportAssetsRule : list) {
            //科目期初余额
            params.setSubjectCode(bookReportAssetsRule.getSubjectCode());
            BigDecimal endBalance = reportAssetsFactory.endBalance(params,bookReportAssetsRule.getDataRule());
            BigDecimal yearInitBalance = reportAssetsFactory.yearInitBalance(params,bookReportAssetsRule.getDataRule());
            bookReportAssetsRule.setEndBalance(endBalance);
            bookReportAssetsRule.setYearInitBalance(yearInitBalance);
        }
        return list;
    }

    public Map balanceSheet(BookReportAssetsRule rule,Date currentPeriod){
        ReportAssetsParams params = new ReportAssetsParams();
        params.setSubjectCode(rule.getSubjectCode());
        params.setBookId(rule.getBookId());
        params.setCurrentPeriod(currentPeriod);
        BigDecimal endBalance = reportAssetsFactory.endBalance(params,rule.getDataRule());
        BigDecimal yearInitBalance = reportAssetsFactory.yearInitBalance(params,rule.getDataRule());
        Map<Object,Object> result = new HashMap<>();
        result.put("endBalance",endBalance);
        result.put("yearInitBalance",yearInitBalance);
        return result;
    }

    /**
     * 资产负债表-规则修改
     * @param reportAssetVo
     */
    public void edit(ReportAssetVo reportAssetVo){
        //先删除老的数据
        reportAssetsRuleMapper.updateByRowNum(reportAssetVo);
        //新增新的数据
        List<BookReportAssetsRule> list = reportAssetVo.getDetails();
        for (BookReportAssetsRule assetsRule : list) {
            assetsRule.setRowNum(reportAssetVo.getRowNum());
            assetsRule.setBookId(reportAssetVo.getBookId());
            assetsRule.setIsClassify(reportAssetVo.getIsClassify());
            assetsRule.setCreateTime(new Date());
            assetsRule.setUpdateTime(new Date());
            reportAssetsRuleMapper.insert(assetsRule);
        }

    }
}
