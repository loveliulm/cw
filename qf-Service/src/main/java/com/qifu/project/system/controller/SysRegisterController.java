package com.qifu.project.system.controller;

import cn.hutool.core.util.RandomUtil;
import com.cxytiandi.encrypt.springboot.annotation.Decrypt;
import com.qifu.common.exception.CustomException;
import com.qifu.common.utils.SmsUtil;
import com.qifu.framework.redis.RedisCache;
import com.qifu.framework.security.RegisterBody;
import com.qifu.framework.security.service.SysRegisterService;
import com.qifu.framework.web.domain.AjaxResult;
import com.qifu.project.system.domain.SysUser;
import com.qifu.project.system.service.ISysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@RestController
@Api(tags = "注册")
public class SysRegisterController {
    @Resource
    private SysRegisterService registerService;

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private RedisCache redisCache;

    @PostMapping(value = "/sendSmsCodeR")
    @ApiOperation("注册-发送手机验证码")
    public AjaxResult sendSmsCodeL(String phonenumber) {
        SysUser sysUser = sysUserService.selectUserByPhonenumber(phonenumber);
        if (sysUser != null) {
            throw new CustomException("此手机号已注册，请登录！");
        }
        // 从Redis查询手机验证码
        String redisKey = "PHONE_REGISTER-" + phonenumber;
        String code = redisCache.getCacheObject(redisKey);

        if (StringUtils.isBlank(code)) {
            code = RandomUtil.randomNumbers(6);
            redisCache.setCacheObject(redisKey, code, 5, TimeUnit.MINUTES);
        }
        System.out.println(phonenumber + "，注册验证码：" + code);
        try {
            SmsUtil.sendSmsCode(phonenumber, code, "SMS_270195200");
            return AjaxResult.success("验证码发送成功，有效期5分钟，请注意查收");
        } catch (Exception e) {
            throw new CustomException("验证码发送失败，请检查手机号输入是否正确");
        }
    }

    @Decrypt
    @ApiOperation("注册")
    @PostMapping("/register")
    public AjaxResult register(@RequestBody RegisterBody registerBody)
    {
        // 从Redis查询手机验证码
        String redisKey = "PHONE_REGISTER-" + registerBody.getPhonenumber();
        String phoneCode = redisCache.getCacheObject(redisKey);
        if (StringUtils.isBlank(phoneCode)) {
            throw new CustomException("手机验证码不存在或已过期");
        }
        if (StringUtils.isBlank(registerBody.getPhonecode()) || !registerBody.getPhonecode().equalsIgnoreCase(phoneCode)) {
            throw new CustomException("手机验证码错误");
        }

        registerService.register(registerBody.getUsername(), registerBody.getPassword(), registerBody.getPhonenumber(), registerBody.getDeptname());

        redisCache.deleteObject(redisKey);

        return AjaxResult.success();
    }

}
