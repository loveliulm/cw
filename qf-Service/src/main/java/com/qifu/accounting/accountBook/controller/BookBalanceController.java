package com.qifu.accounting.accountBook.controller;

import com.qifu.accounting.accountBook.domain.BookBalance;
import com.qifu.accounting.accountBook.domain.vo.ParamsVo;
import com.qifu.accounting.accountBook.service.BookBalanceService;
import com.qifu.accounting.sys.service.IBookLoginService;
import com.qifu.common.utils.DateUtils;
import com.qifu.common.utils.poi.ExcelUtilReport;
import com.qifu.framework.aspectj.lang.annotation.Log;
import com.qifu.framework.aspectj.lang.enums.BusinessType;
import com.qifu.framework.web.controller.BaseController;
import com.qifu.framework.web.domain.AjaxResult;
import com.qifu.framework.web.page.TableDataInfo;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 科目余额表
 *
 */
@RestController
@RequestMapping("/accounting/bookBalance" )
public class BookBalanceController extends BaseController {

    @Resource
    private BookBalanceService bookBalanceService;

    @Resource
    private IBookLoginService bookLoginService;

    /**
     * 科目余额表
     */
    @ApiOperation("科目余额表")
    @GetMapping("/getList")
    public TableDataInfo getList(ParamsVo params)
    {
        startPage();
        List<BookBalance> list = bookBalanceService.getList(params);
        return getDataTable(list);
    }

    @ApiOperation("科目余额表-导出")
    @Log(title = "科目余额表", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(ParamsVo params)
    {
        Date startPeriod = params.getStartPeriod();
        String startPeriodStr = DateUtils.getYear(startPeriod) + "年" + DateUtils.getMonth(startPeriod) + "期";
        Date endPeriod = params.getEndPeriod();
        String endPeriodStr = DateUtils.getYear(endPeriod) + "年" + DateUtils.getMonth(endPeriod) + "期";
        String str = "";
        if(startPeriodStr.equals(endPeriodStr)){
            str = startPeriodStr;
        }else {
            str = startPeriodStr + "-" + endPeriodStr;
        }

        List<BookBalance> list = bookBalanceService.getList(params);
        ExcelUtilReport<BookBalance> util = new ExcelUtilReport<BookBalance>(BookBalance.class);

        String bookName = bookLoginService.getInfo(params.getBookId()).getCustomerName();
        String unit = "单位：元";
        Object [] title2 = {bookName,str,unit};
        return util.exportExcel(list, "科目余额表",title2);
    }
}
