package com.qifu.common.utils;

import com.qifu.project.system.domain.SysDept;
import com.qifu.project.system.domain.SysUser;
import com.qifu.project.system.service.ISysDeptService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.qifu.common.constant.HttpStatus;
import com.qifu.common.exception.CustomException;
import com.qifu.framework.security.LoginUser;

/**
 * 安全服务工具类
 *
 *
 */
public class SecurityUtils
{
    /**
     * 获取用户账户
     **/
    public static String getUsername()
    {
        try
        {
            return getLoginUser().getUsername();
        }
        catch (Exception e)
        {
            throw new CustomException("获取用户账户异常", HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * 获取用户
     **/
    public static LoginUser getLoginUser()
    {
        try
        {
            return (LoginUser) getAuthentication().getPrincipal();
        }
        catch (Exception e)
        {
            throw new CustomException("获取用户信息异常", HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * 获取Authentication
     */
    public static Authentication getAuthentication()
    {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 生成BCryptPasswordEncoder密码
     *
     * @param password 密码
     * @return 加密字符串
     */
    public static String encryptPassword(String password)
    {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    /**
     * 判断密码是否相同
     *
     * @param rawPassword 真实密码
     * @param encodedPassword 加密后字符
     * @return 结果
     */
    public static boolean matchesPassword(String rawPassword, String encodedPassword)
    {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    /**
     * 是否为管理员
     *
     * @param userId 用户ID
     * @return 结果
     */
    public static boolean isAdmin(Long userId)
    {
        return userId != null && 1L == userId;
    }


    public static boolean isAdmin(){
        try
        {
            LoginUser loginUser = (LoginUser) getAuthentication().getPrincipal();
            SysUser user = loginUser.getUser();
            return user.getUserId() != null && 1L == user.getUserId();
        }
        catch (Exception e)
        {
            throw new CustomException("获取用户信息异常", HttpStatus.UNAUTHORIZED);
        }
    }


    /**
     * 获取用户
     * @return
     */
    public static SysUser sysUser(){
        try
        {
            LoginUser loginUser = (LoginUser) getAuthentication().getPrincipal();
            SysUser user = loginUser.getUser();
            return user;
        }
        catch (Exception e)
        {
            throw new CustomException("获取用户信息异常", HttpStatus.UNAUTHORIZED);
        }
    }


    /**
     * 获取登录userId
     * @return
     */
    public static long userId(){
        try
        {
            LoginUser loginUser = (LoginUser) getAuthentication().getPrincipal();
            SysUser user = loginUser.getUser();
            return user.getUserId();
        }
        catch (Exception e)
        {
            throw new CustomException("获取用户信息异常", HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * 获取登录用户deptId
     * @return
     */
    public static long deptId(){
        try
        {
            LoginUser loginUser = (LoginUser) getAuthentication().getPrincipal();
            SysUser user = loginUser.getUser();
            return user.getDeptId();
        }
        catch (Exception e)
        {
            throw new CustomException("获取用户信息异常", HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * 获取登录用户公司id--同下
     * @return
     */
    public static long companyId(){
        try
        {
            LoginUser loginUser = (LoginUser) getAuthentication().getPrincipal();
            SysUser user = loginUser.getUser();
            return user.getCompanyId();
        }
        catch (Exception e)
        {
            throw new CustomException("获取用户信息异常", HttpStatus.UNAUTHORIZED);
        }
    }

    public static long getCompanyId(Long deptId){
        //获取bean
        ISysDeptService service = ApplicationContextUtils.getBean(ISysDeptService.class);
        SysDept dept = service.selectDeptById(deptId);
        if (dept.getIsCompany().equals("0") ){
            return dept.getDeptId();
        } else if(dept.getIsCompany().equals( "1")){
            return dept.getDeptId();
        } else {
            return dept.getParentId();
        }
    }

    /**
     * 获取登录用户deptId(公司id)
     */
    public static long getDeptId(){
        try
        {
            LoginUser loginUser = (LoginUser) getAuthentication().getPrincipal();
            SysUser user = loginUser.getUser();
            //获取bean
            ISysDeptService service = ApplicationContextUtils.getBean(ISysDeptService.class);
                SysDept dept = service.selectDeptById(user.getDeptId());
                if (dept.getIsCompany().equals("0") ){
                    return dept.getDeptId();
                } else if(dept.getIsCompany().equals( "1")){
                    return dept.getDeptId();
                } else {
                    return dept.getParentId();
                }
        }
        catch (Exception e)
        {
            throw new CustomException("获取用户信息异常", HttpStatus.UNAUTHORIZED);
        }
    }

}
