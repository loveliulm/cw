package com.qifu.accounting.accountBook.controller;

import com.qifu.accounting.accountBook.domain.AuxiliaryTotalAccount;
import com.qifu.accounting.accountBook.domain.vo.AuxiliaryAccParamsVo;
import com.qifu.accounting.accountBook.domain.vo.SubjectVo;
import com.qifu.accounting.accountBook.service.AuxiliaryTotalAccountService;
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
 *
 */
@RestController
@RequestMapping("/accounting/auxiliaryTotal" )
public class AuxiliaryTotalAccountController extends BaseController {

    @Resource
    private AuxiliaryTotalAccountService service;

    @Resource
    private IBookLoginService bookLoginService;

    /**
     * 辅助核算总账-科目列表
     */
    @GetMapping("/getSubjectList")
    public TableDataInfo getSubjectList(AuxiliaryAccParamsVo params)
    {
        startPage();
        List<SubjectVo> list = service.getSubjectList(params);
        return getDataTable(list);
    }

    /**
     * 辅助核算总账-列表
     */
    @ApiOperation("辅助核算总账列表")
    @GetMapping("/getList")
    public TableDataInfo getList(AuxiliaryAccParamsVo params)
    {
        startPage();
        List<AuxiliaryTotalAccount> list = service.getList(params);
        return getDataTable(list);
    }

    @ApiOperation("辅助核算总账-导出")
    @Log(title = "辅助核算总账", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(AuxiliaryAccParamsVo params)
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

        List<AuxiliaryTotalAccount> list = service.getList(params);
        ExcelUtilReport<AuxiliaryTotalAccount> util = new ExcelUtilReport<AuxiliaryTotalAccount>(AuxiliaryTotalAccount.class);

        String bookName = bookLoginService.getInfo(params.getBookId()).getCustomerName();
        String unit = "单位：元";
        Object [] title2 = {bookName,str,unit};
        return util.exportExcel(list, "辅助核算总账",title2);
    }
}
