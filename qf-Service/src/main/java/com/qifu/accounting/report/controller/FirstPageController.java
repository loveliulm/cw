package com.qifu.accounting.report.controller;

import com.qifu.accounting.report.domain.vo.FirstPageFinancial;
import com.qifu.accounting.report.domain.vo.FirstPageParams;
import com.qifu.accounting.report.domain.vo.FirstPageTax;
import com.qifu.accounting.report.service.FirstPageService;
import com.qifu.accounting.voucher.domain.BookVoucher;
import com.qifu.framework.web.controller.BaseController;
import com.qifu.framework.web.page.TableDataInfo;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 首页报表
 *  yangyang
 * @date 2020/9/21 10:27
 */
@AllArgsConstructor
@RestController
@RequestMapping("/accounting/firstPage" )
public class FirstPageController extends BaseController {
    private final FirstPageService firstPageService;

    @GetMapping("/report/getInfo")
    public Map reportInfo(FirstPageParams params){
        Map result = firstPageService.reportInfo(params);
        return result;
    }


    @GetMapping("/voucher/getInfo")
    public TableDataInfo voucherInfo(FirstPageParams params){
        List<BookVoucher> list= firstPageService.voucherInfo(params);
        return getDataTable(list);
    }

    @GetMapping("/subject/getInfo")
    public TableDataInfo subjectInfo(FirstPageParams params){
        List<FirstPageFinancial> list= firstPageService.subjectInfo(params);
        return getDataTable(list);
    }


    @GetMapping("/tax/getInfo")
    public TableDataInfo taxInfo(FirstPageParams params){
        List<FirstPageTax> list= firstPageService.taxInfo(params);
        return getDataTable(list);
    }
}
