package com.qifu.accounting.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import java.util.List;
import java.util.Arrays;

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
import com.qifu.accounting.sys.domain.BookAuxiliaryType;
import com.qifu.accounting.sys.service.IBookAuxiliaryTypeService;
import com.qifu.framework.web.controller.BaseController;
import com.qifu.framework.web.domain.AjaxResult;
import com.qifu.common.utils.poi.ExcelUtil;
import com.qifu.framework.web.page.TableDataInfo;

/**
 * 辅组核算类型Controller
 *
 *  YM
 * 07-09
 */
@AllArgsConstructor
@RestController
@RequestMapping("/accounting/auxiliaryType" )
public class BookAuxiliaryTypeController extends BaseController {

    private final IBookAuxiliaryTypeService iBookAuxiliaryTypeService;

    /**
     * 查询辅组核算类型列表
     */
    @GetMapping("/list")
    public TableDataInfo list()
    {
        startPage();
        LambdaQueryWrapper<BookAuxiliaryType> lqw = new LambdaQueryWrapper<BookAuxiliaryType>();
        List<BookAuxiliaryType> list = iBookAuxiliaryTypeService.list(lqw);
        return getDataTable(list);
    }

    /**
     * 获取辅组核算类型详细信息
     */
    @GetMapping(value = "/{auxiliaryTypeId}" )
    public AjaxResult getInfo(@PathVariable("auxiliaryTypeId" ) Long auxiliaryTypeId) {
        return AjaxResult.success(iBookAuxiliaryTypeService.getById(auxiliaryTypeId));
    }
}
