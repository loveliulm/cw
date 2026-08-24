package com.qifu.accounting.carryover.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import java.util.HashMap;
import java.util.List;
import java.util.Arrays;
import java.util.Map;

import com.qifu.accounting.carryover.service.IBookCarryoverService;
import com.qifu.common.utils.StringUtils;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.qifu.framework.aspectj.lang.annotation.Log;
import com.qifu.framework.aspectj.lang.enums.BusinessType;
import com.qifu.accounting.carryover.domain.BookCarryoverRule;
import com.qifu.accounting.carryover.service.IBookCarryoverRuleService;
import com.qifu.framework.web.controller.BaseController;
import com.qifu.framework.web.domain.AjaxResult;
import com.qifu.common.utils.poi.ExcelUtil;
import com.qifu.framework.web.page.TableDataInfo;

/**
 * 结转规则Controller
 *
 *  YM
 * 07-23
 */
@AllArgsConstructor
@RestController
@RequestMapping("/accounting/carryoverRule" )
public class BookCarryoverRuleController extends BaseController {

    private final IBookCarryoverRuleService iBookCarryoverRuleService;
    private final IBookCarryoverService carryoverService;

    /**
     * 查询结转规则列表
     */
//    @PreAuthorize("@ss.hasPermi('accounting:carryoverRule:list')")
    @GetMapping("/list")
    public Map<?,?> list(BookCarryoverRule bookCarryoverRule)
    {
        Map<Object,Object> result = new HashMap<>();
        List<BookCarryoverRule> list = iBookCarryoverRuleService.getList(bookCarryoverRule);
        String carryoverName = carryoverService.getById(bookCarryoverRule.getCarryoverId()).getText();
        result.put("rows",list);
        result.put("code",200);
        result.put("msg","操作成功");
        result.put("carryoverName",carryoverName);
        result.put("total",list.size());
        return result;
    }
}
