package com.qifu.accounting.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import java.util.List;
import java.util.Arrays;

import com.qifu.accounting.sys.domain.vo.BookSubjectAuxiliaryVo;
import com.qifu.common.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
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
import com.qifu.accounting.sys.domain.BookSubjectAuxiliary;
import com.qifu.accounting.sys.service.IBookSubjectAuxiliaryService;
import com.qifu.framework.web.controller.BaseController;
import com.qifu.framework.web.domain.AjaxResult;
import com.qifu.common.utils.poi.ExcelUtil;
import com.qifu.framework.web.page.TableDataInfo;

/**
 * 科目辅组核算关联Controller
 *
 *  YM
 * 08-31
 */
@Api(tags = "科目辅组核算")
@AllArgsConstructor
@RestController
@Transactional
@RequestMapping("/accounting/subjectAuxiliary" )
public class BookSubjectAuxiliaryController extends BaseController {

    private final IBookSubjectAuxiliaryService iBookSubjectAuxiliaryService;

    /**
     * 查询科目辅组核算关联列表
     */
    @ApiOperation("科目辅组核算-查询")
    @GetMapping("/list")
    public TableDataInfo list(BookSubjectAuxiliary bookSubjectAuxiliary)
    {
        startPage();
        iBookSubjectAuxiliaryService.judge(bookSubjectAuxiliary.getSubjectId());
        List<BookSubjectAuxiliary> list = iBookSubjectAuxiliaryService.getList(bookSubjectAuxiliary);
        return getDataTable(list);
    }

    /**
     * 获取科目辅组核算关联详细信息
     */
    @GetMapping(value = "/{id}" )
    public AjaxResult getInfo(@PathVariable("id" ) Long id) {
        return AjaxResult.success(iBookSubjectAuxiliaryService.getById(id));
    }

    /**
     * 新增科目辅组核算关联
     */
    @Log(title = "科目辅组核算关联" , businessType = BusinessType.INSERT)
    @ApiOperation("科目辅组核算-新增")
    @PostMapping
    public AjaxResult add(@RequestBody BookSubjectAuxiliaryVo subjectAuxiliaryVo) throws Exception{
        iBookSubjectAuxiliaryService.judge(subjectAuxiliaryVo.getSubjectId());
        iBookSubjectAuxiliaryService.add(subjectAuxiliaryVo);
        return AjaxResult.success();
    }

    /**
     * 修改科目辅组核算关联
     */
    @Log(title = "科目辅组核算关联" , businessType = BusinessType.UPDATE)
    @ApiOperation("科目辅组核算-修改")
    @PutMapping
    public AjaxResult edit(@RequestBody BookSubjectAuxiliaryVo subjectAuxiliaryVo) throws Exception{
        iBookSubjectAuxiliaryService.judge(subjectAuxiliaryVo.getSubjectId());
        iBookSubjectAuxiliaryService.edit(subjectAuxiliaryVo);
        return AjaxResult.success();
    }


}
