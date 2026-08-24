package com.qifu.project.customer.controller;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Arrays;

import com.qifu.common.utils.Asserts;
import com.qifu.common.utils.DateUtils;
import com.qifu.common.utils.SecurityUtils;
import com.qifu.project.common.enums.CollectionStatusEnum;
import com.qifu.project.common.enums.IsWarningEnum;
import com.qifu.project.customer.domain.TCollection;
import com.qifu.project.customer.service.ITCollectionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
import com.qifu.project.customer.domain.TCollectionRecord;
import com.qifu.project.customer.service.ITCollectionRecordService;
import com.qifu.framework.web.controller.BaseController;
import com.qifu.framework.web.domain.AjaxResult;
import com.qifu.common.utils.poi.ExcelUtil;
import com.qifu.framework.web.page.TableDataInfo;

/**
 * 收款明细Controller
 *
 *  YM
 * 06-28
 */
@Api(tags = "收费管理-明细")
@AllArgsConstructor
@RestController
@RequestMapping("/customer/collection/record" )
public class TCollectionRecordController extends BaseController {

    private final ITCollectionRecordService iTCollectionRecordService;
    @Autowired
    private final ITCollectionService collectionService;

    /**
     * 查询收款明细列表
     */
    @PreAuthorize("@ss.hasPermi('customer:record:list')")
    @GetMapping("/list")
    public TableDataInfo list(TCollectionRecord tCollectionRecord)
    {
        startPage();
        LambdaQueryWrapper<TCollectionRecord> lqw = new LambdaQueryWrapper<TCollectionRecord>();
        lqw.eq(TCollectionRecord::getCollectionId,tCollectionRecord.getCollectionId());
        List<TCollectionRecord> list = iTCollectionRecordService.list(lqw);
        return getDataTable(list);
    }

    /**
     * 导出收款明细列表
     */
    @PreAuthorize("@ss.hasPermi('customer:record:export')" )
    @Log(title = "收款明细" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(TCollectionRecord tCollectionRecord) {
        LambdaQueryWrapper<TCollectionRecord> lqw = new LambdaQueryWrapper<TCollectionRecord>(tCollectionRecord);
        List<TCollectionRecord> list = iTCollectionRecordService.list(lqw);
        ExcelUtil<TCollectionRecord> util = new ExcelUtil<TCollectionRecord>(TCollectionRecord. class);
        return util.exportExcel(list, "record" );
    }

    /**
     * 获取收款明细详细信息
     */
    @PreAuthorize("@ss.hasPermi('customer:record:query')" )
    @GetMapping(value = "/{id}" )
    public AjaxResult getInfo(@PathVariable("id" ) Long id) {
        return AjaxResult.success(iTCollectionRecordService.getById(id));
    }

    /**
     * 新增收款明细
     */
    @ApiOperation("新增代账/工商收款明细")
    @Transactional
    @PreAuthorize("@ss.hasPermi('customer:record:add')" )
    @Log(title = "收款明细" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TCollectionRecord tCollectionRecord) {
        BigDecimal payMoney = tCollectionRecord.getCollectionAmount();
        Asserts.isFalse(payMoney == null || payMoney.compareTo(BigDecimal.ZERO) <= 0,"收款金额不能小于0");
        TCollection collection = collectionService.selectById(tCollectionRecord.getCollectionId());
        Asserts.isNull(collection,"该收款单不存在或已经失效，不能收款");

        tCollectionRecord.setCreateTime(new Date());
        tCollectionRecord.setCollectionDate(new Date());
        tCollectionRecord.setCreateId(SecurityUtils.userId());
        tCollectionRecord.setStatus(CollectionStatusEnum.已收款);
        tCollectionRecord.setServiceClassId(collection.getServiceClassId());
        boolean b = iTCollectionRecordService.save(tCollectionRecord);
        Asserts.isTrue(b,"收款失败");

        BigDecimal amount = collection.getAmount().add(tCollectionRecord.getCollectionAmount());
        BigDecimal debt = collection.getTotal().subtract(amount);
        int i = debt.compareTo(new BigDecimal(0));
        Asserts.isFalse(i < 0,"收款金额超过合同总金额");
        collection.setAmount(amount);
        collection.setDebt(debt);
        collection.setLastPayDate(tCollectionRecord.getEndDate());
        if(BigDecimal.ZERO.compareTo(collection.getDebt()) == 0){
            collection.setIsEnd(1);
            collection.setIsWarning(IsWarningEnum.正常);
        }
        b = collectionService.updateById(collection);
        Asserts.isTrue(b,"收款失败");
        return AjaxResult.success();
    }

    /**
     * 修改收款明细
     */
    @PreAuthorize("@ss.hasPermi('customer:record:edit')" )
    @Log(title = "收款明细" , businessType = BusinessType.UPDATE)
    @PutMapping
    @Transactional
    @ApiOperation("修改收款明细")
    public AjaxResult edit(@RequestBody TCollectionRecord tCollectionRecord) {
        BigDecimal payMoney = tCollectionRecord.getCollectionAmount();
        Asserts.isFalse(payMoney == null || payMoney.compareTo(BigDecimal.ZERO) <= 0,"请输入收款金额");
        //获取未修改前收款明细
        TCollectionRecord tcr = iTCollectionRecordService.getById(tCollectionRecord.getId());
        //收款
        TCollection collection = collectionService.getById(tCollectionRecord.getCollectionId());
        Asserts.isNull(collection,"该收款单不存在或已经失效，不能收款");

        BigDecimal amount = collection.getAmount().subtract(tcr.getCollectionAmount());
        BigDecimal debt = collection.getDebt().add(tcr.getCollectionAmount());

        amount = amount.add(tCollectionRecord.getCollectionAmount());
        debt = collection.getTotal().subtract(amount);
        int i = debt.compareTo(new BigDecimal(0));
        Asserts.isFalse(i < 0,"收款金额超过合同总金额");
        collection.setAmount(amount);
        collection.setDebt(debt);
        collection.setLastPayDate(tCollectionRecord.getEndDate());
        if(BigDecimal.ZERO.compareTo(collection.getDebt()) > -1){
            collection.setIsEnd(1);
            collection.setIsWarning(IsWarningEnum.正常);
        }else{
            collection.setIsEnd(0);
            collection.setIsWarning(IsWarningEnum.已经欠费);
        }
        boolean b = collectionService.updateById(collection);

        tCollectionRecord.setUpdateTime(new Date());
        return toAjax(iTCollectionRecordService.updateById(tCollectionRecord) ? 1 : 0);
    }

//    /**
//     * 删除收款明细
//     */
//    @PreAuthorize("@ss.hasPermi('customer:record:remove')" )
//    @Log(title = "收款明细" , businessType = BusinessType.DELETE)
//    @DeleteMapping("/{ids}" )
//    public AjaxResult remove(@PathVariable Long[] ids) {
//        return toAjax(iTCollectionRecordService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
//    }

    /**
     * 删除收款明细
     */
    @PreAuthorize("@ss.hasPermi('customer:record:remove')" )
    @Log(title = "收款明细" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}" )
    @ApiOperation("删除收款明细")
    @ApiParam(name = "id",value = "收款明细id",required = true)
    public AjaxResult del(@PathVariable Long id){
        AjaxResult ajax = iTCollectionRecordService.deleteById(id);
        return ajax;
    }
}
