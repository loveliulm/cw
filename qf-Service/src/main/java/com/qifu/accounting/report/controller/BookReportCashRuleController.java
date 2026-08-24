package com.qifu.accounting.report.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qifu.accounting.report.domain.BookReportCashRule;
import com.qifu.accounting.report.domain.vo.ReportCashParams;
import com.qifu.accounting.report.domain.vo.ReportCashVo;
import com.qifu.accounting.report.service.IBookReportCashRuleService;
import com.qifu.accounting.sys.service.IBookPeriodService;
import com.qifu.framework.aspectj.lang.annotation.Log;
import com.qifu.framework.aspectj.lang.enums.BusinessType;
import com.qifu.framework.web.controller.BaseController;
import com.qifu.framework.web.domain.AjaxResult;
import com.qifu.framework.web.page.TableDataInfo;

import lombok.AllArgsConstructor;

/**
 * 现金流量规则Controller
 *
 *  YM
 * 09-11
 */
@AllArgsConstructor
@RestController
@RequestMapping("/accounting/cashRule" )
public class BookReportCashRuleController extends BaseController {

    private final IBookReportCashRuleService iBookReportCashRuleService;

    private final IBookPeriodService bookPeriodService;

    /**
     * 查询现金流量规则列表
     */
    @GetMapping("/list")
    public TableDataInfo list(ReportCashParams params)
    {

        params.resetPeriod(bookPeriodService.yearPeriod(params.getBookId()));
        List<BookReportCashRule> list = iBookReportCashRuleService.getList(params);
        return getDataTable(list);
    }

    /**
     * 获取现金流量规则金额
     * @param params
     * @return
     */
    @GetMapping("/balanceSheet")
    public AjaxResult balanceSheet(ReportCashParams params){
        Map result = iBookReportCashRuleService.balanceSheet(params);
        return AjaxResult.success(result);
    }

    /**
     * 修改利润规则
     */
    @Log(title = "现金流量规则" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ReportCashVo reportCashVo) {
        iBookReportCashRuleService.edit(reportCashVo);
        return AjaxResult.success();
    }
}
