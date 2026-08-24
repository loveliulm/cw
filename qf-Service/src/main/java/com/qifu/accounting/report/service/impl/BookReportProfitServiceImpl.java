package com.qifu.accounting.report.service.impl;

import com.qifu.accounting.common.factory.ReportProfitFactory;
import com.qifu.accounting.report.domain.*;
import com.qifu.accounting.report.domain.vo.ReportAssetsParams;
import com.qifu.accounting.report.domain.vo.ReportProfitParams;
import com.qifu.accounting.report.mapper.BookReportProfitModelMapper;
import com.qifu.accounting.report.mapper.BookReportProfitRuleMapper;
import com.qifu.accounting.report.mapper.BookReportProfitRuleModelMapper;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qifu.accounting.report.mapper.BookReportProfitMapper;
import com.qifu.accounting.report.service.IBookReportProfitService;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 利润Service业务层处理
 *
 *  YM
 * 09-11
 */
@Service
public class BookReportProfitServiceImpl extends ServiceImpl<BookReportProfitMapper, BookReportProfit> implements IBookReportProfitService {

    @Resource
    private BookReportProfitMapper reportProfitMapper;

    @Resource
    private BookReportProfitRuleMapper reportProfitRuleMapper;

    @Resource
    private BookReportProfitModelMapper reportProfitModelMapper;

    @Resource
    private BookReportProfitRuleModelMapper reportProfitRuleModelMapper;

    @Resource
    private ReportProfitFactory reportProfitFactory;


    /**
     * 利润表-初始化
     * @param bookId
     * @param templateId
     */
    public void initInsetReportProfit(Long bookId, Long templateId){
        List<BookReportProfitModel> list = reportProfitModelMapper.getListByTemplate(templateId);
        for (BookReportProfitModel bookReportProfitModel : list) {
            BookReportProfit reportProfit = new BookReportProfit();
            reportProfit.setBookId(bookId)
                    .setProfitName(bookReportProfitModel.getProfitName())
                    .setProfitTitle(bookReportProfitModel.getProfitTitle())
                    .setIsRule(bookReportProfitModel.getIsRule())
                    .setLevel(bookReportProfitModel.getLevel())
                    .setRowNum(bookReportProfitModel.getRowNum())
                    .setCreateTime(new Date())
                    .setUpdateTime(new Date());
            reportProfitMapper.insert(reportProfit);
            List<BookReportProfitRuleModel> profitRuleModels = reportProfitRuleModelMapper.getListByProfitId(bookReportProfitModel.getId());
            if(!profitRuleModels.isEmpty()){
                reportProfitRuleMapper.initInsetReportProfitRule(profitRuleModels,reportProfit.getId());
            }
        }
    }

    public List<BookReportProfit> getList(ReportProfitParams params){
        List<BookReportProfit> list = reportProfitMapper.getList(params);
        reportProfitFactory.resetList(params,list);
        return list;
    }
}
