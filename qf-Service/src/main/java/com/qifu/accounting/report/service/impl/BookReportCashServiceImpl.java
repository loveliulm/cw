package com.qifu.accounting.report.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qifu.accounting.common.factory.ReportCashFactory;
import com.qifu.accounting.report.domain.BookReportCash;
import com.qifu.accounting.report.domain.BookReportCashModel;
import com.qifu.accounting.report.domain.BookReportCashRuleModel;
import com.qifu.accounting.report.domain.vo.ReportCashParams;
import com.qifu.accounting.report.mapper.BookReportCashMapper;
import com.qifu.accounting.report.mapper.BookReportCashModelMapper;
import com.qifu.accounting.report.mapper.BookReportCashRuleMapper;
import com.qifu.accounting.report.mapper.BookReportCashRuleModelMapper;
import com.qifu.accounting.report.service.IBookReportCashService;

/**
 * 现金流量Service业务层处理
 *
 *  YM
 * 09-11
 */
@Service
public class BookReportCashServiceImpl extends ServiceImpl<BookReportCashMapper, BookReportCash> implements IBookReportCashService {

    @Resource
    private BookReportCashMapper reportCashMapper;

    @Resource
    private BookReportCashRuleMapper reportCashRuleMapper;

    @Resource
    private BookReportCashModelMapper reportCashModelMapper;

    @Resource
    private BookReportCashRuleModelMapper reportCashRuleModelMapper;

    @Resource
    private ReportCashFactory reportCashFactory;


    /**
     * 利润表-初始化
     * @param bookId
     * @param templateId
     */
    public void initInsetReportCash(Long bookId, Long templateId){
        List<BookReportCashModel> list = reportCashModelMapper.getListByTemplate(templateId);
        for (BookReportCashModel bookReportCashModel : list) {
            BookReportCash reportCash = new BookReportCash();
            reportCash.setBookId(bookId)
                    .setCashName(bookReportCashModel.getCashName())
                    .setCashTitle(bookReportCashModel.getCashTitle())
                    .setIsRule(bookReportCashModel.getIsRule())
                    .setLevel(bookReportCashModel.getLevel())
                    .setRowNum(bookReportCashModel.getRowNum())
                    .setCreateTime(new Date())
                    .setUpdateTime(new Date());
            reportCashMapper.insert(reportCash);
            List<BookReportCashRuleModel> CashRuleModels = reportCashRuleModelMapper.getListByCashId(bookReportCashModel.getId());
            if(!CashRuleModels.isEmpty()){
                reportCashRuleMapper.initInsetReportCashRule(CashRuleModels,reportCash.getId());
            }
        }
    }

    public List<BookReportCash> getList(ReportCashParams params){
        List<BookReportCash> list = reportCashMapper.getList(params);
        reportCashFactory.resetList(params,list);
        return list;
    }

}
