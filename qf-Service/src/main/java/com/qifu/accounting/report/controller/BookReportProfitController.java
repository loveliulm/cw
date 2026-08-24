package com.qifu.accounting.report.controller;

import java.util.Date;
import java.util.List;

import com.qifu.accounting.report.domain.vo.ReportProfitParams;
import com.qifu.accounting.sys.service.IBookLoginService;
import com.qifu.accounting.sys.service.IBookPeriodService;
import com.qifu.common.utils.DateUtils;
import com.qifu.common.utils.poi.ExcelUtilReport;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.qifu.framework.aspectj.lang.annotation.Log;
import com.qifu.framework.aspectj.lang.enums.BusinessType;
import com.qifu.accounting.report.domain.BookReportProfit;
import com.qifu.accounting.report.service.IBookReportProfitService;
import com.qifu.framework.web.controller.BaseController;
import com.qifu.framework.web.domain.AjaxResult;
import com.qifu.framework.web.page.TableDataInfo;

/**
 * 利润Controller
 *
 *  YM
 * 09-11
 */
@AllArgsConstructor
@RestController
@RequestMapping("/accounting/profit" )
public class BookReportProfitController extends BaseController {

    private final IBookReportProfitService iBookReportProfitService;

    private final IBookPeriodService bookPeriodService;

    private final IBookLoginService bookLoginService;

    /**
     * 查询利润列表
     */
    @GetMapping("/list")
    public TableDataInfo list(ReportProfitParams params)
    {
        startPage();
        params.resetPeriod(bookPeriodService.yearPeriod(params.getBookId()));
        List<BookReportProfit> list = iBookReportProfitService.getList(params);
        return getDataTable(list);
    }

    /**
     * 导出利润列表
     */
    @Log(title = "利润" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(ReportProfitParams params) {
        params.resetPeriod(bookPeriodService.yearPeriod(params.getBookId()));
        List<BookReportProfit> list = iBookReportProfitService.getList(params);
        String bookName = bookLoginService.getInfo(params.getBookId()).getCustomerName();
        Date yearPeriod = params.getCurrentPeriod();
        String period = "";
        if("月报".equals(params.getType())){
            period = DateUtils.getYear(yearPeriod) + "年" + DateUtils.getMonth(yearPeriod) + "期";
        }if("季报".equals(params.getType())){
            period = DateUtils.getYear(yearPeriod) + "年" + DateUtils.getMonth(yearPeriod) + "季度";
        }

        String unit = "单位：元";
        Object [] title2 = {bookName,period,unit};
        ExcelUtilReport<BookReportProfit> util = new ExcelUtilReport<BookReportProfit>(BookReportProfit. class);
        return util.exportExcel(list, "利润表" ,title2);
    }

    //初始化
//    @PostMapping("/init" )
//    public void initInsetReportAssets(Long bookId, Long templateId){
//        iBookReportProfitService.initInsetReportProfit(bookId, templateId);
//    }
}
