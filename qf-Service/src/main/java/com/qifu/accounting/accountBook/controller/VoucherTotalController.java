package com.qifu.accounting.accountBook.controller;

import com.qifu.accounting.accountBook.domain.VoucherTotal;
import com.qifu.accounting.accountBook.domain.vo.ParamsVo;
import com.qifu.accounting.accountBook.service.VoucherTotalService;
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
@RequestMapping("/accounting/voucherTotal" )
public class VoucherTotalController extends BaseController {

    @Resource
    private VoucherTotalService voucherTotalService;

    @Resource
    private IBookLoginService bookLoginService;


    /**
     * 凭证汇总表
     * @param yearPeriod
     * @return
     */
    @GetMapping("/list")
    public TableDataInfo getList(Long bookId, Date yearPeriod){
        startPage();
        ParamsVo params = new ParamsVo();
        params.setStartLevel(1);
        params.setEndLevel(1);
        params.setBookId(bookId);
        params.setStartPeriod(yearPeriod);
        params.setEndPeriod(yearPeriod);
        List<VoucherTotal> list = voucherTotalService.getList(params);
        return getDataTable(list);
    }

    @ApiOperation("导出")
    @Log(title = "凭证汇总表", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(Long bookId,Date yearPeriod)
    {
        ParamsVo params = new ParamsVo();
        params.setStartLevel(1);
        params.setEndLevel(1);
        params.setBookId(bookId);
        params.setStartPeriod(yearPeriod);
        params.setEndPeriod(yearPeriod);
        List<VoucherTotal> list = voucherTotalService.getList(params);
        ExcelUtilReport<VoucherTotal> util = new ExcelUtilReport<VoucherTotal>(VoucherTotal.class);

        String bookName = bookLoginService.getInfo(params.getBookId()).getCustomerName();
        String period = DateUtils.getYear(yearPeriod) + "年" + DateUtils.getMonth(yearPeriod) + "期";
        String unit = "单位：元";
        Object [] title2 = {bookName,period,unit};
        return util.exportExcel(list, "凭证汇总表",title2);
    }
}
