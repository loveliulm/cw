package com.qifu.project.customer.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import java.util.Date;
import java.util.List;
import java.util.Arrays;

import com.qifu.common.utils.Asserts;
import com.qifu.common.utils.SecurityUtils;
import com.qifu.common.utils.StringDateUtil;
import com.qifu.common.utils.StringUtils;
import com.qifu.project.customer.domain.TCustomer;
import com.qifu.project.customer.domain.vo.ReportData;
import com.qifu.project.customer.service.impl.TCustomerServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
import com.qifu.project.customer.domain.Declaration;
import com.qifu.project.customer.service.IDeclarationService;
import com.qifu.framework.web.controller.BaseController;
import com.qifu.framework.web.domain.AjaxResult;
import com.qifu.common.utils.poi.ExcelUtil;
import com.qifu.framework.web.page.TableDataInfo;

/**
 * 报税Controller
 *
 *  YM
 * 09-08
 */
@Api(tags = "报税")
@AllArgsConstructor
@RestController
@RequestMapping("/customer/declaration" )
public class DeclarationController extends BaseController {

    private final IDeclarationService iDeclarationService;
    @Autowired
    private TCustomerServiceImpl customerService;

    /**
     * 查询报税列表
     */
    @PreAuthorize("@ss.hasPermi('customer:declaration:list')")
    @GetMapping("/list")
    public TableDataInfo list(Declaration declaration)
    {
        startPage();
        LambdaQueryWrapper<Declaration> lqw = new LambdaQueryWrapper<Declaration>();
        if (declaration.getCustomerId() != null){
            lqw.eq(Declaration::getCustomerId ,declaration.getCustomerId());
        }
        if (declaration.getTaxVatChange() != null){
            lqw.eq(Declaration::getTaxVatChange ,declaration.getTaxVatChange());
        }
        if (declaration.getTaxBuilding() != null){
            lqw.eq(Declaration::getTaxBuilding ,declaration.getTaxBuilding());
        }
        if (declaration.getTaxEducation() != null){
            lqw.eq(Declaration::getTaxEducation ,declaration.getTaxEducation());
        }
        if (declaration.getTaxCorporate() != null){
            lqw.eq(Declaration::getTaxCorporate ,declaration.getTaxCorporate());
        }
        if (declaration.getTaxPersonal() != null){
            lqw.eq(Declaration::getTaxPersonal ,declaration.getTaxPersonal());
        }
        if (declaration.getTaxStamp() != null){
            lqw.eq(Declaration::getTaxStamp ,declaration.getTaxStamp());
        }
        if (declaration.getTaxOther() != null){
            lqw.eq(Declaration::getTaxOther ,declaration.getTaxOther());
        }
        if (declaration.getTotal() != null){
            lqw.eq(Declaration::getTotal ,declaration.getTotal());
        }
        if (declaration.getYear() != null){
            lqw.eq(Declaration::getYear ,declaration.getYear());
        }
        if (declaration.getMonth() != null){
            lqw.eq(Declaration::getMonth ,declaration.getMonth());
        }
        List<Declaration> list = iDeclarationService.list(lqw);
        return getDataTable(list);
    }

    /**
     * 导出报税列表
     */
    @PreAuthorize("@ss.hasPermi('customer:declaration:export')" )
    @Log(title = "报税" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(Declaration declaration) {
        LambdaQueryWrapper<Declaration> lqw = new LambdaQueryWrapper<Declaration>(declaration);
        List<Declaration> list = iDeclarationService.list(lqw);
        ExcelUtil<Declaration> util = new ExcelUtil<Declaration>(Declaration. class);
        return util.exportExcel(list, "declaration" );
    }

    /**
     * 获取报税详细信息
     */
    @PreAuthorize("@ss.hasPermi('customer:declaration:query')" )
    @GetMapping(value = "/{id}" )
    public AjaxResult getInfo(@PathVariable("id" ) Long id) {
        return AjaxResult.success(iDeclarationService.getById(id));
    }

    /**
     * 新增报税
     */
    @ApiOperation("新增报税")
    @PreAuthorize("@ss.hasPermi('customer:declaration:add')" )
    @Log(title = "报税" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Declaration declaration) {
        //判断客户是否删除
        boolean b = checkCustomerIsDel(declaration.getCustomerId());
        Asserts.isTrue(b,"该客户不存在或已删除，不能报税！");

        LambdaQueryWrapper<Declaration> lqw = new LambdaQueryWrapper<Declaration>();
        lqw.eq(Declaration::getCustomerId,declaration.getCustomerId());
        lqw.eq(Declaration::getMonth,declaration.getMonth());
        lqw.eq(Declaration::getYear,declaration.getYear());
        Declaration de = iDeclarationService.getOne(lqw);
        Asserts.notNull(de,"该月已报过税！");
        Asserts.isFalse(StringUtils.isNull(declaration.getCustomerId()),"请选择报税客户!");
        Asserts.isFalse(StringUtils.isNull(declaration.getYear()),"请选择报税年份!");
        Asserts.isFalse(StringUtils.isNull(declaration.getMonth()),"请选择报税月份!");

        declaration.setCreateBy(SecurityUtils.userId());
        declaration.setCreateTime(new Date());
        declaration.setDeptId(SecurityUtils.companyId());
        declaration.setIsDel(StringDateUtil.Del_FLAG_0);
        return toAjax(iDeclarationService.save(declaration) ? 1 : 0);
    }

    /**
     * 判断客户是否删除
     * @param customerId
     * @return
     */
    public boolean checkCustomerIsDel(long customerId){
        LambdaQueryWrapper<TCustomer> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(TCustomer::getId,customerId);
        lambdaQueryWrapper.eq(TCustomer::getDelFlag,StringDateUtil.Del_FLAG_0);
        TCustomer tCustomer = customerService.getOne(lambdaQueryWrapper);
        if(tCustomer == null){
            return false;
        }
        return true;
    }

    /**
     * 修改报税
     */
    @ApiOperation("修改报税")
    @PreAuthorize("@ss.hasPermi('customer:declaration:edit')" )
    @Log(title = "报税" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Declaration declaration) {
        //判断客户是否删除
        boolean b = checkCustomerIsDel(declaration.getCustomerId());
        Asserts.isTrue(b,"该客户不存在或已删除，不能报税！");

        Asserts.isFalse(StringUtils.isNull(declaration.getCustomerId()),"请选择报税客户!");
        Asserts.isFalse(StringUtils.isNull(declaration.getYear()),"请选择报税年份!");
        Asserts.isFalse(StringUtils.isNull(declaration.getMonth()),"请选择报税月份!");

        declaration.setUpdateBy(SecurityUtils.userId());
        declaration.setUpdateTime(new Date());
        return toAjax(iDeclarationService.updateById(declaration) ? 1 : 0);
    }

    /**
     * 删除报税
     */
    @PreAuthorize("@ss.hasPermi('customer:declaration:remove')" )
    @Log(title = "报税" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}" )
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(iDeclarationService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }

    @ApiOperation("获取客户一年的每月是否报税")
    @GetMapping("/getTaxByYear")
    @PreAuthorize("@ss.hasPermi('customer:declaration:list')")
    public AjaxResult getTaxByYear(Declaration declaration){
        Asserts.isTrue(StringUtils.isNotNull(declaration.getCustomerId()),"客户id为空");
        Asserts.isTrue(StringUtils.isNotNull(declaration.getYear()),"请选择年");
        ReportData reportData = iDeclarationService.getTaxByYear(declaration);
        if (reportData == null) {
            reportData = new ReportData(0,0,0,0,0,
                    0,0,0,0,0,0,0);
        }
        return AjaxResult.success(reportData);
    }

    @ApiOperation("获取报税详情")
    @GetMapping("/getDetail")
    @PreAuthorize("@ss.hasPermi('customer:declaration:query')" )
    public AjaxResult getDetail(Declaration declaration){
        Asserts.isTrue(StringUtils.isNotNull(declaration.getCustomerId()),"客户id为空");
        Asserts.isTrue(StringUtils.isNotNull(declaration.getYear()),"请选择年");
        Asserts.isTrue(StringUtils.isNotNull(declaration.getMonth()),"请选择月");
        LambdaQueryWrapper<Declaration> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Declaration::getCustomerId,declaration.getCustomerId());
        lqw.eq(Declaration::getMonth,declaration.getMonth());
        lqw.eq(Declaration::getYear,declaration.getYear());
        lqw.eq(Declaration::getIsDel,StringDateUtil.Del_FLAG_0);

        return  AjaxResult.success(iDeclarationService.getOne(lqw));
    }
}
