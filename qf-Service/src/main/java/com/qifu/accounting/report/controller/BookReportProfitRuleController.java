package com.qifu.accounting.report.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import java.util.List;
import java.util.Arrays;
import java.util.Map;

import com.qifu.accounting.report.domain.vo.ReportProfitParams;
import com.qifu.accounting.report.domain.vo.ReportProfitVo;
import com.qifu.accounting.sys.service.IBookPeriodService;
import com.qifu.common.utils.StringUtils;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.qifu.framework.aspectj.lang.annotation.Log;
import com.qifu.framework.aspectj.lang.enums.BusinessType;
import com.qifu.accounting.report.domain.BookReportProfitRule;
import com.qifu.accounting.report.service.IBookReportProfitRuleService;
import com.qifu.framework.web.controller.BaseController;
import com.qifu.framework.web.domain.AjaxResult;
import com.qifu.common.utils.poi.ExcelUtil;
import com.qifu.framework.web.page.TableDataInfo;

/**
 * 利润规则Controller
 *
 *  YM
 * 09-11
 */
@AllArgsConstructor
@RestController
@RequestMapping("/accounting/profitRule" )
public class BookReportProfitRuleController extends BaseController {

    private final IBookReportProfitRuleService iBookReportProfitRuleService;

    private final IBookPeriodService bookPeriodService;

    /**
     * 查询利润规则列表
     */
    @GetMapping("/list")
    public TableDataInfo list(ReportProfitParams params)
    {

        params.resetPeriod(bookPeriodService.yearPeriod(params.getBookId()));
        List<BookReportProfitRule> list = iBookReportProfitRuleService.getList(params);
        return getDataTable(list);
    }

    /**
     * 获取规则金额
     * @param params
     * @return
     */
    @GetMapping("/balanceSheet")
    public AjaxResult balanceSheet(ReportProfitParams params){
        Map result = iBookReportProfitRuleService.balanceSheet(params);
        return AjaxResult.success(result);
    }

    /**
     * 修改利润规则
     */
    @Log(title = "利润规则" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ReportProfitVo reportProfitVo) {
        iBookReportProfitRuleService.edit(reportProfitVo);
        return AjaxResult.success();
    }
}
