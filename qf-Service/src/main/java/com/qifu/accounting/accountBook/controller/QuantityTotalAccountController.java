package com.qifu.accounting.accountBook.controller;

import com.qifu.accounting.accountBook.domain.QuantityTotalAccount;
import com.qifu.accounting.accountBook.domain.vo.ParamsVo;
import com.qifu.accounting.accountBook.service.QuantityTotalAccountService;
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
 * 数量金额总账
 *  yangyang
 * @date 2020/9/10 14:46
 */
@RestController
@RequestMapping("/accounting/quantityTotal" )
public class QuantityTotalAccountController extends BaseController {
    @Resource
    private QuantityTotalAccountService service;

    @Resource
    private IBookLoginService bookLoginService;

    /**
     * 数量金额总账
     */
    @ApiOperation("数量金额总账")
    @GetMapping("/getList")
    public TableDataInfo getList(ParamsVo params)
    {
        startPage();
        List<QuantityTotalAccount> list = service.getList(params);
        return getDataTable(list);
    }

    @ApiOperation("数量金额总账-导出")
    @Log(title = "数量金额总账", businessType = BusinessType.EXPORT)
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

        List<QuantityTotalAccount> list = service.getList(params);
        ExcelUtilReport<QuantityTotalAccount> util = new ExcelUtilReport<QuantityTotalAccount>(QuantityTotalAccount.class);

        String bookName = bookLoginService.getInfo(params.getBookId()).getCustomerName();
        String unit = "单位：元";
        Object [] title2 = {bookName,str,unit};
        return util.exportExcel(list, "数量金额总账",title2);
    }
}
