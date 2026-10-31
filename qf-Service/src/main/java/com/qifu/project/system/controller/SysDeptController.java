package com.qifu.project.system.controller;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.qifu.common.utils.ConfigKey;
import com.qifu.common.utils.StringDateUtil;
import com.qifu.framework.web.page.TableDataInfo;
import com.qifu.project.system.domain.SysRole;
import com.qifu.project.system.service.ISysConfigService;
import com.qifu.project.system.service.ISysRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
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
import com.qifu.common.utils.StringUtils;
import com.qifu.framework.aspectj.lang.annotation.Log;
import com.qifu.framework.aspectj.lang.enums.BusinessType;
import com.qifu.framework.web.controller.BaseController;
import com.qifu.framework.web.domain.AjaxResult;
import com.qifu.project.system.domain.SysDept;
import com.qifu.project.system.service.ISysDeptService;

/**
 * 部门信息
 *
 *
 */
@Api(tags = "公司及部门")
@RestController
@RequestMapping("/system/dept")
public class SysDeptController extends BaseController
{
    @Autowired
    private ISysDeptService deptService;
    @Autowired
    private ISysRoleService roleService;

    /**
     * 获取部门列表
     */
    @ApiOperation("获取公司列表")
    @PreAuthorize("@ss.hasPermi('system:dept:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysDept dept)
    {
        startPage();
        List<SysDept> depts = deptService.selectDeptList(dept);
        return getDataTable(depts);
    }

    /**
     * 查询部门列表（排除节点）
     */
    @ApiOperation("查询部门列表（排除节点）")
    @PreAuthorize("@ss.hasPermi('system:dept:list')")
    @GetMapping("/list/exclude/{deptId}")
    public AjaxResult excludeChild(@PathVariable(value = "deptId", required = false) Long deptId)
    {
        List<SysDept> depts = deptService.selectDeptList(new SysDept());
        Iterator<SysDept> it = depts.iterator();
        while (it.hasNext())
        {
            SysDept d = (SysDept) it.next();
            if (d.getDeptId().intValue() == deptId
                    || ArrayUtils.contains(StringUtils.split(d.getAncestors(), ","), deptId + ""))
            {
                it.remove();
            }
        }
        return AjaxResult.success(depts);
    }

    /**
     * 根据部门(公司)编号获取详细信息
     */
    @ApiOperation("根据部门编号获取详细信息")
    @PreAuthorize("@ss.hasPermi('system:dept:query')")
    @GetMapping(value = "/{deptId}")
    public AjaxResult getInfo(@PathVariable Long deptId)
    {
        return AjaxResult.success(deptService.selectDeptById(deptId));
    }

    /**
     * 获取部门下拉树列表
     */
    @ApiOperation("获取部门下拉树列表")
    @GetMapping("/treeselect")
    public AjaxResult treeselect(SysDept dept)
    {
        List<SysDept> depts = new ArrayList<>();
        if(!SecurityUtils.isAdmin()){
            dept.setDeptId(SecurityUtils.companyId());
            depts = deptService.selectDeptListNotAdmin(dept);
        }else {
            depts = deptService.selectDeptList(dept);
        }
        return AjaxResult.success(deptService.buildDeptTreeSelect(depts));
    }

    /**
     * 加载对应角色部门列表树
     */
    @ApiOperation("加载对应角色部门列表树")
    @GetMapping(value = "/roleDeptTreeselect/{roleId}")
    public AjaxResult roleDeptTreeselect(@PathVariable("roleId") Long roleId)
    {
        List<SysDept> depts = deptService.selectDeptList(new SysDept());
        AjaxResult ajax = AjaxResult.success();
        ajax.put("checkedKeys", deptService.selectDeptListByRoleId(roleId));
        ajax.put("depts", deptService.buildDeptTreeSelect(depts));
        return ajax;
    }

    /**
     * 新增公司
     */
    @ApiOperation("新增公司")
    @PreAuthorize("@ss.hasPermi('system:dept:add')")
    @Log(title = "部门管理", businessType = BusinessType.INSERT)
    @PostMapping
    @Transactional
    public AjaxResult add(SysDept dept)
    {
        if (UserConstants.NOT_UNIQUE.equals(deptService.checkDeptNameUnique(dept)))
        {
            return AjaxResult.error("新增部门'" + dept.getDeptName() + "'失败，公司名称已存在");
        }
        dept.setCreateBy(SecurityUtils.getUsername());
        dept.setIsCompany(StringDateUtil.IS_COMPANY_1);
        int i = deptService.insertDept(dept);
        if(SecurityUtils.isAdmin()){
            SysRole role = new SysRole();
            role.setDeptId(dept.getDeptId());
            role.setStatus(StringDateUtil.STATUS_0);
            role.setCreateBy(SecurityUtils.getUsername());
            roleService.insertInitRoleList(role);
        }
        return toAjax(i);
    }

    /**
     * 修改公司
     */
    @ApiOperation("修改公司")
    @PreAuthorize("@ss.hasPermi('system:dept:edit')")
    @Log(title = "部门管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysDept dept)
    {
        if (UserConstants.NOT_UNIQUE.equals(deptService.checkDeptNameUnique(dept)))
        {
            return AjaxResult.error("修改公司'" + dept.getDeptName() + "'失败，公司名称已存在");
        }
        else if (dept.getParentId().equals(dept.getDeptId()))
        {
            return AjaxResult.error("修改公司'" + dept.getDeptName() + "'失败，上级不能是自己");
        }
        else if (StringUtils.equals(UserConstants.DEPT_DISABLE, dept.getStatus())
                && deptService.selectNormalChildrenDeptById(dept.getDeptId()) > 0)
        {
            return AjaxResult.error("该部门包含未停用的子部门！");
        }
        dept.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(deptService.updateDept(dept));
    }

    /**
     * 删除公司
     */
    @ApiOperation("删除公司")
    @PreAuthorize("@ss.hasPermi('system:dept:remove')")
    @Log(title = "部门管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{deptId}")
    public AjaxResult remove(@PathVariable Long deptId)
    {
        if (deptService.hasChildByDeptId(deptId))
        {
            return AjaxResult.error("存在下级部门,不允许删除");
        }
        if (deptService.checkDeptExistUser(deptId))
        {
            return AjaxResult.error("公司存在用户,不允许删除");
        }
        return toAjax(deptService.deleteDeptById(deptId));
    }
}
