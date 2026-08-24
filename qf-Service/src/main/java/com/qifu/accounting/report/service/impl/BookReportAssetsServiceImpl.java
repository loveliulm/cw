package com.qifu.accounting.report.service.impl;

import com.qifu.accounting.common.factory.ReportAssetsFactory;
import com.qifu.accounting.report.domain.vo.ReportAssetsParams;
import com.qifu.accounting.report.mapper.BookReportAssetsRuleMapper;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qifu.accounting.report.mapper.BookReportAssetsMapper;
import com.qifu.accounting.report.domain.BookReportAssets;
import com.qifu.accounting.report.service.IBookReportAssetsService;

import javax.annotation.Resource;
import java.util.List;

/**
 * 资产负债模板Service业务层处理
 *
 *  YM
 * 09-03
 */
@Service
public class BookReportAssetsServiceImpl extends ServiceImpl<BookReportAssetsMapper, BookReportAssets> implements IBookReportAssetsService {

    @Resource
    private BookReportAssetsMapper reportAssetsMapper;

    @Resource
    private BookReportAssetsRuleMapper reportAssetsRuleMapper;

    @Resource
    private ReportAssetsFactory reportAssetsFactory;


    /**
     * 初始化资产负债表
     * @param bookId
     * @param templateId
     */
    public void initInsetReportAssets(Long bookId, Long templateId){
        //模板初始化
        reportAssetsMapper.initInsetReportAssets(bookId,templateId);
        //模板规程初始化
        reportAssetsRuleMapper.initInsetReportAssetsRule(bookId,templateId);
    }

    /**
     * 资产负债表-查询列表
     * @param params
     * @return
     */
    public List<BookReportAssets> getList(ReportAssetsParams params){
        List<BookReportAssets> list = reportAssetsMapper.getList(params);
        reportAssetsFactory.resetReportAssetsList(params,list);
        return list;
    }
}
