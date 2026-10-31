package com.qifu.accounting.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import java.awt.print.Book;
import java.util.Date;
import java.util.List;
import java.util.Arrays;

import com.qifu.common.utils.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.aspectj.weaver.loadtime.Aj;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
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
import com.qifu.accounting.sys.domain.BookAuxiliary;
import com.qifu.accounting.sys.service.IBookAuxiliaryService;
import com.qifu.framework.web.controller.BaseController;
import com.qifu.framework.web.domain.AjaxResult;
import com.qifu.common.utils.poi.ExcelUtil;
import com.qifu.framework.web.page.TableDataInfo;

/**
 * 辅组核算Controller
 *
 *  YM
 * 07-09
 */
@Api(tags = "辅组核算")
@AllArgsConstructor
@RestController
@RequestMapping("/accounting/auxiliary" )
public class BookAuxiliaryController extends BaseController {

    @Autowired
    private final IBookAuxiliaryService iBookAuxiliaryService;

    /**
     * 获取辅助核算编号
     * @param bookId
     * @param auxiliaryTypeId
     * @return
     */
    @GetMapping("/getCode")
    public String getCode(Long bookId,Long auxiliaryTypeId){
        BookAuxiliary auxiliary = iBookAuxiliaryService.getOne(bookId,auxiliaryTypeId);
        if(auxiliary == null){
            return MakerNum.makerNumRuleLength4(null);
        }else {
            return MakerNum.makerNumRuleLength4(auxiliary.getCode());
        }
    }

    /**
     * 查询辅组核算列表
     */
    @ApiOperation("查询辅组核算列表")
    @GetMapping("/list")
    public TableDataInfo list(BookAuxiliary bookAuxiliary)
    {
        startPage();
        LambdaQueryWrapper<BookAuxiliary> lqw = new LambdaQueryWrapper<BookAuxiliary>();
        if (bookAuxiliary.getBookId() != null){
            lqw.eq(BookAuxiliary::getBookId ,bookAuxiliary.getBookId());
        }
        if (StringUtils.isNotBlank(bookAuxiliary.getCode())){
            lqw.eq(BookAuxiliary::getCode ,bookAuxiliary.getCode());
        }
        if (StringUtils.isNotBlank(bookAuxiliary.getName())){
            lqw.like(BookAuxiliary::getName ,bookAuxiliary.getName());
        }
        if (StringUtils.isNotBlank(bookAuxiliary.getPhoneNumber())){
            lqw.eq(BookAuxiliary::getPhoneNumber ,bookAuxiliary.getPhoneNumber());
        }
        if (StringUtils.isNotBlank(bookAuxiliary.getAddress())){
            lqw.eq(BookAuxiliary::getAddress ,bookAuxiliary.getAddress());
        }
        if (bookAuxiliary.getAuxiliaryTypeId() != null){
            lqw.eq(BookAuxiliary::getAuxiliaryTypeId ,bookAuxiliary.getAuxiliaryTypeId());
        }
        if (bookAuxiliary.getCreateUserId() != null){
            lqw.eq(BookAuxiliary::getCreateUserId ,bookAuxiliary.getCreateUserId());
        }
        lqw.eq(BookAuxiliary::getDelFlag,StringDateUtil.Del_FLAG_0);
        List<BookAuxiliary> list = iBookAuxiliaryService.list(lqw);
        return getDataTable(list);
    }

    /**
     * 导出辅组核算列表
     */
    @ApiOperation("导出辅组核算列表")
    @Log(title = "辅组核算" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(BookAuxiliary bookAuxiliary) {
        LambdaQueryWrapper<BookAuxiliary> lqw = new LambdaQueryWrapper<BookAuxiliary>(bookAuxiliary);
        List<BookAuxiliary> list = iBookAuxiliaryService.list(lqw);
        ExcelUtil<BookAuxiliary> util = new ExcelUtil<BookAuxiliary>(BookAuxiliary. class);
        return util.exportExcel(list, "auxiliary" );
    }

    /**
     * 获取辅组核算详细信息
     */
    @ApiOperation("获取辅组核算详细信息")
    @GetMapping(value = "/{auxiliaryId}" )
    public AjaxResult getInfo(@PathVariable("auxiliaryId" ) Long auxiliaryId) {
        return AjaxResult.success(iBookAuxiliaryService.getById(auxiliaryId));
    }

    /**
     * 新增辅组核算
     */
    @ApiOperation("新增辅组核算")
    @PreAuthorize("@ss.hasPermi('accounting:auxiliary:add')")
    @Log(title = "辅组核算" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody BookAuxiliary bookAuxiliary) {
        iBookAuxiliaryService.checkAuxiliaryName(bookAuxiliary);
        bookAuxiliary.setCreateTime(new Date());
        bookAuxiliary.setCreateUserId(SecurityUtils.userId());
        boolean b = iBookAuxiliaryService.save(bookAuxiliary);
        Asserts.isTrue(b,"添加失败");
        return AjaxResult.success();
    }

    /**
     * 修改辅组核算
     */
    @ApiOperation("修改辅组核算")
    @PreAuthorize("@ss.hasPermi('accounting:auxiliary:edit')")
    @Log(title = "辅组核算" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated  @RequestBody BookAuxiliary bookAuxiliary) {
        iBookAuxiliaryService.checkAuxiliaryName(bookAuxiliary);
        boolean b = iBookAuxiliaryService.updateById(bookAuxiliary);
        Asserts.isTrue(b,"修改失败");
        return AjaxResult.success();
    }

    /**
     * 删除辅组核算
     */
    @ApiOperation("删除辅组核算")
    @PreAuthorize("@ss.hasPermi('accounting:auxiliary:del')")
    @Log(title = "辅组核算" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{auxiliaryIds}" )
    public AjaxResult remove(@PathVariable Long[] auxiliaryIds) {
        for (int i = 0; i < auxiliaryIds.length; i++) {
            BookAuxiliary bookAuxiliary = iBookAuxiliaryService.getById(auxiliaryIds[i]);
            bookAuxiliary.setDelFlag(StringDateUtil.Del_FLAG_2);
            iBookAuxiliaryService.updateById(bookAuxiliary);
        }
        return AjaxResult.success();
    }
}
