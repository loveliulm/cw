package com.qifu.accounting.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qifu.accounting.sys.domain.BookSubjectCategory;
import com.qifu.accounting.sys.service.IBookSubjectCategoryService;
import com.qifu.common.constant.Constants;
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

import java.util.List;

/**
 * 科目类目Controller
 *
 *  YM
 * 07-03
 */
@AllArgsConstructor
@RestController
@RequestMapping("/accounting/subjectCategory" )
public class BookSubjectCategoryController extends BaseController {

    private final IBookSubjectCategoryService iBookSubjectCategoryService;

    @Autowired
    private RedisCache redisCache;

    /**
     * 查询科目类目列表
     */
    @GetMapping("/list")
    public TableDataInfo list(Long typeId)
    {
        startPage();
        String key = Constants.BOOK_SUBJECT_CATEGORY + typeId;
        List<BookSubjectCategory> list = redisCache.getCacheList(key);
        if(list.isEmpty()){
            LambdaQueryWrapper<BookSubjectCategory> lqw = new LambdaQueryWrapper<BookSubjectCategory>();
            lqw.eq(BookSubjectCategory::getTypeId,typeId)
                    .orderByAsc(BookSubjectCategory::getCategoryId);
            list = iBookSubjectCategoryService.list(lqw);
            if(!list.isEmpty()){
                redisCache.setCacheList(key,list);
            }
        }
        return getDataTable(list);
    }

    /**
     * 获取科目类别详细信息
     */
    @ApiOperation("获取科目类型详细信息")
    @GetMapping(value = "/{categoryId}" )
    public AjaxResult getInfo(@PathVariable("categoryId" ) Long categoryId) {
        return AjaxResult.success(iBookSubjectCategoryService.getById(categoryId));
    }

}
