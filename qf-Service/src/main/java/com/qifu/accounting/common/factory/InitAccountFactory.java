package com.qifu.accounting.common.factory;

import com.qifu.accounting.carryover.service.IBookCarryoverModelService;
import com.qifu.accounting.fixedAsset.service.IBookFixedAssetCategoryService;
import com.qifu.accounting.report.service.IBookReportAssetsService;
import com.qifu.accounting.report.service.IBookReportCashService;
import com.qifu.accounting.report.service.IBookReportProfitService;
import com.qifu.accounting.sys.domain.BookPeriod;
import com.qifu.accounting.sys.service.IBookConfigService;
import com.qifu.accounting.sys.service.IBookPeriodService;
import com.qifu.accounting.sys.service.IBookSubjectService;
import com.qifu.accounting.voucher.service.IBookVoucherModelService;
import com.qifu.common.utils.StringDateUtil;
import com.qifu.project.customer.domain.TAccountBook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 初始化建账操作
 *  yangyang
 * @date 2020/7/23 10:45
 */
@Component
public class InitAccountFactory {

    @Autowired
    private IBookSubjectService subjectService;

    @Autowired
    private IBookPeriodService periodService;

    @Autowired
    private IBookCarryoverModelService carryoverModelService;

    @Autowired
    private IBookFixedAssetCategoryService fixedAssetCategroyService;

    @Autowired
    private IBookVoucherModelService bookVoucherModelService;

    @Autowired
    private IBookConfigService configService;

    @Autowired
    private IBookReportAssetsService reportAssetsService;

    @Autowired
    private IBookReportProfitService reportProfitService;

    @Autowired
    private IBookReportCashService reportCashService;

    public void initAccountBook(TAccountBook tAccountBook){
        Long bookId = tAccountBook.getBookId();
        Long templateId = tAccountBook.getAccountTemplateId();
        //创建账期
        BookPeriod period = new BookPeriod(bookId,tAccountBook.getCurrentPeriod(),Boolean.TRUE, StringDateUtil.Del_FLAG_0);
        periodService.save(period);

        //初始化科目
        subjectService.insertInitSubject(bookId,templateId);

        //初始化工资模板
        bookVoucherModelService.InitWageVoucherModel(bookId,templateId);

        //初始化固定资产类别
        fixedAssetCategroyService.initInsertFixedAssertCategory(bookId,templateId);

        //初始化配置表
        configService.initInsertConfig(bookId,templateId);

        //初始化结转
        carryoverModelService.insertInitCarryOver(bookId,templateId);

        //初始化资产负债表模板
        reportAssetsService.initInsetReportAssets(bookId,templateId);
        //初始化利润表模板
        reportProfitService.initInsetReportProfit(bookId,templateId);
        //初始化现金流量表模板
        reportCashService.initInsetReportCash(bookId, templateId);
    }
}
