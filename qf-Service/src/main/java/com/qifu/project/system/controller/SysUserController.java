package com.qifu.project.system.controller;

import java.util.ArrayList;
import java.util.List;

import com.qifu.common.utils.*;
import com.qifu.project.customer.domain.TCustomer;
import com.qifu.project.system.domain.SysDept;
import com.qifu.project.system.domain.SysRole;
import com.qifu.project.system.mapper.SysDeptMapper;
import com.qifu.project.system.mapper.SysRoleDeptMapper;
import com.qifu.project.system.mapper.SysRoleMapper;
import com.qifu.project.system.mapper.SysUserMapper;
import com.qifu.project.system.service.ISysDeptService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
import org.springframework.web.multipart.MultipartFile;
import com.qifu.common.constant.UserConstants;
import com.qifu.common.utils.poi.ExcelUtil;
import com.qifu.framework.aspectj.lang.annotation.Log;
import com.qifu.framework.aspectj.lang.enums.BusinessType;
import com.qifu.framework.security.LoginUser;
import com.qifu.framework.security.service.TokenService;
import com.qifu.framework.web.controller.BaseController;
import com.qifu.framework.web.domain.AjaxResult;
import com.qifu.framework.web.page.TableDataInfo;
import com.qifu.project.system.domain.SysUser;
import com.qifu.project.system.service.ISysPostService;
import com.qifu.project.system.service.ISysRoleService;
import com.qifu.project.system.service.ISysUserService;

import javax.management.relation.Role;

/**
 * 用户信息
 *
 *
 */
@Api(tags = "用户管理")
@RestController
@RequestMapping("/system/user")
public class SysUserController extends BaseController
{
    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private ISysPostService postService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private ISysDeptService deptService;

    @Autowired
    private SysDeptMapper deptMapper;

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private SysRoleDeptMapper roleDeptMapper;

    /**
     * 新增部门
     */
    @ApiOperation("新增部门")
    @PreAuthorize("@ss.hasPermi('system:user:dept:add')")
    @Log(title = "部门管理", businessType = BusinessType.INSERT)
    @PostMapping("/add/dept")
    @Transactional
    public AjaxResult add(SysDept dept)
    {
        if (UserConstants.NOT_UNIQUE.equals(deptService.checkDeptNameUnique(dept)))
        {
            return AjaxResult.error("新增部门'" + dept.getDeptName() + "'失败，部门名称已存在");
        }
        SysDept pathDept = deptMapper.selectDeptById(dept.getParentId());
//        Asserts.isTrue((StringDateUtil.IS_COMPANY_1).equals(pathDept.getIsCompany()),"不能添加三级部门，只支持二级部门");

        dept.setStatus(StringDateUtil.STATUS_0);
        dept.setIsCompany(StringDateUtil.IS_COMPANY_2);
        dept.setCreateBy(SecurityUtils.getUsername());
        dept.setDeptStatus(StringDateUtil.STATUS_0);
        int i = deptService.insertDept(dept);
        Asserts.isTrue(i == 1,"新增部门失败！");

        return AjaxResult.success();
    }

    /**
     * 修改部门
     */
    @ApiOperation("修改部门")
    @PreAuthorize("@ss.hasPermi('system:user:dept:edit')")
    @Log(title = "部门管理", businessType = BusinessType.UPDATE)
    @PutMapping("dept/edit")
    public AjaxResult edit(@RequestBody SysDept dept)
    {
        if (UserConstants.NOT_UNIQUE.equals(deptService.checkDeptNameUnique(dept)))
        {
            return AjaxResult.error("修改部门'" + dept.getDeptName() + "'失败，部门名称已存在");
        }
        else if (dept.getParentId().equals(dept.getDeptId()))
        {
            return AjaxResult.error("修改部门'" + dept.getDeptName() + "'失败，上级部门不能是自己");
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
     * 删除部门
     */
    @ApiOperation("3.删除部门")
    @PreAuthorize("@ss.hasPermi('system:user:dept:remove')")
    @Log(title = "部门管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/dept/{deptId}")
    public AjaxResult remove(@PathVariable Long deptId)
    {
        if (deptService.hasChildByDeptId(deptId))
        {
            return AjaxResult.error("存在下级部门,不允许删除");
        }
        if (deptService.checkDeptExistUser(deptId))
        {
            return AjaxResult.error("部门存在用户,不允许删除");
        }
        return toAjax(deptService.deleteDeptById(deptId));
    }

    /**
     * 获取用户列表
     */
    @ApiOperation("获取用户列表")
//    @PreAuthorize("@ss.hasPermi('system:user:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysUser user) {
        if(user.getDeptId() != null && SecurityUtils.companyId() == user.getDeptId()){
            user.setDeptId(null);
        }
        startPage();
        List<SysUser> list;
        if(SecurityUtils.isAdmin()){
            list = userService.selectUserList(user);
        }else{
            user.setCompanyId(SecurityUtils.companyId());
            list = userService.selectUserListNotAdmin(user);
        }

        return getDataTable(list);
    }

    @ApiOperation("导出")
    @Log(title = "用户管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('system:user:export')")
    @GetMapping("/export")
    public AjaxResult export(SysUser user)
    {
        List<SysUser> list;
        if(SecurityUtils.isAdmin()){
            list = userService.selectUserList(user);
        }else{
            user.setCompanyId(SecurityUtils.companyId());
            list = userService.selectUserListNotAdmin(user);
        }
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        return util.exportExcel(list, "用户数据");
    }

    /**
     * 导入
     * @param file
     * @param updateSupport
     * @return
     * @throws Exception
     */

    @Log(title = "用户管理", businessType = BusinessType.IMPORT)
    @PreAuthorize("@ss.hasPermi('system:user:import')")
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception
    {
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        List<SysUser> userList = util.importExcel(file.getInputStream());
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        String operName = loginUser.getUsername();
        String message = userService.importUser(userList, updateSupport, operName);
        return AjaxResult.success(message);
    }

    @GetMapping("/importTemplate")
    public AjaxResult importTemplate()
    {
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        return util.importTemplateExcel("用户数据");
    }

    /**
     * 根据用户编号获取详细信息
     */
    @ApiOperation("根据用户编号获取详细信息")
    @PreAuthorize("@ss.hasPermi('system:user:query')")
    @GetMapping(value = { "/", "/{userId}" })
    public AjaxResult getInfo(@PathVariable(value = "userId", required = false) Long userId)
    {
        AjaxResult ajax = AjaxResult.success();
//        ajax.put("roles", roleService.selectRoleAll());
        ajax.put("posts", postService.selectPostAll());
        if (StringUtils.isNotNull(userId))
        {
            ajax.put("roles", roleService.listByDeptId(userMapper.selectUserById(userId).getCompanyId()));
            ajax.put(AjaxResult.DATA_TAG, userService.selectUserById(userId));
            ajax.put("postIds", postService.selectPostListByUserId(userId));
            ajax.put("roleIds", roleService.selectRoleListByUserId(userId));
        }else {
            ajax.put("roles", roleService.selectRoleAll());
        }
        return ajax;
    }

    /**
     * 新增用户
     */
    @ApiOperation("新增用户")
    @PreAuthorize("@ss.hasPermi('system:user:add')")
    @Log(title = "用户管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysUser user)
    {
        if (UserConstants.NOT_UNIQUE.equals(userService.checkUserNameUnique(user.getUserName())))
        {
            return AjaxResult.error("新增用户'" + user.getUserName() + "'失败，用户名称已存在");
        }
        else if (UserConstants.NOT_UNIQUE.equals(userService.checkPhoneUnique(user)))
        {
            return AjaxResult.error("新增用户'" + user.getUserName() + "'失败，手机号码已存在");
        }
        else if (UserConstants.NOT_UNIQUE.equals(userService.checkEmailUnique(user)))
        {
            return AjaxResult.error("新增用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        }
        Asserts.isTrue(StringUtils.isNotNull(user.getPassword()),"登录密码不能为空");
        user.setCreateBy(SecurityUtils.getUsername());
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        user.setCompanyId(SecurityUtils.getDeptId());

        return toAjax(userService.insertUser(user));
    }

    /**
     * 修改用户
     */
    @ApiOperation("修改用户")
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysUser user)
    {
        userService.checkUserAllowed(user);

//        Asserts.isFalse(SecurityUtils.userId() == user.getUserId(),"当前用户不能操作自己");

        if (UserConstants.NOT_UNIQUE.equals(userService.checkPhoneUnique(user)))
        {
            return AjaxResult.error("修改用户'" + user.getUserName() + "'失败，手机号码已存在");
        }
        else if (UserConstants.NOT_UNIQUE.equals(userService.checkEmailUnique(user)))
        {
            return AjaxResult.error("修改用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        }
        user.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(userService.updateUser(user));
    }

    /**
     * 删除用户
     */
    @ApiOperation("删除用户")
    @PreAuthorize("@ss.hasPermi('system:user:remove')")
    @Log(title = "用户管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{userIds}")
    public AjaxResult remove(@PathVariable Long[] userIds)
    {
        return toAjax(userService.deleteUserByIds(userIds));
    }

    /**
     * 重置密码
     */
    @ApiOperation("重置密码")
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping("/resetPwd")
    public AjaxResult resetPwd(@RequestBody SysUser user)
    {
        userService.checkUserAllowed(user);
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        user.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(userService.resetPwd(user));
    }

    /**
     * 状态修改
     */
    @ApiOperation("状态修改")
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody SysUser user)
    {
        userService.checkUserAllowed(user);
        if (StringDateUtil.STATUS_1.equals(user.getStatus())){
            List<TCustomer> list = userService.checkAssignUser(user);
            if (list.size() > 0){
                String message = "用户已分配给";
                for (int i = 0; i < list.size(); i++) {
                    TCustomer tcustomer = list.get(i);
                    if (i == 0){
                        message += "" + tcustomer.getCustomerName() +"";
                    }else{
                        message += "，" + tcustomer.getCustomerName() +"";
                    }
                }
                message += "。请重新派工再进行停用";
                return AjaxResult.error(message);
            }
        }
        user.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(userService.updateUserStatus(user));
    }


    @GetMapping("/list/dept")
    public AjaxResult userListByDeptId(){
        return AjaxResult.success(userMapper.selectUserListByCompanyId(SecurityUtils.companyId()));
    }
}
