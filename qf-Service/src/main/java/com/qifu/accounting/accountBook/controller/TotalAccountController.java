package com.qifu.accounting.accountBook.controller;

import com.qifu.accounting.accountBook.domain.vo.ParamsVo;
import com.qifu.accounting.accountBook.domain.vo.TotalAccResult;
import com.qifu.accounting.accountBook.service.TotalAccountService;
import com.qifu.common.utils.poi.ExcelUtil;
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
import java.util.List;

/**
 *
 */
@RestController
@RequestMapping("/accounting/totalAccount" )
public class TotalAccountController extends BaseController {
    @Resource
    private TotalAccountService totalAccountService;

    /**
     * 总账-列表
     */
    @ApiOperation("查询总账列表")
    @GetMapping("/getList")
    public TableDataInfo getListAll(ParamsVo params)
    {
        startPage();
        List<TotalAccResult> list = totalAccountService.getListAll(params);
        return getDataTable(list);
    }

    @ApiOperation("导出")
    @Log(title = "总账", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(ParamsVo params)
    {
        List<TotalAccResult> list = totalAccountService.getListAll(params);
        ExcelUtil<TotalAccResult> util = new ExcelUtil<TotalAccResult>(TotalAccResult.class);
        return util.exportExcel(list, "总账");
    }
}
