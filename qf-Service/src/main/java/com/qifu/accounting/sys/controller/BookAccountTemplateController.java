package com.qifu.accounting.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qifu.accounting.sys.domain.BookAccountTemplate;
import com.qifu.accounting.sys.service.IBookAccountTemplateService;
import com.qifu.common.constant.Constants;
import com.qifu.framework.redis.RedisCache;
import com.qifu.framework.web.controller.BaseController;
import com.qifu.framework.web.page.TableDataInfo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 会计制度Controller
 *
 *  YM
 * 07-03
 */
@AllArgsConstructor
@RestController
@RequestMapping("/accounting/accountTemplate" )
public class BookAccountTemplateController extends BaseController {

    private final IBookAccountTemplateService iBookAccountTemplateService;

    @Autowired
    private RedisCache redisCache;

    /**
     * 查询会计制度列表
     */
    @GetMapping("/list")
    public TableDataInfo list()
    {
        startPage();
        String key = Constants.BOOK_ACCOUNT_TEMPLATE;
        List<BookAccountTemplate> list = redisCache.getCacheList(key);
        if(list.isEmpty()){
            LambdaQueryWrapper<BookAccountTemplate> lqw = new LambdaQueryWrapper<BookAccountTemplate>();
            list = iBookAccountTemplateService.list(lqw);
            if(!list.isEmpty()){
                redisCache.setCacheList(key,list);
            }
        }
        return getDataTable(list);
    }

}
