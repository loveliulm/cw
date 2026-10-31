package com.qifu.project.customer.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import java.util.*;

import com.qifu.accounting.common.factory.InitAccountFactory;
import com.qifu.accounting.sys.domain.BookPeriod;
import com.qifu.accounting.sys.service.IBookPeriodService;
import com.qifu.accounting.sys.service.IBookSubjectService;
import com.qifu.common.utils.Asserts;
import com.qifu.common.utils.SecurityUtils;
import com.qifu.common.utils.StringDateUtil;
import com.qifu.common.utils.StringUtils;
import com.qifu.project.commerce.domain.vo.StatisticalData;
import com.qifu.project.common.enums.ServerStatusEnum;
import com.qifu.project.customer.domain.TCustomer;
import com.qifu.project.customer.domain.vo.BookAccountTemplateVo;
import com.qifu.project.customer.domain.vo.TAccountBookVo;
import com.qifu.project.customer.mapper.TCustomerMapper;
import com.qifu.project.customer.service.ITCustomerService;
import com.qifu.project.system.domain.SysRole;
import com.qifu.project.system.service.ISysRoleService;
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
import com.qifu.project.customer.domain.TAccountBook;
import com.qifu.project.customer.service.ITAccountBookService;
import com.qifu.framework.web.controller.BaseController;
import com.qifu.framework.web.domain.AjaxResult;
import com.qifu.common.utils.poi.ExcelUtil;
import com.qifu.framework.web.page.TableDataInfo;

/**
 * 账套Controller
 *
 *  yangyang
 * 06-30
 */
@AllArgsConstructor
@RestController
@RequestMapping("/customer/book" )
public class TAccountBookController extends BaseController {

    private final ITAccountBookService iTAccountBookService;

    @Autowired
    private ISysRoleService iSysRoleService;

    @Autowired
    private InitAccountFactory initAccountFactory;

    private final ITCustomerService customerService;

    /**
     * 查询账套列表
     */
    @PreAuthorize("@ss.hasPermi('customer:book:list')")
    @GetMapping("/list")
    public TableDataInfo list(TAccountBookVo tAccountBookVo)
    {
        tAccountBookVo.setDeptId(SecurityUtils.companyId());
        //登录用户id
        Long userId = SecurityUtils.userId();
        // 获取登录用户角色集合
        List<SysRole> roles = iSysRoleService.selectRoleByUserId(userId);
        if (!SecurityUtils.isAdmin() && checkRole(roles)){
            tAccountBookVo.setBookkeepingAccountingUserId(userId);
//            tAccountBookVo.setAssistantAccountingUserId(userId);
        }
        startPage();
        List<TAccountBookVo> list = iTAccountBookService.selectListBook(tAccountBookVo);
        return getDataTable(list);
    }

    //验证用户是否管理员角色或会计主管角色
    private boolean checkRole(List<SysRole> roles){
        for (SysRole sysRole: roles) {
            String roleKey = sysRole.getRoleKey();
            if (StringDateUtil.COMPANYADMIN.equals(roleKey) || StringDateUtil.ACCOUNTMANGER.equals(roleKey)){
                return false;
            }
        }
        return true;
    }

    /**
     * 导出账套列表
     */
    @PreAuthorize("@ss.hasPermi('customer:book:export')" )
    @Log(title = "账套" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(TAccountBookVo tAccountBookVo) {
        List<TAccountBookVo> list = iTAccountBookService.selectListBook(tAccountBookVo);
        tAccountBookVo.setDeptId(SecurityUtils.companyId());
        ExcelUtil<TAccountBookVo> util = new ExcelUtil<TAccountBookVo>(TAccountBookVo. class);
        return util.exportExcel(list, "book" );
    }

    /**
     * 获取账套详细信息
     */
    @PreAuthorize("@ss.hasPermi('customer:book:query')" )
    @GetMapping(value = "/{bookId}" )
    public AjaxResult getInfo(@PathVariable("bookId" ) Long bookId) {
        return AjaxResult.success(iTAccountBookService.getById(bookId));
    }

    /**
     * 修改账套
     */
    @PreAuthorize("@ss.hasPermi('customer:book:edit')" )
    @Log(title = "账套" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TAccountBook tAccountBook) {
        return toAjax(iTAccountBookService.updateById(tAccountBook) ? 1 : 0);
    }

    /**
     * 建账
     */
    @Transactional
    @Log(title = "建账" , businessType = BusinessType.UPDATE)
    @PutMapping("/createAccount")
    public AjaxResult createAccount(@RequestBody TAccountBook tAccountBook) {

        Asserts.isNull(tAccountBook.getBookId(),"账套id不能为空");
        Asserts.isNull(tAccountBook.getCreatePeriod(),"请选择账期");
        Asserts.isNull(tAccountBook.getAccountTemplateId(),"必须选择会计制度");
        Asserts.isTrue(iTAccountBookService.checkBookNumber(SecurityUtils.companyId()),"您的账套已超，不能建账");
        tAccountBook.setBookCount(iTAccountBookService.getById(tAccountBook.getBookId()).getBookCount()+1);
        tAccountBook.setCurrentPeriod(tAccountBook.getCreatePeriod());
        boolean b = iTAccountBookService.updateById(tAccountBook);

        //修改客户的记账会计
        TCustomer customer = new TCustomer();
        customer.setId(iTAccountBookService.getById(tAccountBook.getBookId()).getCustomerId());
        customer.setBookkeepingAccountingUserId(tAccountBook.getBookkeepingAccountingUserId());
        customer.setUpdateTime(new Date());
        customerService.updateById(customer);

        Asserts.isTrue(b,"建账失败");
        if(tAccountBook.getBookCount() == 1){
            initAccountFactory.initAccountBook(tAccountBook);
        }
        return AjaxResult.success();
    }

    /**
     * 获取会计制度
     */
    @GetMapping("/account/template")
    public TableDataInfo getAccountTemplate() {
        List<BookAccountTemplateVo> list = iTAccountBookService.getAccountTemplate();
        return getDataTable(list);
    }

    @ApiOperation("获取首页统计数据")
    @GetMapping("/getIndexData")
    public AjaxResult getIndexData(@ApiParam(name = "当月/全部",required = true) String type){
        Map<String,Object> map = new HashMap<String,Object>();
        if(!SecurityUtils.isAdmin()){
            map.put("deptId",SecurityUtils.getDeptId());
        }

        if(StringUtils.isEmpty(type)){
            map.put("type","month");
        }else{
            map.put("type",type);
        }
        StatisticalData statisticalData = iTAccountBookService.getIndexData(map);
        return AjaxResult.success(statisticalData);
    }

    @ApiOperation("停止服务")
    @PreAuthorize("@ss.hasPermi('customer:book:stopService')" )
    @GetMapping("/stopService/{id}")
    public AjaxResult stopService(@PathVariable Long id){
        Asserts.isNull(id,"请选择要停止的服务");
        Asserts.isTrue(iTAccountBookService.checkServiceStatus(id),"所选服务已停止");
        TAccountBook tAccountBook = new TAccountBook();
        tAccountBook.setBookId(id);
        tAccountBook.setServerStatus(ServerStatusEnum.停止服务);
        return toAjax(iTAccountBookService.updateById(tAccountBook) ? 1 : 0);
    }

    @ApiOperation("恢复服务")
    @PreAuthorize("@ss.hasPermi('customer:book:restoreService')" )
    @GetMapping("/restoreService/{id}")
    public AjaxResult restoreService(@PathVariable Long id){
        Asserts.isNull(id,"请选择要恢复的服务");
        Asserts.isFalse(iTAccountBookService.checkServiceStatus(id),"所选服务不是停止状态");
        TAccountBook tAccountBook = new TAccountBook();
        tAccountBook.setBookId(id);
        tAccountBook.setServerStatus(ServerStatusEnum.正常服务);
        return toAjax(iTAccountBookService.updateById(tAccountBook) ? 1 : 0);
    }
}
