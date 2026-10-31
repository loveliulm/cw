package com.qifu.accounting.accountBook.controller;

import com.qifu.accounting.accountBook.domain.QuantityDetailAccount;
import com.qifu.accounting.accountBook.domain.vo.ParamsVo;
import com.qifu.accounting.accountBook.domain.vo.SubjectVo;
import com.qifu.accounting.accountBook.service.QuantityDetailAccountService;
import com.qifu.accounting.sys.service.IBookLoginService;
import com.qifu.common.utils.Asserts;
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
 * 数量金额明细账
 *  yangyang
 * @date 2020/9/10 11:41
 */
@RestController
@RequestMapping("/accounting/quantityDetail" )
public class QuantityDetailAccountController extends BaseController {
    @Resource
    private QuantityDetailAccountService service;

    @Resource
    private IBookLoginService bookLoginService;


    /**
     * 数量金额明细账-科目列表
     */
    @ApiOperation("数量金额明细账-科目列表")
    @GetMapping("/getSubjectList")
    public TableDataInfo getSubjectList(ParamsVo params)
    {
        startPage();
        List<SubjectVo> list = service.getSubjectList(params);
        return getDataTable(list);
    }

    /**
     * 数量金额明细账-列表
     */
    @ApiOperation("数量金额明细账-列表")
    @GetMapping("/getList")
    public TableDataInfo getList(ParamsVo params)
    {
        startPage();
        Asserts.isNull(params.getSubjectId(),"当前账期还没有数据哦");
        Asserts.isNull(params.getBookId(),"账套不存在");
        List<QuantityDetailAccount> list = service.getList(params);
        return getDataTable(list);
    }

    @ApiOperation("数量金额明细账-导出")
    @Log(title = "数量金额明细账", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(ParamsVo params)
    {
        Asserts.isNull(params.getSubjectId(),"当前账期还没有数据哦");
        Asserts.isNull(params.getBookId(),"账套不存在");
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

        List<QuantityDetailAccount> list = service.getList(params);
        ExcelUtilReport<QuantityDetailAccount> util = new ExcelUtilReport<QuantityDetailAccount>(QuantityDetailAccount.class);

        String bookName = bookLoginService.getInfo(params.getBookId()).getCustomerName();
        String unit = "单位：元";
        Object [] title2 = {bookName,str,unit};
        return util.exportExcel(list, "数量金额明细账",title2);
    }
}
