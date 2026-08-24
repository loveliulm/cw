package com.qifu.project.customer.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.qifu.common.utils.*;
import com.qifu.project.base.domain.BaseServiceItem;
import com.qifu.project.base.domain.BaseServiceType;
import com.qifu.project.base.service.IBaseServiceTypeService;
import com.qifu.project.commerce.domain.CommerceService;
import com.qifu.project.commerce.service.ICommerceServiceService;
import com.qifu.project.common.enums.CollectionStatusEnum;
import com.qifu.project.common.enums.IsWarningEnum;
import com.qifu.project.customer.domain.TCollection;
import com.qifu.project.customer.domain.vo.CollectionVo;
import com.qifu.project.customer.service.ITCollectionService;
import com.qifu.project.system.domain.SysNumberRule;
import com.qifu.project.system.mapper.SysUserMapper;
import com.qifu.project.system.service.ISysNumberRuleService;
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
import com.qifu.project.customer.domain.TContract;
import com.qifu.project.customer.service.ITContractService;
import com.qifu.framework.web.controller.BaseController;
import com.qifu.framework.web.domain.AjaxResult;
import com.qifu.common.utils.poi.ExcelUtil;
import com.qifu.framework.web.page.TableDataInfo;

/**
 * 合同Controller
 *
 *  YM
 * 06-18
 */
@Api(tags = "合同管理")
@AllArgsConstructor
@RestController
@RequestMapping("/customer/contract" )
public class TContractController extends BaseController {

    private final ITContractService iTContractService;

    @Autowired
    private ISysNumberRuleService sysNumberRuleService;

    @Autowired
    private ITCollectionService collectionservice;

    @Autowired
    private IBaseServiceTypeService typeService;

    private ICommerceServiceService commerceServiceService;

    /**
     * 获取合同编号规则
     */
    @ApiOperation("合同编号生成规则")
    @GetMapping("/getContractNo")
    public synchronized String getContractNo(){
        String contractNo = null;
        Long deptId = SecurityUtils.companyId();
        //查找规则
        SysNumberRule sysNumberRules = sysNumberRuleService.findByRuleType(2);
        TContract contract = iTContractService.findFirstByDeptId(deptId);
        if (contract == null){
            contractNo = MakerNum.makerNumRule(sysNumberRules.getPrefix(), null);
        } else {
            contractNo = MakerNum.makerNumRule(sysNumberRules.getPrefix(), contract.getContractNo());
        }
        return contractNo;
    }

    /**
     * 查询合同列表
     */
    @ApiOperation("查询合同列表")
    @PreAuthorize("@ss.hasPermi('customer:contract:list')")
    @GetMapping("/list")
    public TableDataInfo list(TContract tContract)
    {
        tContract.setDeptId(SecurityUtils.companyId());
        startPage();

        List<TContract> list = iTContractService.selectContract(tContract);
        return getDataTable(list);
    }

    /**
     * 导出合同列表
     */
    @ApiOperation("导出合同列表")
    @PreAuthorize("@ss.hasPermi('customer:contract:export')" )
    @Log(title = "合同导出" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(TContract tContract) {
        tContract.setDeptId(SecurityUtils.companyId());
        List<TContract> list = iTContractService.selectContract(tContract);
        ExcelUtil<TContract> util = new ExcelUtil<TContract>(TContract. class);
        return util.exportExcel(list, "contract" );
    }

    /**
     * 获取合同详细信息
     */
    @ApiOperation("获取合同详细信息")
    @PreAuthorize("@ss.hasPermi('customer:contract:query')" )
    @GetMapping(value = "/{contractId}" )
    public AjaxResult getInfo(@PathVariable("contractId" ) Long contractId) {
        TContract tContract = iTContractService.getById(contractId);
        AjaxResult ajax = AjaxResult.success(tContract);

        if(tContract.getServiceTypeId() != 1){
            LambdaQueryWrapper<CommerceService> lqw = new LambdaQueryWrapper();
            lqw.eq(CommerceService::getContractId,contractId);
            CommerceService cs = commerceServiceService.getOne(lqw);
            //区分服务类型(0:工商服务，1:记账)
            ajax.put("type",0);
            if(cs != null && cs.getStatus() > 0){
                ajax.put("serviceStatus",1);
            }else{
                ajax.put("serviceStatus",0);
            }
        }else{
            LambdaQueryWrapper<TCollection> lqw = new LambdaQueryWrapper();
            lqw.eq(TCollection::getContractId,contractId);
            TCollection collection = collectionservice.getOne(lqw);
            //区分服务类型(0:工商服务，1:记账)
            ajax.put("type",1);
            if (collection!= null && collection.getAmount().compareTo(BigDecimal.ZERO) == 1){
                ajax.put("serviceStatus",1);
            }else{
                ajax.put("serviceStatus",0);
            }
        }
        return ajax;
    }

    /**
     * 新增合同
     */
    @ApiOperation("新增合同")
    @PreAuthorize("@ss.hasPermi('customer:contract:add')" )
    @Log(title = "新增合同" , businessType = BusinessType.INSERT)
    @Transactional
    @PostMapping
    public AjaxResult add(@RequestBody TContract tContract) {
        tContract.setCreateBy(SecurityUtils.getUsername());
        tContract.setDeptId(SecurityUtils.companyId());//二级部门（公司id）
        tContract.setCreateTime(new Date());
        tContract.setStatus(CollectionStatusEnum.未到期.name());
        tContract.setDelFlag(StringDateUtil.Del_FLAG_0);
        boolean b = iTContractService.save(tContract);
        Asserts.isTrue(b,"新增合同失败");

        //服务类型
        Long serviceType = typeService.getServiceClassId(tContract.getServiceTypeId());

        //新增收款
        TCollection co = new TCollection();
        co.setDeptId(SecurityUtils.companyId());
        co.setContractId(tContract.getContractId());
        co.setCreateTime(new Date());
        co.setIsEnd(0);
        co.setTotal(tContract.getReceivedMoney());
        co.setAmount(new BigDecimal(0));
        co.setDebt(tContract.getReceivedMoney().subtract(co.getAmount()));
        co.setIsNotice(0);
        co.setLastPayDate(tContract.getContractStartDate());
        co.setIsWarning(IsWarningEnum.已经欠费);
        co.setIsDel(StringDateUtil.Del_FLAG_0);
//        co.setServiceClassId(typeService.getServiceClassId(tContract.getServiceTypeId()));
        co.setServiceClassId(serviceType);
        b = collectionservice.save(co);
        Asserts.isTrue(b,"新增合同失败");

        //工商服务任务
        //判断合同服务类型
        if(serviceType != null && serviceType != 1L){
            Asserts.isFalse(tContract.getPayType() != 1L,"工商服务不能分期付款");
            CommerceService cs = new CommerceService();
            cs.setContractId(tContract.getContractId());
            cs.setCreateBy(SecurityUtils.userId());
            cs.setCreateTime(new Date());
            cs.setStatus(StringDateUtil.TASK_TYPE_0);
            cs.setIsDel(StringDateUtil.Del_FLAG_0);
            b = commerceServiceService.save(cs);
            Asserts.isTrue(b,"新增合同失败");
        }
        return AjaxResult.success();
    }

    /**
     * 修改合同
     */
    @Transactional
    @ApiOperation("修改合同")
    @PreAuthorize("@ss.hasPermi('customer:contract:edit')" )
    @Log(title = "修改合同" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TContract tContract) {
        tContract.setUpdateTime(new Date());

        LambdaUpdateWrapper<TCollection> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(TCollection::getContractId,tContract.getContractId());
        //收款服务修改
        TCollection co = collectionservice.getOne(lambdaUpdateWrapper);
        co.setTotal(tContract.getReceivedMoney());
        co.setLastPayDate(tContract.getContractStartDate());
        co.setServiceClassId(typeService.getServiceClassId(tContract.getServiceTypeId()));
        co.setDebt(tContract.getReceivedMoney().subtract(co.getAmount()));
        if(co.getDebt().compareTo(BigDecimal.ZERO) == 1){
            co.setIsEnd(Integer.valueOf(StringDateUtil.STATUS_0));
        }

        lambdaUpdateWrapper.eq(TCollection::getId,co.getId());
        boolean b = collectionservice.update(co,lambdaUpdateWrapper);
        Asserts.isTrue(b,"修改合同失败");

        return toAjax(iTContractService.updateById(tContract) ? 1 : 0);
    }

    /**
     * 删除合同
     */
    @ApiOperation("删除合同")
    @PreAuthorize("@ss.hasPermi('customer:contract:remove')" )
    @Log(title = "删除合同" , businessType = BusinessType.UPDATE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable("ids") long[] ids) {
        return toAjax(iTContractService.deleteContractByIds(ids));
    }

}
