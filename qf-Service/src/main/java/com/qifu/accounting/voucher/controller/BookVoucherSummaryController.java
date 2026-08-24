package com.qifu.accounting.voucher.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import java.util.List;
import java.util.Arrays;

import com.qifu.common.utils.Asserts;
import com.qifu.common.utils.StringUtils;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.qifu.framework.aspectj.lang.annotation.Log;
import com.qifu.framework.aspectj.lang.enums.BusinessType;
import com.qifu.accounting.voucher.domain.BookVoucherSummary;
import com.qifu.accounting.voucher.service.IBookVoucherSummaryService;
import com.qifu.framework.web.controller.BaseController;
import com.qifu.framework.web.domain.AjaxResult;
import com.qifu.common.utils.poi.ExcelUtil;
import com.qifu.framework.web.page.TableDataInfo;

/**
 * 摘要Controller
 *
 *
 */
@AllArgsConstructor
@RestController
@RequestMapping("/accounting/summary" )
public class BookVoucherSummaryController extends BaseController {

    private final IBookVoucherSummaryService iBookVoucherSummaryService;

    /**
     * 查询摘要列表
     */
    @GetMapping("/list")
    public TableDataInfo list(BookVoucherSummary bookVoucherSummary)
    {
        Asserts.isNull(bookVoucherSummary.getBookId(),"bookId不能为空！");
        List<BookVoucherSummary> list = iBookVoucherSummaryService.getList(bookVoucherSummary);
        return getDataTable(list);
    }

    /**
     * 获取摘要详细信息
     */
    @GetMapping(value = "/{id}" )
    public AjaxResult getInfo(@PathVariable("id" ) Long id) {
        return AjaxResult.success(iBookVoucherSummaryService.getById(id));
    }

    /**
     * 新增摘要
     */
    @Log(title = "摘要" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BookVoucherSummary bookVoucherSummary) {
        bookVoucherSummary.setIsDefault(Boolean.FALSE);
        return toAjax(iBookVoucherSummaryService.save(bookVoucherSummary) ? 1 : 0);
    }

    /**
     * 修改摘要
     */
    @Log(title = "摘要" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BookVoucherSummary bookVoucherSummary) {
        BookVoucherSummary summary = iBookVoucherSummaryService.getById(bookVoucherSummary.getId());
        Asserts.isFalse(summary.getIsDefault(),"系统摘要不能修改！");
        return toAjax(iBookVoucherSummaryService.updateById(bookVoucherSummary) ? 1 : 0);
    }

    /**
     * 删除摘要
     */
    @Log(title = "摘要" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}" )
    public AjaxResult remove(@PathVariable long id) {
        BookVoucherSummary summary = iBookVoucherSummaryService.getById(id);
        Asserts.isFalse(summary.getIsDefault(),"系统摘要不能删除！");
        iBookVoucherSummaryService.removeById(id);
        return AjaxResult.success();
    }
}
