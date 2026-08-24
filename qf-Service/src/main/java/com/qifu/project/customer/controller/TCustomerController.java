package com.qifu.project.customer.controller;

import com.qifu.common.constant.UserConstants;
import com.qifu.common.utils.Asserts;
import com.qifu.common.utils.MakerNum;
import com.qifu.common.utils.SecurityUtils;
import com.qifu.common.utils.poi.ExcelUtil;
import com.qifu.framework.aspectj.lang.annotation.Log;
import com.qifu.framework.aspectj.lang.enums.BusinessType;
import com.qifu.framework.web.controller.BaseController;
import com.qifu.framework.web.domain.AjaxResult;
import com.qifu.framework.web.page.TableDataInfo;
import com.qifu.project.customer.domain.TCustomer;
import com.qifu.project.customer.domain.TCustomerServiceType;
import com.qifu.project.customer.domain.vo.ReportData;
import com.qifu.project.customer.domain.vo.TCustomerVo;
import com.qifu.project.customer.service.ITCustomerService;
import com.qifu.project.customer.service.ITCustomerServiceTypeService;
import com.qifu.project.system.domain.SysNumberRule;
import com.qifu.project.system.service.ISysNumberRuleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 客户Controller
 *
 */
@Api(tags = "客户管理")
@AllArgsConstructor
@RestController
@RequestMapping("/customer/customer" )
public class TCustomerController extends BaseController {

    private final ITCustomerService iTCustomerService;

    @Autowired
    private ITCustomerServiceTypeService serviceTypeService;

    @Autowired
    private ISysNumberRuleService sysNumberRuleService;


    /**
     * 获取客户编号规则
     */
    @ApiOperation("客户编号生成规则")
    @GetMapping("/getCustomerNo")
    public synchronized String getCustomerNo(){
        String customerNo = null;
        Long deptId = SecurityUtils.getDeptId();
        //查找规则
        SysNumberRule sysNumberRules = sysNumberRuleService.findByRuleType(1);
        TCustomer customer = iTCustomerService.findFirstByDeptId(deptId);
        if (customer == null){
            customerNo = MakerNum.makerNumRule(sysNumberRules.getPrefix(), null);
        } else {
            customerNo = MakerNum.makerNumRule(sysNumberRules.getPrefix(), customer.getCustomerCode());
        }
        return customerNo;
    }

    /**
     * 查询客户列表
     */
    @ApiOperation("查询客户列表")
    @PreAuthorize("@ss.hasPermi('customer:customer:list')")
    @GetMapping("/list")
    public TableDataInfo list(TCustomer tCustomer)
    {
        startPage();
        List<TCustomerVo> list = iTCustomerService.customerList(tCustomer);
        return getDataTable(list);
    }

    /**
     * 导出客户列表
     */
    @ApiOperation("导出客户列表")
    @PreAuthorize("@ss.hasPermi('customer:customer:export')" )
    @Log(title = "客户" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(TCustomer tCustomer) {
        List<TCustomerVo> list = iTCustomerService.customerList(tCustomer);
        ExcelUtil<TCustomerVo> util = new ExcelUtil<TCustomerVo>(TCustomerVo. class);
        return util.exportExcel(list, "customer" );
    }

    @GetMapping("/importTemplate")
    public AjaxResult importTemplate()
    {
        ExcelUtil<TCustomer> util = new ExcelUtil<TCustomer>(TCustomer.class);
        return util.importTemplateExcel("客户数据");
    }

    @Log(title = "客户管理", businessType = BusinessType.IMPORT)
//    @PreAuthorize("@ss.hasPermi('customer:customer:import')")
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception{
        ExcelUtil<TCustomer> util = new ExcelUtil<TCustomer>(TCustomer.class);
        List<TCustomer> customerList = util.importExcel(file.getInputStream());
        String message = iTCustomerService.importCustomer(customerList,false,SecurityUtils.getUsername());
//        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
//        String operName = loginUser.getUsername();
//        String message = userService.importUser(userList, updateSupport, operName);
        return AjaxResult.success(message);
    }

    /**
     * 获取客户详细信息
     */
    @ApiOperation("获取客户详细信息")
    @PreAuthorize("@ss.hasPermi('customer:customer:query')" )
    @GetMapping(value = "/{id}" )
    public AjaxResult getInfo(@PathVariable("id" ) Long id) {
        return AjaxResult.success(iTCustomerService.customerById(id));
    }

    /**
     * 新增客户
     */
    @ApiOperation("新增客户")
    @PreAuthorize("@ss.hasPermi('customer:customer:add')" )
    @Log(title = "客户" , businessType = BusinessType.INSERT)
    @Transactional
    @PostMapping
    public AjaxResult add(@RequestBody TCustomerVo tCustomer) {
        if (UserConstants.NOT_UNIQUE.equals(iTCustomerService.checkCodeUnique(tCustomer.getCustomerCode()))) {
            return AjaxResult.error("添加失败,客户编码已存在，请刷新页面重新添加！");
        }
        if (UserConstants.NOT_UNIQUE.equals(iTCustomerService.checkNameUnique(tCustomer))){
            return AjaxResult.error("添加失败，客户名称已存在！");
        }
        TCustomerServiceType[] customerServiceTypes = tCustomer.getTCustomerServiceTypes();
        Asserts.isTrue(customerServiceTypes.length > 0,"请选择服务类型");
        iTCustomerService.add(tCustomer);
        return AjaxResult.success();
    }

    /**
     * 修改客户
     */
    @ApiOperation("修改客户")
    @PreAuthorize("@ss.hasPermi('customer:customer:edit')" )
    @Log(title = "客户" , businessType = BusinessType.UPDATE)
    @Transactional
    @PutMapping
    public AjaxResult edit(@RequestBody TCustomerVo tCustomer) {
        TCustomerServiceType[] customerServiceTypes = tCustomer.getTCustomerServiceTypes();
        Asserts.isTrue(customerServiceTypes.length > 0,"请选择服务类型");
        iTCustomerService.edit(tCustomer);
        return AjaxResult.success();
    }

    /**
     * 删除客户
     */
    @ApiOperation("删除客户")
    @PreAuthorize("@ss.hasPermi('customer:customer:remove')" )
    @Log(title = "客户" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}" )
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(iTCustomerService.deleteCustomerByIds(ids));
    }

    /**
     * 派工
     */
    @ApiOperation("派工")
    @PreAuthorize("@ss.hasPermi('customer:customer:dispatch')" )
    @Log(title = "派工" , businessType = BusinessType.UPDATE)
    @PutMapping("/dispatch" )
    public AjaxResult dispatch(@RequestBody TCustomer tCustomer) {
        tCustomer.setDeptId(SecurityUtils.companyId());
        iTCustomerService.dispatch(tCustomer);
        return AjaxResult.success();
    }

    /**
     * 派工-获取人员
     * @param roleKey "accountManager"：会计主管，"accountClerk"：会计助理，"accounting"：记账会计
     * @return
     */
    @ApiOperation("派工-获取人员")
    @PreAuthorize("@ss.hasPermi('customer:customer:edit')" )
    @GetMapping("/user/list" )
    public AjaxResult selectUserList(String roleKey) {
        return AjaxResult.success(iTCustomerService.selectUserList(roleKey,SecurityUtils.companyId()));
    }

    /**
     * 创建账套
     * @param customerId
     * @return
     */
    @ApiOperation("建账")
    @PreAuthorize("@ss.hasPermi('customer:customer:edit')" )
    @Log(title = "建账" , businessType = BusinessType.UPDATE)
    @PutMapping("/create/account" )
    public AjaxResult createAccount(int customerId){
        return toAjax(iTCustomerService.createAccount(customerId));
    }

    @ApiOperation("获取首页统计数据")
    @GetMapping("/getReportData")
    public AjaxResult getReportData(){
        TCustomer customer = new TCustomer();
        if(!SecurityUtils.isAdmin()){
            customer.setDeptId(SecurityUtils.getDeptId());
        }

        ReportData reportData = iTCustomerService.selectByAccount(customer);
        return AjaxResult.success(reportData !=null ? reportData.getArray() : new Integer[]{0,0,0,0,0,0,0,0,0,0,0,0,});
    }
}
