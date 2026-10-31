package com.qifu.accounting.report.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.qifu.accounting.report.domain.vo.ReportAssetsParams;
import com.qifu.accounting.sys.service.IBookLoginService;
import com.qifu.accounting.sys.service.IBookSubjectService;
import com.qifu.common.utils.DateUtils;
import com.qifu.common.utils.poi.ExcelUtilReport;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.qifu.framework.aspectj.lang.annotation.Log;
import com.qifu.framework.aspectj.lang.enums.BusinessType;
import com.qifu.accounting.report.domain.BookReportAssets;
import com.qifu.accounting.report.service.IBookReportAssetsService;
import com.qifu.framework.web.controller.BaseController;
import com.qifu.framework.web.domain.AjaxResult;

/**
 * 资产负债模板Controller
 *
 *  YM
 * 09-03
 */
@AllArgsConstructor
@RestController
@RequestMapping("/accounting/assetsReport" )
public class BookReportAssetsController extends BaseController {

    private final IBookReportAssetsService iBookReportAssetsService;

    private final IBookSubjectService bookSubjectService;

    private final IBookLoginService bookLoginService;

    /**
     * 资产负债列表
     */
    @GetMapping("/list")
    public Map list(ReportAssetsParams params)
    {
        startPage();
        params.resetLastYearPeriod();
        List<BookReportAssets> list = iBookReportAssetsService.getList(params);
        Map<Object,Object> result = new HashMap<>();
        result.put("rows",list);
        result.put("success",true);
        result.put("initialBalance","期初余额：平衡");
        result.put("yearInitBalance","年初数：平衡");
        //测试平衡
        BookReportAssets assets = list.get(list.size()-1);
        if(assets.getAssetsEndBalance().doubleValue() != assets.getDebtEndBalance().doubleValue()||
                assets.getAssetsYearInitBalance().doubleValue() != assets.getDebtYearInitBalance().doubleValue()){
            result.put("success",false);
        }
        if(assets.getAssetsEndBalance().doubleValue() != assets.getDebtEndBalance().doubleValue()){
            result.put("initialBalance","期初余额：不平衡");
        }
        if(assets.getAssetsYearInitBalance().doubleValue() != assets.getDebtYearInitBalance().doubleValue()){
            result.put("yearInitBalance","年初数：不平衡，请检查科目期初是否试算平衡且损益类科目年初余额是否都为0");
        }
        return result;
    }

    /**
     * 资产负债列表-导出
     */
    //@PreAuthorize("@ss.hasPermi('accounting:assetsReportM:export')" )
    @Log(title = "资产负债表" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(ReportAssetsParams params) {
        params.resetLastYearPeriod();
        List<BookReportAssets> list = iBookReportAssetsService.getList(params);
        ExcelUtilReport<BookReportAssets> util = new ExcelUtilReport<BookReportAssets>(BookReportAssets. class);

        String bookName = bookLoginService.getInfo(params.getBookId()).getCustomerName();
        Date yearPeriod = params.getCurrentPeriod();
        String period = DateUtils.getYear(yearPeriod) + "年" + DateUtils.getMonth(yearPeriod) + "期";
        String unit = "单位：元";
        Object [] title2 = {bookName,period,unit};
        return util.exportExcel(list, "资产负债表",title2 );
    }

    //初始化
//    @PostMapping("/init" )
//    public void initInsetReportAssets(Long bookId, Long templateId){
//        iBookReportAssetsService.initInsetReportAssets(bookId, templateId);
//    }

}
