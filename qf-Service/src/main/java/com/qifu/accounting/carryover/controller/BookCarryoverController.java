package com.qifu.accounting.carryover.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import java.math.BigDecimal;
import java.util.*;

import com.qifu.accounting.sys.domain.BookPeriod;
import com.qifu.accounting.sys.service.IBookPeriodService;
import com.qifu.common.utils.Asserts;
import com.qifu.common.utils.DateUtils;
import com.qifu.common.utils.StringDateUtil;
import com.qifu.common.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
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
import com.qifu.accounting.carryover.domain.BookCarryover;
import com.qifu.accounting.carryover.service.IBookCarryoverService;
import com.qifu.framework.web.controller.BaseController;
import com.qifu.framework.web.domain.AjaxResult;
import com.qifu.common.utils.poi.ExcelUtil;
import com.qifu.framework.web.page.TableDataInfo;

/**
 * 结转Controller
 *
 *
 */
@Api(tags = "结转")
@Transactional
@AllArgsConstructor
@RestController
@RequestMapping("/accounting/carryover" )
public class BookCarryoverController extends BaseController {

    private final IBookCarryoverService iBookCarryoverService;

    /**
     * 查询结转列表
     */
    @GetMapping("/list")
    public TableDataInfo list(BookCarryover bookCarryover)
    {
        startPage();
        List<BookCarryover> list = iBookCarryoverService.getList(bookCarryover);

        return getDataTable(list);
    }

    /**
     * 结转销售成本百分比设置
     * @param carryoverId
     * @param percentage
     * @return
     */
    @PutMapping("/salesCost/percentage")
    public AjaxResult resetSalesCostPercentage(Long carryoverId, BigDecimal percentage){
        Asserts.isNull(percentage,"百分比不能为空");
        iBookCarryoverService.resetSalesCostPercentage(carryoverId,percentage);
        return AjaxResult.success();
    }



    /**
     * 结转销售成本百分比查询
     * @param carryoverId
     * @return
     */
    @GetMapping("/select/percentage")
    public AjaxResult selectSalesCostPercentage(Long carryoverId){
        Map<String,BigDecimal> result = new HashMap<>();
        BigDecimal salesCostPercentage = iBookCarryoverService.selectSalesCostPercentage(carryoverId);
        result.put("percentage",salesCostPercentage);
        return AjaxResult.success(result);
    }

    /**
     * 结转损益是否分开结转
     * @param carryoverId
     * @return
     */
    @GetMapping("/select/profitLossOpen")
    public AjaxResult selectProfitLossOpen(Long carryoverId){
        Map<String,Boolean> result = new HashMap<>();
        Boolean profitLossOpen = iBookCarryoverService.selectProfitLossOpen(carryoverId);
        result.put("profitLossOpen",profitLossOpen);
        return AjaxResult.success(result);
    }

    /**
     * 结转损益是否分开结转
     * @param carryoverId
     * @param profitLossOpen true:分开结转，false:不分开结转
     * @return
     */
    @PutMapping("/profitLossOpen")
    public AjaxResult updateProfitLossOpen(Long carryoverId,Boolean profitLossOpen){
        iBookCarryoverService.updateProfitLossOpen(carryoverId,profitLossOpen);
        return AjaxResult.success();
    }

    /**
     * 新增结转
     */
//    @PreAuthorize("@ss.hasPermi('accounting:carryover:add')" )
    @ApiOperation("新增结转")
    @PreAuthorize("@ss.hasPermi('accounting:carryover:add')")
    @Log(title = "结转" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody BookCarryover bookCarryover) {
        iBookCarryoverService.add(bookCarryover);
        return AjaxResult.success();
    }

    /**
     * 修改结转
     */
//    @PreAuthorize("@ss.hasPermi('accounting:carryover:edit')" )
    @ApiOperation("修改结转")
    @PreAuthorize("@ss.hasPermi('accounting:carryover:edit')")
    @Log(title = "结转" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody BookCarryover bookCarryover) {
        iBookCarryoverService.edit(bookCarryover);
        return AjaxResult.success();
    }

    /**
     * 删除结转
     */
//    @PreAuthorize("@ss.hasPermi('accounting:carryover:remove')" )
    @PreAuthorize("@ss.hasPermi('accounting:carryover:del')")
    @Log(title = "结转" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{carryoverId}" )
    public AjaxResult remove(@PathVariable Long carryoverId) {
        BookCarryover bookCarryover = iBookCarryoverService.getById(carryoverId);
        Asserts.isFalse(bookCarryover.getIsModel(),"该模块为初始化模块不能被删除");
        bookCarryover.setDelFlag(StringDateUtil.Del_FLAG_2);
        iBookCarryoverService.updateById(bookCarryover);
        return AjaxResult.success();
    }

    /**
     * 一键测算(有勾选的一键测算)
     */
    @GetMapping("/calculation")
    public Map<?,?> calculation(Long [] carryoverIds,Long bookId)
    {
        Map<?,?> result = iBookCarryoverService.calculation(carryoverIds,bookId);
        return result;
    }

    /**
     * 测算所有
     */
    @GetMapping("/calculation/all")
    public Map<?,?> calculationAll(Long bookId)
    {
        Map<?,?> result = iBookCarryoverService.calculationAll(bookId);
        return result;
    }

    /**
     * 生成凭证
     * @param carryoverId
     * @return
     */
    @PreAuthorize("@ss.hasPermi('accounting:carryover:generate')")
    @PostMapping("/generate/voucher")
    public Map<?,?> generateVoucher(Long carryoverId)
    {
        Map<?,?> result = iBookCarryoverService.generateVoucher(carryoverId);
        return result;
    }

    /**
     * 结转到下期
     * @return
     */
    @PreAuthorize("@ss.hasPermi('accounting:carryover:carryoverNextPeriod')")
    @PostMapping("/nextPeriod")
    public AjaxResult carryoverNextPeriod(Long bookId){
        iBookCarryoverService.carryoverNextPeriod(bookId);
        return AjaxResult.success();
    }

    /**
     * 反结账
     * @return
     */
    @PreAuthorize("@ss.hasPermi('accounting:carryover:backPeriod')")
    @PostMapping("/backPeriod")
    public AjaxResult backCarryoverPeriod(Long bookId){
        iBookCarryoverService.backCarryoverPeriod(bookId);
        return AjaxResult.success();
    }


    /**
     * 获取当前账期凭证个数
     * @param bookId
     * @return
     */
    @GetMapping("/voucherNum")
    public AjaxResult voucherNum(Long bookId){
        Map<String, Integer> result = new HashMap<>();
        Integer voucherNum = iBookCarryoverService.voucherNum(bookId);
        result.put("voucherNum",voucherNum);
        return AjaxResult.success(result);
    }
}
