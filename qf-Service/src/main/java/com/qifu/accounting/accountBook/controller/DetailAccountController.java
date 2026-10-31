package com.qifu.accounting.accountBook.controller;

import com.qifu.accounting.accountBook.domain.DetailAccount;
import com.qifu.accounting.accountBook.domain.vo.ParamsVo;
import com.qifu.accounting.accountBook.domain.vo.SubjectVo;
import com.qifu.accounting.accountBook.service.DetailAccountService;
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
 *  yangyang
 * 账簿-明细账
 * @date 2020/7/20 14:34
 */
@RestController
@RequestMapping("/accounting/detailAccount" )
public class DetailAccountController extends BaseController {

    @Resource
    private DetailAccountService detailAccountService;

    @Resource
    private IBookLoginService bookLoginService;

    /**
     * 明细账-科目列表
     */
    @ApiOperation("查询明细账科目列表")
    @GetMapping("/getSubjectList")
    public TableDataInfo getSubjectList(ParamsVo params)
    {
        startPage();
        List<SubjectVo> list = detailAccountService.getSubjectList(params);
        return getDataTable(list);
    }

    /**
     * 明细账-列表
     */
    @ApiOperation("查询明细账列表")
    @GetMapping("/getList")
    public TableDataInfo getList(ParamsVo params)
    {
        startPage();
        Asserts.isNull(params.getSubjectId(),"当前账期还没有数据哦");
        Asserts.isNull(params.getBookId(),"账套不存在");
        List<DetailAccount> list = detailAccountService.getList(params);
        return getDataTable(list);
    }

    @ApiOperation("明细账-导出")
    @Log(title = "明细账", businessType = BusinessType.EXPORT)
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
        String unit = "单位：元";

        List<DetailAccount> list = detailAccountService.getList(params);
        ExcelUtilReport<DetailAccount> util = new ExcelUtilReport<DetailAccount>(DetailAccount.class);

        String bookName = bookLoginService.getInfo(params.getBookId()).getCustomerName();
        Object [] title2 = {bookName,str,unit};
        return util.exportExcel(list, "明细账",title2);
    }
}
