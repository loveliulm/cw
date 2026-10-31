package com.qifu.accounting.accountBook.controller;

import com.qifu.common.utils.IdUtils;
import com.qifu.common.utils.StringUtils;
import com.qifu.framework.redis.RedisCache;
import com.qifu.framework.web.domain.AjaxResult;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

/**
 * 文件控制层
 *  YM
 */
@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private RedisCache redisCache;

    /**
     * 保存
     * @param file
     * @return
     */
    @PostMapping("/savePDF")
    public AjaxResult savePDF(String file) throws Exception{
        if(StringUtils.isBlank(file)) {
            return AjaxResult.error("缺少必要参数");
        } else {
            String uuid = IdUtils.getUUID();
            redisCache.setCacheObject("PDF_" + uuid, file, 30, TimeUnit.MINUTES);
            return AjaxResult.success("uri", uuid);
        }
    }

    /**
     * 在线文件预览
     * @param uri
     * @param response
     */
    @GetMapping("/showPDF/{uri}")
    public void showPDF(@PathVariable("uri") String uri, HttpServletResponse response) throws Exception {
        String base64String =redisCache.getCacheObject("PDF_" + uri);
        if (StringUtils.isBlank(base64String)){
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().println("该pdf不存在");
        } else {
            response.setContentType("application/pdf;charset=UTF-8");
            base64String = base64String.replaceFirst("data:application/pdf;filename=generated.pdf;base64,", "");
            byte[] data = Base64.decodeBase64(base64String);
            OutputStream out = response.getOutputStream();
            out.write(data);
            out.flush();
            out.close();
        }
    }

}
