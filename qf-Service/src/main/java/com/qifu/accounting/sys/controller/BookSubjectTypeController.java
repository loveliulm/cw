package com.qifu.accounting.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qifu.accounting.sys.domain.BookSubjectType;
import com.qifu.accounting.sys.mapper.BookSubjectMapper;
import com.qifu.accounting.sys.service.IBookSubjectTypeService;
import com.qifu.common.constant.Constants;
import com.qifu.common.utils.Asserts;
import com.qifu.framework.redis.RedisCache;
import com.qifu.framework.web.controller.BaseController;
import com.qifu.framework.web.domain.AjaxResult;
import com.qifu.framework.web.page.TableDataInfo;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 科目类型Controller
 *
 *  YM
 * 07-03
 */
@AllArgsConstructor
@RestController
@RequestMapping("/accounting/subjectType" )
public class BookSubjectTypeController extends BaseController {

    private final IBookSubjectTypeService iBookSubjectTypeService;

    @Autowired
    private BookSubjectMapper subjectMapper;

    @Autowired
    private RedisCache redisCache;

    /**
     * 查询科目类型列表
     */
    @GetMapping("/list")
    public TableDataInfo list(Long bookId)
    {
        startPage();
        Long templateId = subjectMapper.templateIdByBookId(bookId);
        Asserts.isNull(templateId,"查询科目类型失败");

        String key = Constants.BOOK_SUBJECT_TYPE+templateId;
        List<BookSubjectType> list = redisCache.getCacheList(key);
        if(list.isEmpty()){
            LambdaQueryWrapper<BookSubjectType> lqw = new LambdaQueryWrapper<BookSubjectType>();
            lqw.eq(BookSubjectType::getAccountTemplateId,templateId)
            .orderByAsc(BookSubjectType::getTypeId);
            list = iBookSubjectTypeService.list(lqw);
            if(!list.isEmpty()){
                redisCache.setCacheList(key,list);
            }
        }
        return getDataTable(list);
    }

    /**
     * 获取科目类型详细信息
     */
    @ApiOperation("获取科目类型详细信息")
    @GetMapping(value = "/{typeId}" )
    public AjaxResult getInfo(@PathVariable("typeId" ) Long typeId) {
        return AjaxResult.success(iBookSubjectTypeService.getById(typeId));
    }

}
