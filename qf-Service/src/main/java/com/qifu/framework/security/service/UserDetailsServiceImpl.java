package com.qifu.framework.security.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.qifu.common.enums.UserStatus;
import com.qifu.common.exception.BaseException;
import com.qifu.common.utils.StringUtils;
import com.qifu.framework.security.LoginUser;
import com.qifu.project.system.domain.SysUser;
import com.qifu.project.system.service.ISysUserService;

/**
 * 用户验证处理
 *
 *
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService
{
    private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Autowired
    private ISysUserService userService;

    @Autowired
    private SysPermissionService permissionService;

    @Override
    public UserDetails loadUserByUsername(String phonenumber) throws UsernameNotFoundException
    {
        SysUser user = userService.selectUserByPhonenumber(phonenumber);
        if (StringUtils.isNull(user))
        {
            log.info("登录用户：{} 不存在.", phonenumber);
            throw new UsernameNotFoundException("登录用户：" + phonenumber + " 不存在");
        }
        else if (UserStatus.DELETED.getCode().equals(user.getDelFlag()))
        {
            log.info("登录用户：{} 已被删除.", phonenumber);
            throw new BaseException("对不起，您的账号：" + phonenumber + " 已被删除");
        }
        else if (UserStatus.DISABLE.getCode().equals(user.getStatus()))
        {
            log.info("登录用户：{} 已被停用.", phonenumber);
            throw new BaseException("对不起，您的账号：" + phonenumber + " 已停用");
        }

        return createLoginUser(user);
    }

    public UserDetails createLoginUser(SysUser user)
    {
        return new LoginUser(user, permissionService.getMenuPermission(user));
    }
}
