package com.qifu.project.common;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletResponse;

import com.wf.captcha.ArithmeticCaptcha;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.qifu.common.constant.Constants;
import com.qifu.common.utils.IdUtils;
import com.qifu.common.utils.VerifyCodeUtils;
import com.qifu.common.utils.sign.Base64;
import com.qifu.framework.redis.RedisCache;
import com.qifu.framework.web.domain.AjaxResult;

/**
 * 验证码操作处理
 *
 *
 */
@Api(tags = "验证码")
@RestController
public class CaptchaController
{
    @Autowired
    private RedisCache redisCache;

    /**
     * 生成验证码
     */
    @ApiOperation("验证码")
    @GetMapping("/captchaImage")
    public AjaxResult getCode(HttpServletResponse response) throws IOException
    {
        // 生成随机字串
        String verifyCode = VerifyCodeUtils.generateVerifyCode(4);
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(111, 36);
        captcha.setLen(2);

        try {
            verifyCode = new Double(Double.parseDouble(captcha.text())).intValue() + "";
        } catch (Exception e) {
            verifyCode = captcha.text();
        }


        // 唯一标识
        String uuid = IdUtils.simpleUUID();
        String verifyKey = Constants.CAPTCHA_CODE_KEY + uuid;

        redisCache.setCacheObject(verifyKey, verifyCode, Constants.CAPTCHA_EXPIRATION, TimeUnit.MINUTES);
        // 生成图片
        int w = 111, h = 36;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        VerifyCodeUtils.outputImage(w, h, stream, verifyCode);
        try
        {
            AjaxResult ajax = AjaxResult.success();
//            ajax.put("code", verifyCode);
            ajax.put("uuid", uuid);
//            ajax.put("img", Base64.encode(stream.toByteArray()));
            ajax.put("img", captcha.toBase64());
            return ajax;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }
        finally
        {
            stream.close();
        }
    }


    /**
     * 生成验证码-调试用
     */
    @ApiOperation("验证码-调试用")
    @GetMapping("/captchaImage2")
    public AjaxResult getCode2(HttpServletResponse response) throws IOException
    {
        // 生成随机字串
        String verifyCode = VerifyCodeUtils.generateVerifyCode(4);
        System.out.println("code:"+verifyCode);
        // 唯一标识
        String uuid = IdUtils.simpleUUID();
        System.out.println("uuid:"+uuid);
        String verifyKey = Constants.CAPTCHA_CODE_KEY + uuid;

        redisCache.setCacheObject(verifyKey, verifyCode, Constants.CAPTCHA_EXPIRATION, TimeUnit.MINUTES);
        // 生成图片
        int w = 111, h = 36;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        VerifyCodeUtils.outputImage(w, h, stream, verifyCode);
        try
        {
            AjaxResult ajax = AjaxResult.success();
            ajax.put("code", verifyCode);
            ajax.put("uuid", uuid);
            ajax.put("img", Base64.encode(stream.toByteArray()));
            return ajax;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }
        finally
        {
            stream.close();
        }
    }
}
