package com.qifu.accounting.accountBook.controller;

import com.qifu.accounting.accountBook.domain.AuxiliaryDetailAccount;
import com.qifu.accounting.accountBook.domain.vo.AuxiliaryAccParamsVo;
import com.qifu.accounting.accountBook.domain.vo.AuxiliaryTypeVo;
import com.qifu.accounting.accountBook.domain.vo.AuxiliaryVo;
import com.qifu.accounting.accountBook.domain.vo.SubjectVo;
import com.qifu.accounting.accountBook.service.AuxiliaryDetailAccountService;
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
 *
 * 辅助核算明细账
 * @date
 */
@RestController
@RequestMapping("/accounting/auxiliaryDetail" )
public class AuxiliaryDetailAccountController extends BaseController {

    @Resource
    private AuxiliaryDetailAccountService service;

    @Resource
    private IBookLoginService bookLoginService;


    /**
     * 辅助核算明细账-科目列表
     */
    @GetMapping("/getSubjectList")
    public TableDataInfo getSubjectList(AuxiliaryAccParamsVo params)
    {
        startPage();
        List<SubjectVo> list = service.getSubjectList(params);
        return getDataTable(list);
    }


    /**
     * 辅助核算-辅助类别
     */
    @GetMapping("/getAuxiliaryTypeList")
    public TableDataInfo getAuxiliaryTypeList(Long subjectId)
    {
        startPage();
        List<AuxiliaryTypeVo> list = service.getAuxiliaryTypeList(subjectId);
        return getDataTable(list);
    }

    /**
     * 辅助核算-类别下的项目
     */
    @GetMapping("/getAuxiliary")
    public TableDataInfo getAuxiliary(AuxiliaryAccParamsVo params)
    {
        startPage();
        Asserts.isNull(params.getAuxiliaryTypeId(),"辅助核算类别不存在");
        List<AuxiliaryVo> list = service.getAuxiliary(params);
        return getDataTable(list);
    }

    /**
     * 辅助核算明细账-列表
     */
    @ApiOperation("查询辅助核算明细账列表")
    @GetMapping("/getList")
    public TableDataInfo getList(AuxiliaryAccParamsVo params)
    {
        startPage();
        Asserts.isNull(params.getAuxiliaryTypeId(),"辅助核算类别不存在");
        Asserts.isNull(params.getSubjectId(),"当前账期还没有数据哦");
        Asserts.isNull(params.getBookId(),"账套不存在");
        List<AuxiliaryDetailAccount> list = service.getList(params);
        return getDataTable(list);
    }

    @ApiOperation("辅助核算明细账-导出")
    @Log(title = "辅助核算明细账", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(AuxiliaryAccParamsVo params)
    {

        Asserts.isNull(params.getAuxiliaryTypeId(),"辅助核算类别不存在");
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

        List<AuxiliaryDetailAccount> list = service.getList(params);
        ExcelUtilReport<AuxiliaryDetailAccount> util = new ExcelUtilReport<AuxiliaryDetailAccount>(AuxiliaryDetailAccount.class);

        String bookName = bookLoginService.getInfo(params.getBookId()).getCustomerName();
        String unit = "单位：元";
        Object [] title2 = {bookName,str,unit};
        return util.exportExcel(list, "辅助核算明细账",title2);
    }







}
