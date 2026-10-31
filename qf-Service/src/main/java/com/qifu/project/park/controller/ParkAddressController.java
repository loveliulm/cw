package com.qifu.project.park.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qifu.common.utils.SecurityUtils;
import com.qifu.common.utils.StringUtils;
import com.qifu.common.utils.bean.BeanConverUtil;
import com.qifu.common.utils.poi.ExcelUtil;
import com.qifu.framework.aspectj.lang.annotation.Log;
import com.qifu.framework.aspectj.lang.enums.BusinessType;
import com.qifu.framework.web.controller.BaseController;
import com.qifu.framework.web.domain.AjaxResult;
import com.qifu.framework.web.page.TableDataInfo;
import com.qifu.project.customer.domain.TContract;
import com.qifu.project.customer.domain.TCustomer;
import com.qifu.project.customer.domain.vo.TCustomerVo;
import com.qifu.project.customer.service.ITContractService;
import com.qifu.project.customer.service.ITCustomerService;
import com.qifu.project.park.domain.ParkAddress;
import com.qifu.project.park.domain.vo.ParkAddressVo;
import com.qifu.project.park.service.IParkAddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 园区管理-园区地址Controller
 */
@Api(tags = "园区管理-园区地址")
@AllArgsConstructor
@RestController
@RequestMapping("/park/address")
public class ParkAddressController extends BaseController {

    private final IParkAddressService parkAddressService;

    @Autowired
    private ITContractService contractService;

    @Autowired
    private ITCustomerService customerService;

    /**
     * 查询园区地址列表
     */
    @ApiOperation("查询园区地址列表")
    @GetMapping("/list")
    public TableDataInfo list(ParkAddress info) {
        startPage();
        LambdaQueryWrapper<ParkAddress> lqw = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(info.getAddress())){
            lqw.like(ParkAddress::getAddress ,info.getAddress());
        }
        if (info.getAddressStatus() == 0) info.setAddressStatus(null);
        if (info.getAddressStatus() != null){
            lqw.eq(ParkAddress::getAddressStatus ,info.getAddressStatus());
        }
        if (StringUtils.isNotBlank(info.getRegionname())){
            lqw.like(ParkAddress::getRegionname ,info.getRegionname());
        }
        lqw.eq(ParkAddress::getDeptId, SecurityUtils.getDeptId());
        List<ParkAddress> list = parkAddressService.list(lqw);
        List<ParkAddressVo> parkAddressVos = BeanConverUtil.converList(list, ParkAddressVo.class);
        if (parkAddressVos != null && parkAddressVos.size() > 0) {
            for (ParkAddressVo parkAddressVo : parkAddressVos) {
                // 如果地址为再用，查询合同和公司信息
                if (parkAddressVo.getAddressStatus() == 2) {
                    Long contractId = parkAddressService.getContractId(parkAddressVo.getId());
                    if (contractId != null) {
                        TContract contract = contractService.getById(contractId);
                        if (contract != null) {
                            parkAddressVo.setContractId(contractId);
                            parkAddressVo.setContractNo(contract.getContractNo());
                            parkAddressVo.setCustomerId(contract.getCustomerId());
                            TCustomer customer = customerService.getById(contract.getCustomerId());
                            if (customer != null) {
                                parkAddressVo.setCustomerName(customer.getCustomerName());
                                parkAddressVo.setAccountingStatus(customer.getAccountingStatus());
                                parkAddressVo.setAnnualReports(1);
                                parkAddressVo.setRepresentative(customer.getRepresentative());

                            }
                            TCustomerVo tCustomerVo = customerService.customerById(contract.getCustomerId());
                            if (tCustomerVo != null) {
                                if (StringUtils.isNotBlank(tCustomerVo.getManagerAccounting())) {
                                    parkAddressVo.setBelongingSalesperson(tCustomerVo.getManagerAccounting());
                                } else if (StringUtils.isNotBlank(tCustomerVo.getBookkeepingAccounting())) {
                                    parkAddressVo.setBelongingSalesperson(tCustomerVo.getBookkeepingAccounting());
                                } else {
                                    parkAddressVo.setBelongingSalesperson(tCustomerVo.getAssistantAccounting());
                                }
                                parkAddressVo.setBankAcount(tCustomerVo.getBankAcount());
                            }
                            parkAddressVo.setContractStartDate(contract.getContractStartDate());
                            parkAddressVo.setContractEndDate(contract.getContractEndDate());
                        }
                    }
                }
            }
        }
        return getDataTable(parkAddressVos);
    }

    /**
     * 获取园区地址详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        ParkAddress parkAddress = parkAddressService.getById(id);
        ParkAddressVo parkAddressVo = BeanConverUtil.conver(parkAddress, ParkAddressVo.class);
        if (parkAddressVo != null) {
            Long contractId = parkAddressService.getContractId(parkAddressVo.getId());
            if (contractId != null) {
                TContract contract = contractService.getById(contractId);
                if (contract != null) {
                    parkAddressVo.setContractId(contractId);
                    parkAddressVo.setContractNo(contract.getContractNo());
                    parkAddressVo.setCustomerId(contract.getCustomerId());
                    TCustomer customer = customerService.getById(contract.getCustomerId());
                    if (customer != null) {
                        parkAddressVo.setCustomerName(customer.getCustomerName());
                        parkAddressVo.setAccountingStatus(customer.getAccountingStatus());
                        parkAddressVo.setAnnualReports(1);
                        parkAddressVo.setRepresentative(customer.getRepresentative());

                    }
                    TCustomerVo tCustomerVo = customerService.customerById(contract.getCustomerId());
                    if (tCustomerVo != null) {
                        if (StringUtils.isNotBlank(tCustomerVo.getManagerAccounting())) {
                            parkAddressVo.setBelongingSalesperson(tCustomerVo.getManagerAccounting());
                        } else if (StringUtils.isNotBlank(tCustomerVo.getBookkeepingAccounting())) {
                            parkAddressVo.setBelongingSalesperson(tCustomerVo.getBookkeepingAccounting());
                        } else {
                            parkAddressVo.setBelongingSalesperson(tCustomerVo.getAssistantAccounting());
                        }
                        parkAddressVo.setBankAcount(tCustomerVo.getBankAcount());
                    }
                    parkAddressVo.setContractStartDate(contract.getContractStartDate());
                    parkAddressVo.setContractEndDate(contract.getContractEndDate());
                }
            }
        }
        return AjaxResult.success(parkAddressVo);
    }

    /**
     * 新增园区地址
     */
    @PreAuthorize("@ss.hasPermi('park:address:add')" )
    @Log(title = "新增园区地址", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ParkAddress info) {
        info.setCreateBy(SecurityUtils.getUsername());
        info.setCreateTime(new Date());
        info.setDeptId(SecurityUtils.companyId());
        return toAjax(parkAddressService.save(info) ? 1 : 0);
    }

    /**
     * 修改园区地址
     */
    @PreAuthorize("@ss.hasPermi('park:address:edit')" )
    @Log(title = "修改园区地址", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ParkAddress info) {
        info.setUpdateBy(SecurityUtils.getUsername());
        info.setUpdateTime(new Date());
        return toAjax(parkAddressService.updateById(info) ? 1 : 0);
    }

    /**
     * 删除园区地址
     */
    @PreAuthorize("@ss.hasPermi('park:address:remove')" )
    @Log(title = "删除园区地址", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(parkAddressService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }

    /**
     * 导出园区地址
     */
    @PreAuthorize("@ss.hasPermi('park:address:export')" )
    @Log(title = "导出园区地址", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(ParkAddress info) {
        LambdaQueryWrapper<ParkAddress> lqw = new LambdaQueryWrapper<ParkAddress>(info);
        List<ParkAddress> list = parkAddressService.list(lqw);
        ExcelUtil<ParkAddress> util = new ExcelUtil<ParkAddress>(ParkAddress.class);
        return util.exportExcel(list, "item");
    }

    /**
     * 查询未关联地址的合同列表
     */
    @ApiOperation("查询未关联地址的合同列表")
    @GetMapping("/getContractList")
    public AjaxResult getContractList() {
        List<TContract> list = parkAddressService.getContractList();
        return AjaxResult.success(list);
    }

    /**
     * 使用/取消使用地址
     */
    @ApiOperation("使用/取消使用地址")
    @GetMapping("/doParkContract")
    public AjaxResult doParkContract(Long id, Long contractId) {
        // 修改园区地址使用状态
        ParkAddress parkAddress = new ParkAddress();
        parkAddress.setId(id);
        parkAddress.setUpdateBy(SecurityUtils.getUsername());
        parkAddress.setUpdateTime(new Date());

        if (contractId != null) {
            // 添加关联表
            parkAddressService.doParkContract(id, contractId);
            parkAddress.setAddressStatus(2);
        } else {
            // 删除关联表
            parkAddressService.deleteParkContract(id);
            parkAddress.setAddressStatus(1);
        }
        parkAddressService.updateById(parkAddress);
        return AjaxResult.success();
    }

}
