package com.qifu.accounting.report.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.qifu.accounting.report.domain.vo.ReportAssetVo;
import com.qifu.accounting.report.domain.vo.ReportAssetsParams;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.qifu.framework.aspectj.lang.annotation.Log;
import com.qifu.framework.aspectj.lang.enums.BusinessType;
import com.qifu.accounting.report.domain.BookReportAssetsRule;
import com.qifu.accounting.report.service.IBookReportAssetsRuleService;
import com.qifu.framework.web.controller.BaseController;
import com.qifu.framework.web.domain.AjaxResult;
import com.qifu.framework.web.page.TableDataInfo;

/**
 * 资产负债规则Controller
 *
 *  YM
 * 09-03
 */
@AllArgsConstructor
@RestController
@RequestMapping("/accounting/assetsReportRule" )
public class BookReportAssetsRuleController extends BaseController {

    private final IBookReportAssetsRuleService iBookReportAssetsRuleService;


    /**
     * 查询资产负债规则列表
     */

    @GetMapping("/list")
    public TableDataInfo list(ReportAssetsParams params)
    {
        startPage();
        List<BookReportAssetsRule> list = iBookReportAssetsRuleService.getList(params);
        return getDataTable(list);
    }

    /**
     * 查询资产负债规则金额
     */
    @GetMapping("/balanceSheet")
    public AjaxResult balanceSheet(BookReportAssetsRule rule, Date currentPeriod)
    {
        startPage();
        Map<Object,Object> result = iBookReportAssetsRuleService.balanceSheet(rule,currentPeriod);
        return AjaxResult.success(result);
    }

    /**
     * 修改资产负债规则
     */
    @Log(title = "资产负债规则" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ReportAssetVo reportAssetVo) {
        iBookReportAssetsRuleService.edit(reportAssetVo);
        return AjaxResult.success();
    }

}
