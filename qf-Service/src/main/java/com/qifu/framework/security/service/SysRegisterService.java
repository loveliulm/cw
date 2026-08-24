package com.qifu.framework.security.service;

import com.qifu.common.constant.Constants;
import com.qifu.common.constant.UserConstants;
import com.qifu.common.exception.UtilException;
import com.qifu.common.exception.user.CaptchaException;
import com.qifu.common.exception.user.CaptchaExpireException;
import com.qifu.common.utils.*;
import com.qifu.framework.manager.AsyncManager;
import com.qifu.framework.manager.factory.AsyncFactory;
import com.qifu.framework.redis.RedisCache;
import com.qifu.project.system.domain.SysDept;
import com.qifu.project.system.domain.SysRole;
import com.qifu.project.system.domain.SysUser;
import com.qifu.project.system.mapper.SysDeptMapper;
import com.qifu.project.system.mapper.SysRoleMapper;
import com.qifu.project.system.mapper.SysUserMapper;
import com.qifu.project.system.service.ISysRoleService;
import com.qifu.project.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Component
public class SysRegisterService
{
    @Resource
    private ISysUserService userService;
    @Resource
    private RedisCache redisCache;
    @Resource
    private SysUserMapper userMapper;

    @Autowired
    private SysDeptMapper sysDeptMapper;

    @Autowired
    private ISysRoleService sysRoleService;

    /**
     * 注册验证
     *
     * @param username 用户名
     * @param password 密码
     * @param phonenumber 手机号
     * @param deptname
     * @return 结果
     */
    public void register(String username, String password, String phonenumber, String deptname)
    {
        //用户名判断
        if (UserConstants.NOT_UNIQUE.equals(userService.checkUserNameUnique(username)))
        {
            throw new UtilException("该用户名已存在");
        }

        //手机号
        SysUser info = userMapper.checkPhoneUnique(phonenumber);
        if (StringUtils.isNotNull(info))
        {
            throw new UtilException("注册失败，手机号码已存在");
        }

        SysDept sysDept = sysDeptMapper.checkDeptNameUnique(deptname, 0L);
        if (sysDept != null) {
            throw new UtilException("注册公司已存在，请更改公司名称！");
        }

        //新增数据
        SysUser user = new SysUser();
        user.setUserName(username);
        user.setNickName(username);
        user.setPhonenumber(phonenumber);
        user.setCreateBy(username);
        user.setPassword(SecurityUtils.encryptPassword(password));

        // 部门赋值
        sysDept = new SysDept();
        sysDept.setDeptName(deptname);
        sysDept.setParentId(0L);
        sysDept.setAncestors("0");
        sysDept.setAccountBookNum(20);
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, 1);
        sysDept.setBeginDate(date);
        sysDept.setEndDate(calendar.getTime());
        sysDept.setCreateTime(date);
        sysDept.setUpdateTime(date);
        sysDept.setIsCompany(StringDateUtil.IS_COMPANY_1);

        int result = sysDeptMapper.insertDept(sysDept);
        if (result == 0) {
            throw new UtilException("注册失败，请联系管理员！");
        }
        sysDept = sysDeptMapper.checkDeptNameUnique(deptname, 0L);

        user.setDeptId(sysDept.getDeptId());
        user.setCompanyId(sysDept.getDeptId());

        SysRole role = new SysRole();
        role.setDeptId(sysDept.getDeptId());
        role.setStatus(StringDateUtil.STATUS_0);
        role.setCreateBy(username);
        sysRoleService.insertInitRoleList(role);

        // 角色赋值
        SysRole sysRole = sysRoleService.selectRoleByDeptId(sysDept.getDeptId());
        if (sysRole != null) {
            Long[] roles = {sysRole.getRoleId()};
            user.setRoleIds(roles);
        } else {
            throw new UtilException("注册失败，请联系管理员！");
        }

        int i = userService.insertUser(user);
        Asserts.isTrue(i ==1 ,"用户注册失败！");
    }
}
