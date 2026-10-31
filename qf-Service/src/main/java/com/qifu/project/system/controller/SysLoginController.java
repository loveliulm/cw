package com.qifu.project.system.controller;

import java.util.*;
import java.util.concurrent.TimeUnit;

import cn.hutool.core.util.RandomUtil;
import com.cxytiandi.encrypt.springboot.annotation.Decrypt;
import com.qifu.common.exception.CustomException;
import com.qifu.common.utils.DateUtils;
import com.qifu.common.utils.SecurityUtils;
import com.qifu.common.utils.SmsUtil;
import com.qifu.common.utils.bean.BeanUtils;
import com.qifu.framework.redis.RedisCache;
import com.qifu.framework.security.PhoneLoginBody;
import com.qifu.framework.security.UpdatePwdBody;
import com.qifu.project.customer.domain.vo.AccountBookVoTwo;
import com.qifu.project.customer.service.ITAccountBookService;
import com.qifu.project.system.domain.SysDept;
import com.qifu.project.system.domain.vo.UserVo;
import com.qifu.project.system.service.ISysDeptService;
import com.qifu.project.system.service.ISysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.qifu.common.constant.Constants;
import com.qifu.common.utils.ServletUtils;
import com.qifu.framework.security.LoginBody;
import com.qifu.framework.security.LoginUser;
import com.qifu.framework.security.service.SysLoginService;
import com.qifu.framework.security.service.SysPermissionService;
import com.qifu.framework.security.service.TokenService;
import com.qifu.framework.web.domain.AjaxResult;
import com.qifu.project.system.domain.SysMenu;
import com.qifu.project.system.domain.SysUser;
import com.qifu.project.system.service.ISysMenuService;

/**
 * 登录验证
 *
 *
 */
@Api(tags = "登录")
@RestController
public class SysLoginController
{
    @Autowired
    private SysLoginService loginService;

    @Autowired
    private ISysMenuService menuService;

    @Autowired
    private SysPermissionService permissionService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private ISysDeptService sysDeptService;
    @Autowired
    private ITAccountBookService itAccountBookService;

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private RedisCache redisCache;

    /**
     * 登录方法
     *
     * @param username 用户名
     * @param password 密码
     * @param captcha 验证码
     * @param uuid 唯一标识
     * @return 结果
     */
    @Decrypt
    @ApiOperation("登录")
    @PostMapping("/login")
    public AjaxResult login(@RequestBody LoginBody loginBody)
    {
//        Calendar cal = Calendar.getInstance();
//
//        cal.set(Calendar.YEAR, 2021);
//        cal.set(Calendar.MONTH,3);
//        cal.set(Calendar.DAY_OF_MONTH,30);
//        cal.set(Calendar.HOUR_OF_DAY,18);
//        cal.set(Calendar.MINUTE,10);
//        cal.set(Calendar.SECOND,0);
//        if (new Date().getTime() > cal.getTime().getTime()) {
//            return AjaxResult.error("已过期！请联系管理员！");
//        } else {
            AjaxResult ajax = AjaxResult.success();
            // 生成令牌
            String token = loginService.login(loginBody.getPhonenumber(), loginBody.getPassword(), loginBody.getCode(),
                    loginBody.getUuid());
            ajax.put(Constants.TOKEN, token);
            return ajax;
//        }

    }

    @PostMapping(value = "/sendSmsCodeL")
    @ApiOperation("手机号登录-发送手机验证码")
    public AjaxResult sendSmsCodeL(String phonenumber) {
        SysUser sysUser = sysUserService.selectUserByPhonenumber(phonenumber);
        if (sysUser == null) {
            throw new CustomException("此手机号未注册，请先注册！");
        }
        // 从Redis查询手机验证码
        String redisKey = "PHONE_LOGIN-" + phonenumber;
        String code = redisCache.getCacheObject(redisKey);

        if (StringUtils.isBlank(code)) {
            code = RandomUtil.randomNumbers(6);
            redisCache.setCacheObject(redisKey, code, 5, TimeUnit.MINUTES);
        }
        try {
            SmsUtil.sendSmsCode(phonenumber, code, "SMS_270190207");
            return AjaxResult.success("验证码发送成功，有效期5分钟，请注意查收");
        } catch (Exception e) {
            throw new CustomException("验证码发送失败，请检查手机号输入是否正确");
        }
    }

    @Decrypt
    @ApiOperation("手机号登录")
    @PostMapping("/phoneLogin")
    public AjaxResult phoneLogin(@RequestBody PhoneLoginBody loginBody) {
        AjaxResult ajax = AjaxResult.success();
        SysUser sysUser = sysUserService.selectUserByPhonenumber(loginBody.getPhonenumber());
        if (sysUser == null) {
            throw new CustomException("此手机号未注册，请先注册！");
        }

        // 从Redis查询手机验证码
        String redisKey = "PHONE_LOGIN-" + loginBody.getPhonenumber();
        String phoneCode = redisCache.getCacheObject(redisKey);
        if (StringUtils.isBlank(phoneCode)) {
            throw new CustomException("手机验证码不存在或已过期");
        }
        if (StringUtils.isBlank(loginBody.getPhonecode()) || !loginBody.getPhonecode().equalsIgnoreCase(phoneCode)) {
            throw new CustomException("手机验证码错误");
        }

        // 生成令牌
        String token = loginService.logins(loginBody.getPhonenumber());
        ajax.put(Constants.TOKEN, token);

        redisCache.deleteObject(redisKey);

        return ajax;
    }

    @PostMapping(value = "/sendSmsCodeU")
    @ApiOperation("修改密码-发送手机验证码")
    public AjaxResult sendSmsCodeU(String phonenumber) {
        SysUser sysUser = sysUserService.selectUserByPhonenumber(phonenumber);
        if (sysUser == null) {
            throw new CustomException("此手机号未注册，请先注册！");
        }
        // 从Redis查询手机验证码
        String redisKey = "PHONE_UPDATEPWD-" + phonenumber;
        String code = redisCache.getCacheObject(redisKey);

        if (StringUtils.isBlank(code)) {
            code = RandomUtil.randomNumbers(6);
            redisCache.setCacheObject(redisKey, code, 5, TimeUnit.MINUTES);
        }
        try {
            SmsUtil.sendSmsCode(phonenumber, code, "SMS_270275202");
            return AjaxResult.success("验证码发送成功，有效期5分钟，请注意查收");
        } catch (Exception e) {
            throw new CustomException("验证码发送失败，请检查手机号输入是否正确");
        }
    }

    @Decrypt
    @ApiOperation("修改密码")
    @PostMapping("/updatePassword")
    public AjaxResult updatePassword(@RequestBody UpdatePwdBody updatePwdBody) {
        SysUser sysUser = sysUserService.selectUserByPhonenumber(updatePwdBody.getPhonenumber());
        if (sysUser == null) {
            throw new CustomException("此手机号未注册，请先注册！");
        }

        // 从Redis查询手机验证码
        String redisKey = "PHONE_UPDATEPWD-" + updatePwdBody.getPhonenumber();
        String phoneCode = redisCache.getCacheObject(redisKey);
        if (StringUtils.isBlank(phoneCode)) {
            throw new CustomException("手机验证码不存在或已过期");
        }
        if (StringUtils.isBlank(updatePwdBody.getPhonecode()) || !updatePwdBody.getPhonecode().equalsIgnoreCase(phoneCode)) {
            throw new CustomException("手机验证码错误");
        }

        if (!updatePwdBody.getPassword().equals(updatePwdBody.getRepeatpassword())) {
            throw new CustomException("账号密码输入不一致");
        }
        sysUser.setPassword(SecurityUtils.encryptPassword(updatePwdBody.getPassword()));
        sysUserService.resetPwd(sysUser);

        redisCache.deleteObject(redisKey);

        return AjaxResult.success();
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @ApiOperation("获取用户信息")
    @GetMapping("getInfo")
    public AjaxResult getInfo() {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        SysUser user = loginUser.getUser();
        UserVo userVo = new UserVo();
        BeanUtils.copyBeanProp(userVo,user);
        userVo.setToken(loginUser.getToken());
        // 角色集合
        Set<String> roles = permissionService.getRolePermission(user);
        // 权限集合
        Set<String> permissions = permissionService.getMenuPermission(user);
        //判断登录用户是否公司管理员
        if(!SecurityUtils.isAdmin()){
            int status = sysDeptService.checkDeptValidTime(SecurityUtils.companyId());
            SysDept dept = userVo.getDept();
            if (status == 1){
                //即将到期更新状态
                if (!"1".equals(dept.getDeptStatus())){
                    dept.setDeptStatus("1");
                    sysDeptService.updateDeptStatusById(dept);
                }
            } else if (status == 2) {
                //到期限制操作权限
                permissions = filtrate(permissions);
                //更新公司状态
                if (!"2".equals(dept.getDeptStatus())){
                    dept.setDeptStatus("2");
                    sysDeptService.updateDeptStatusById(dept);
                }
            }else {
                if (!"0".equals(dept.getDeptStatus())){
                    dept.setDeptStatus("0");
                    sysDeptService.updateDeptStatusById(dept);
                }
            }
            userVo.setDept(dept);
        }
        AjaxResult ajax = AjaxResult.success();
        ajax.put("user", userVo);
        ajax.put("roles", roles);
        ajax.put("permissions", permissions);
        return ajax;
    }

    public Set<String> filtrate(Set<String> strings){
        Set<String> set = new HashSet<>();
        for (String str: strings) {
            if (str.contains("list")){
                set.add(str);
            }
        }
        return set;
    }

    /**
     * 获取路由信息
     *
     * @return 路由信息
     */
    @ApiOperation("获取路由信息")
    @GetMapping("getRouters")
    public AjaxResult getRouters()
    {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        // 用户信息
        SysUser user = loginUser.getUser();
        List<SysMenu> menus = menuService.selectMenuTreeByUserId(user.getUserId(),1);
        return AjaxResult.success(menuService.buildMenus(menus));
    }


    /**
     * 账套系统-获取路由信息
     *
     * @return 路由信息
     */
    @ApiOperation("获取路由信息")
    @GetMapping("/book/getRouters")
    public AjaxResult getRoutersByBook()
    {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        // 用户信息
        SysUser user = loginUser.getUser();
        List<SysMenu> menus = menuService.selectMenuTreeByUserId(user.getUserId(),2);

        //如果账号到期不返回结账菜单。
        if(!SecurityUtils.isAdmin() && sysDeptService.checkDeptValidTime(SecurityUtils.companyId()) == 2) {
            for (int i = menus.size() - 1; i >= 0; i--) {
                if("结账".equals(menus.get(i).getMenuName())){
                    menus.remove(menus.get(i));
                }
            }
        }
        return AjaxResult.success(menuService.buildMenus(menus));
    }

    /**
     * 获取公司基本信息
     * @return
     */
    @ApiOperation("获取公司基本信息")
    @GetMapping("/getDeptMessage")
    public AjaxResult getDeptMessage(){
        SysDept dept = sysDeptService.selectDeptById(SecurityUtils.companyId());
        AccountBookVoTwo accountBookVoTwo = itAccountBookService.selectAccountBookStatusByDeptId(SecurityUtils.companyId());
        if (accountBookVoTwo == null) {
            accountBookVoTwo = new AccountBookVoTwo(0,0,0);
        }
        Map<String,Object> map = new HashMap<>();
        map.put("bookNumber",dept.getAccountBookNum());
        map.put("endTime", DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD,dept.getEndDate()));
        map.put("occupied",accountBookVoTwo.getOccupied());
        map.put("disable",accountBookVoTwo.getDisable());
        map.put("recycled",accountBookVoTwo.getRecycled());
        return AjaxResult.success(map);
    }
}
