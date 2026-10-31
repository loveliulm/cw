package com.qifu.project.system.controller;

import java.util.List;

import com.qifu.common.utils.StringDateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.qifu.common.constant.UserConstants;
import com.qifu.common.utils.SecurityUtils;
import com.qifu.common.utils.poi.ExcelUtil;
import com.qifu.framework.aspectj.lang.annotation.Log;
import com.qifu.framework.aspectj.lang.enums.BusinessType;
import com.qifu.framework.web.controller.BaseController;
import com.qifu.framework.web.domain.AjaxResult;
import com.qifu.framework.web.page.TableDataInfo;
import com.qifu.project.system.domain.SysRole;
import com.qifu.project.system.service.ISysRoleService;

/**
 * 角色信息
 *
 *
 */
@RestController
@Api(tags = "角色管理")
@RequestMapping("/system/role")
public class SysRoleController extends BaseController
{
    @Autowired
    private ISysRoleService roleService;

    @ApiOperation("角色列表")
    @PreAuthorize("@ss.hasPermi('system:role:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysRole role)
    {
        startPage();
        if(!SecurityUtils.isAdmin()){
            role.setDeptId(SecurityUtils.companyId());
        }
        List<SysRole> list = roleService.selectRoleList(role);
        return getDataTable(list);
    }

    @ApiOperation("根据部门id（公司id）获取角色")
    @PreAuthorize("@ss.hasPermi('system:role:list')")
    @GetMapping("/list/deptId")
    public TableDataInfo listByDeptId(int deptId)
    {
        startPage();
        List<SysRole> list = roleService.listByDeptId(SecurityUtils.getCompanyId(new Long(deptId)));
        return getDataTable(list);
    }

    @ApiOperation("导出")
    @Log(title = "角色管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('system:role:export')")
    @GetMapping("/export")
    public AjaxResult export(SysRole role)
    {
        List<SysRole> list = roleService.selectRoleList(role);
        ExcelUtil<SysRole> util = new ExcelUtil<SysRole>(SysRole.class);
        return util.exportExcel(list, "角色数据");
    }

    /**
     * 根据角色编号获取详细信息
     */
    @ApiOperation("根据角色编号获取详细信息")
    @PreAuthorize("@ss.hasPermi('system:role:query')")
    @GetMapping(value = "/{roleId}")
    public AjaxResult getInfo(@PathVariable Long roleId)
    {
        return AjaxResult.success(roleService.selectRoleById(roleId));
    }

    /**
     * 新增角色
     */
    @ApiOperation("新增角色")
    @PreAuthorize("@ss.hasPermi('system:role:add')")
    @Log(title = "角色管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysRole role)
    {
//        if (UserConstants.NOT_UNIQUE.equals(roleService.checkRoleNameUnique(role)))
//        {
//            return AjaxResult.error("新增角色'" + role.getRoleName() + "'失败，角色名称已存在");
//        }
//        else if (UserConstants.NOT_UNIQUE.equals(roleService.checkRoleKeyUnique(role)))
//        {
//            return AjaxResult.error("新增角色'" + role.getRoleName() + "'失败，角色权限已存在");
//        }
        role.setCreateBy(SecurityUtils.getUsername());
        role.setIsDefault(StringDateUtil.IS_DEFAULT_1);
        role.setDeptId(SecurityUtils.companyId());
        return toAjax(roleService.insertRole(role));

    }

    /**
     * 修改保存角色
     */
    @ApiOperation("修改保存角色")
    @PreAuthorize("@ss.hasPermi('system:role:edit')")
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysRole role)
    {
        roleService.checkRoleAllowed(role);
//        if (UserConstants.NOT_UNIQUE.equals(roleService.checkRoleNameUnique(role)))
//        {
//            return AjaxResult.error("修改角色'" + role.getRoleName() + "'失败，角色名称已存在");
//        }
//        else if (UserConstants.NOT_UNIQUE.equals(roleService.checkRoleKeyUnique(role)))
//        {
//            return AjaxResult.error("修改角色'" + role.getRoleName() + "'失败，角色权限已存在");
//        }
        role.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(roleService.updateRole(role));
    }

    /**
     * 修改保存数据权限
     */
    @ApiOperation("修改保存数据权限")
    @PreAuthorize("@ss.hasPermi('system:role:edit')")
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @PutMapping("/dataScope")
    public AjaxResult dataScope(@RequestBody SysRole role)
    {
        roleService.checkRoleAllowed(role);
        return toAjax(roleService.authDataScope(role));
    }

    /**
     * 状态修改
     */
    @ApiOperation("状态修改")
    @PreAuthorize("@ss.hasPermi('system:role:edit')")
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody SysRole role)
    {
        roleService.checkRoleAllowed(role);
        role.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(roleService.updateRoleStatus(role));
    }

    /**
     * 删除角色
     */
    @ApiOperation("删除角色")
    @PreAuthorize("@ss.hasPermi('system:role:remove')")
    @Log(title = "角色管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{roleIds}")
    public AjaxResult remove(@PathVariable Long[] roleIds)
    {
        return toAjax(roleService.deleteRoleByIds(roleIds));
    }

    /**
     * 获取角色选择框列表
     */
    @ApiOperation("获取角色选择框列表")
    @PreAuthorize("@ss.hasPermi('system:role:query')")
    @GetMapping("/optionselect")
    public AjaxResult optionselect()
    {
        return AjaxResult.success(roleService.selectRoleAll());
    }
}
