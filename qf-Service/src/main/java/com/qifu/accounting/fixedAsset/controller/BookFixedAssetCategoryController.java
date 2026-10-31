package com.qifu.accounting.fixedAsset.controller;

import java.util.Date;
import java.util.List;

import com.qifu.accounting.fixedAsset.domain.BookFixedAssetCategory;
import com.qifu.common.utils.MakerNum;
import com.qifu.common.utils.StringDateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
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
import com.qifu.accounting.fixedAsset.service.IBookFixedAssetCategoryService;
import com.qifu.framework.web.controller.BaseController;
import com.qifu.framework.web.domain.AjaxResult;
import com.qifu.common.utils.poi.ExcelUtil;
import com.qifu.framework.web.page.TableDataInfo;

/**
 * 固定资产类别Controller
 *
 *  YM
 * 07-27
 */
@Api(tags = "固定资产类别")
@AllArgsConstructor
@RestController
@RequestMapping("/accounting/fixedAssertCategroy" )
public class BookFixedAssetCategoryController extends BaseController {

    private final IBookFixedAssetCategoryService iBookFixedAssetCategoryService;

    /**
     * 获取编号
     * @param bookId
     * @return
     */
    @GetMapping("/getCode")
    public String getCode(Long bookId){
        BookFixedAssetCategory category = iBookFixedAssetCategoryService.getOne(bookId);
        if(category == null){
            return MakerNum.makerNumRuleLength3(null);
        }else {
            return MakerNum.makerNumRuleLength3(category.getCode());
        }
    }

    /**
     * 查询固定资产类别列表
     */
    @PreAuthorize("@ss.hasPermi('accounting:fixedAssertCategroy:list')")
    @GetMapping("/list")
    public TableDataInfo getList(BookFixedAssetCategory bookFixedAssetCategroy)
    {
        startPage();
        List<BookFixedAssetCategory> list = iBookFixedAssetCategoryService.getList(bookFixedAssetCategroy);
        return getDataTable(list);
    }

    /**
     * 导出固定资产类别列表
     */
//    @PreAuthorize("@ss.hasPermi('accounting:fixedAssertCategroy:export')" )
    @Log(title = "固定资产类别" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(BookFixedAssetCategory bookFixedAssetCategroy) {
        List<BookFixedAssetCategory> list = iBookFixedAssetCategoryService.getList(bookFixedAssetCategroy);
        ExcelUtil<BookFixedAssetCategory> util = new ExcelUtil<BookFixedAssetCategory>(BookFixedAssetCategory. class);
        return util.exportExcel(list, "fixedAssertCategroy" );
    }

    /**
     * 获取固定资产类别详细信息
     */
//    @PreAuthorize("@ss.hasPermi('accounting:fixedAssertCategroy:query')" )
    @GetMapping(value = "/{categoryId}" )
    public AjaxResult getInfo(@PathVariable("categoryId" ) Long categoryId) {
        return AjaxResult.success(iBookFixedAssetCategoryService.getById(categoryId));
    }

    /**
     * 新增固定资产类别
     */
    @PreAuthorize("@ss.hasPermi('accounting:fixedAssertCategroy:add')" )
    @ApiOperation("新增固定资产类别")
    @Log(title = "固定资产类别" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody BookFixedAssetCategory bookFixedAssetCategroy) {
        bookFixedAssetCategroy.setCreateTime(new Date());
        bookFixedAssetCategroy.setUpdateTime(new Date());
        return toAjax(iBookFixedAssetCategoryService.save(bookFixedAssetCategroy) ? 1 : 0);
    }

    /**
     * 修改固定资产类别
     */
    @PreAuthorize("@ss.hasPermi('accounting:fixedAssertCategroy:edit')" )
    @ApiOperation("修改固定资产类别")
    @Log(title = "固定资产类别" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody BookFixedAssetCategory bookFixedAssetCategroy) {
        bookFixedAssetCategroy.setUpdateTime(new Date());
        return toAjax(iBookFixedAssetCategoryService.updateById(bookFixedAssetCategroy) ? 1 : 0);
    }

    /**
     * 删除固定资产类别
     */
    @PreAuthorize("@ss.hasPermi('accounting:fixedAssertCategroy:del')" )
    @Log(title = "固定资产类别" , businessType = BusinessType.DELETE)
    @Transactional
    @DeleteMapping("/{categoryIds}" )
    public AjaxResult remove(@PathVariable Long[] categoryIds) {
        for (int i = 0; i < categoryIds.length; i++) {
            BookFixedAssetCategory category = new BookFixedAssetCategory();
            category.setCategoryId(categoryIds[i]);
            category.setDelFlag(StringDateUtil.Del_FLAG_2);
            category.setUpdateTime(new Date());
            iBookFixedAssetCategoryService.updateById(category);
        }
        return AjaxResult.success();
    }



}
