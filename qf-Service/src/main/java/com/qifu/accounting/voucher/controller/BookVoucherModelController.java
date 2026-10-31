package com.qifu.accounting.voucher.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import java.util.*;

import com.qifu.accounting.common.enums.VoucherModelType;
import com.qifu.accounting.common.enums.VoucherTypeEnum;
import com.qifu.accounting.sys.domain.BookPeriod;
import com.qifu.accounting.voucher.domain.vo.BookVoucherModelWageVo;
import com.qifu.accounting.voucher.domain.vo.BookVoucherVo;
import com.qifu.accounting.wage.domain.BookWage;
import com.qifu.common.utils.StringDateUtil;
import com.qifu.common.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.quartz.impl.jdbcjobstore.SimpleTriggerPersistenceDelegate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.qifu.accounting.voucher.domain.BookVoucherModel;
import com.qifu.accounting.voucher.service.IBookVoucherModelService;
import com.qifu.framework.web.controller.BaseController;
import com.qifu.framework.web.domain.AjaxResult;
import com.qifu.common.utils.poi.ExcelUtil;
import com.qifu.framework.web.page.TableDataInfo;

/**
 * 凭证模板Controller
 *
 *
 * 07-16
 */
@Transactional
@Api(tags = "凭证模板")
@AllArgsConstructor
@RestController
@RequestMapping("/accounting/voucherModel" )
public class BookVoucherModelController extends BaseController {

    private final IBookVoucherModelService iBookVoucherModelService;

    /**
     * 查询凭证模板列表
     */
    @PreAuthorize("@ss.hasPermi('accounting:voucherModel:list')")
    @GetMapping("/list")
    public TableDataInfo list(BookVoucherModel bookVoucherModel)
    {
        startPage();
        LambdaQueryWrapper<BookVoucherModel> lqw = new LambdaQueryWrapper<BookVoucherModel>();
        if (bookVoucherModel.getBookId() != null){
            lqw.eq(BookVoucherModel::getBookId ,bookVoucherModel.getBookId());
        }
        if (bookVoucherModel.getAttachmentCount() != null){
            lqw.eq(BookVoucherModel::getAttachmentCount ,bookVoucherModel.getAttachmentCount());
        }
        if (bookVoucherModel.getIsAmount() != null){
            lqw.eq(BookVoucherModel::getIsAmount ,bookVoucherModel.getIsAmount());
        }
        if (StringUtils.isNotBlank(bookVoucherModel.getName())){
            lqw.like(BookVoucherModel::getName ,bookVoucherModel.getName());
        }
        if (bookVoucherModel.getCreateUserId() != null){
            lqw.eq(BookVoucherModel::getCreateUserId ,bookVoucherModel.getCreateUserId());
        }
        lqw.eq(BookVoucherModel::getType, VoucherModelType.普通凭证模板);
        lqw.eq(BookVoucherModel::getDelFlag, StringDateUtil.Del_FLAG_0);
        List<BookVoucherModel> list = iBookVoucherModelService.list(lqw);
        return getDataTable(list);
    }

    /**
     * 导出凭证模板列表
     */
    @Log(title = "凭证模板" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(BookVoucherModel bookVoucherModel) {
        LambdaQueryWrapper<BookVoucherModel> lqw = new LambdaQueryWrapper<BookVoucherModel>(bookVoucherModel);
        List<BookVoucherModel> list = iBookVoucherModelService.list(lqw);
        ExcelUtil<BookVoucherModel> util = new ExcelUtil<BookVoucherModel>(BookVoucherModel. class);
        return util.exportExcel(list, "voucherModel" );
    }

    /**
     * 获取凭证模板详细信息
     */
    @PreAuthorize("@ss.hasPermi('accounting:voucherModel:info')")
    @GetMapping(value = "/{modelId}" )
    public AjaxResult getInfo(@PathVariable("modelId" ) Long modelId) {
        BookVoucherModel model = iBookVoucherModelService.getInfo(modelId);
        return AjaxResult.success(model);
    }

    /**
     * 新增凭证模板
     */
    @ApiOperation("新增凭证模板")
    @Log(title = "凭证模板" , businessType = BusinessType.INSERT)
    @PreAuthorize("@ss.hasPermi('accounting:voucherModel:add')")
    @PostMapping
    public AjaxResult add(@Validated @RequestBody BookVoucherModel bookVoucherModel) {
        iBookVoucherModelService.add(bookVoucherModel);
        return AjaxResult.success();
    }

    /**
     * 修改凭证模板
     */
    @ApiOperation("修改凭证模板")
    @Log(title = "凭证模板" , businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasPermi('accounting:voucherModel:edit')")
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody BookVoucherModel bookVoucherModel) {
        iBookVoucherModelService.edit(bookVoucherModel);
        return AjaxResult.success();
    }

    /**
     * 删除凭证模板
     */
    @Log(title = "凭证模板" , businessType = BusinessType.DELETE)
    @PreAuthorize("@ss.hasPermi('accounting:voucherModel:del')")
    @DeleteMapping("/{modelId}" )
    public AjaxResult remove(@PathVariable Long modelId) {
        iBookVoucherModelService.delete(modelId);
        return AjaxResult.success();
    }

    /*----------------------------------------工资模板-------------------------------------------*/
    /**
     * 工资模板-查询列表
     */
    @PreAuthorize("@ss.hasPermi('accounting:wageVoucherModel:list')")
    @GetMapping("/wage/list")
    public TableDataInfo getListByWage(BookVoucherModel bookVoucherModel)
    {
        startPage();
        LambdaQueryWrapper<BookVoucherModel> lqw = new LambdaQueryWrapper<BookVoucherModel>();
        if (bookVoucherModel.getBookId() != null){
            lqw.eq(BookVoucherModel::getBookId ,bookVoucherModel.getBookId());
        }
        if (StringUtils.isNotBlank(bookVoucherModel.getName())){
            lqw.like(BookVoucherModel::getName ,bookVoucherModel.getName());
        }
        lqw.ne(BookVoucherModel::getType, VoucherModelType.普通凭证模板);
        lqw.eq(BookVoucherModel::getDelFlag, StringDateUtil.Del_FLAG_0);
        List<BookVoucherModel> list = iBookVoucherModelService.list(lqw);
        return getDataTable(list);
    }

    /**
     * 工资模板-获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('accounting:wageVoucherModel:info')")
    @GetMapping(value = "/wage/{modelId}" )
    public AjaxResult getInfoByWage(@PathVariable("modelId" ) Long modelId) {
        BookVoucherModelWageVo model = iBookVoucherModelService.getInfoByWage(modelId);
        return AjaxResult.success(model);
    }

    /**
     * 工资模板-修改
     */
    @ApiOperation("修改凭证模板")
    @Log(title = "凭证模板" , businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasPermi('accounting:wageVoucherModel:edit')")
    @PutMapping("/wage")
    public AjaxResult editByWage(@Validated @RequestBody BookVoucherModel bookVoucherModel) {
        iBookVoucherModelService.editByWage(bookVoucherModel);
        return AjaxResult.success();
    }
}
