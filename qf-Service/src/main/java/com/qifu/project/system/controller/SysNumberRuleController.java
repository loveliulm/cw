package com.qifu.project.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import java.util.List;
import java.util.Arrays;

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
import com.qifu.project.system.domain.SysNumberRule;
import com.qifu.project.system.service.ISysNumberRuleService;
import com.qifu.framework.web.controller.BaseController;
import com.qifu.framework.web.domain.AjaxResult;
import com.qifu.common.utils.poi.ExcelUtil;
import com.qifu.framework.web.page.TableDataInfo;

/**
 * 编号规则Controller
 *
 *
 * 06-18
 */
@AllArgsConstructor
@RestController
@RequestMapping("/system/rule" )
public class SysNumberRuleController extends BaseController {

    private final ISysNumberRuleService iSysNumberRuleService;

    /**
     * 查询编号规则列表
     */
    @PreAuthorize("@ss.hasPermi('system:rule:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysNumberRule sysNumberRule)
    {
        startPage();
        LambdaQueryWrapper<SysNumberRule> lqw = new LambdaQueryWrapper<SysNumberRule>();
        if (StringUtils.isNotBlank(sysNumberRule.getRuleName())){
            lqw.like(SysNumberRule::getRuleName ,sysNumberRule.getRuleName());
        }
        if (sysNumberRule.getRuleType() != null){
            lqw.eq(SysNumberRule::getRuleType ,sysNumberRule.getRuleType());
        }
        if (StringUtils.isNotBlank(sysNumberRule.getPrefix())){
            lqw.eq(SysNumberRule::getPrefix ,sysNumberRule.getPrefix());
        }
        if (sysNumberRule.getStatus() != null){
            lqw.eq(SysNumberRule::getStatus ,sysNumberRule.getStatus());
        }
        if (sysNumberRule.getSortNum() != null){
            lqw.eq(SysNumberRule::getSortNum ,sysNumberRule.getSortNum());
        }
        List<SysNumberRule> list = iSysNumberRuleService.list(lqw);
        return getDataTable(list);
    }

    /**
     * 导出编号规则列表
     */
    @PreAuthorize("@ss.hasPermi('system:rule:export')" )
    @Log(title = "编号规则" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(SysNumberRule sysNumberRule) {
        LambdaQueryWrapper<SysNumberRule> lqw = new LambdaQueryWrapper<SysNumberRule>(sysNumberRule);
        List<SysNumberRule> list = iSysNumberRuleService.list(lqw);
        ExcelUtil<SysNumberRule> util = new ExcelUtil<SysNumberRule>(SysNumberRule. class);
        return util.exportExcel(list, "rule" );
    }

    /**
     * 获取编号规则详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:rule:query')" )
    @GetMapping(value = "/{ruleId}" )
    public AjaxResult getInfo(@PathVariable("ruleId" ) Long ruleId) {
        return AjaxResult.success(iSysNumberRuleService.getById(ruleId));
    }

    /**
     * 新增编号规则
     */
    @PreAuthorize("@ss.hasPermi('system:rule:add')" )
    @Log(title = "编号规则" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysNumberRule sysNumberRule) {
        return toAjax(iSysNumberRuleService.save(sysNumberRule) ? 1 : 0);
    }

    /**
     * 修改编号规则
     */
    @PreAuthorize("@ss.hasPermi('system:rule:edit')" )
    @Log(title = "编号规则" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysNumberRule sysNumberRule) {
        return toAjax(iSysNumberRuleService.updateById(sysNumberRule) ? 1 : 0);
    }

    /**
     * 删除编号规则
     */
    @PreAuthorize("@ss.hasPermi('system:rule:remove')" )
    @Log(title = "编号规则" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ruleIds}" )
    public AjaxResult remove(@PathVariable Long[] ruleIds) {
        return toAjax(iSysNumberRuleService.removeByIds(Arrays.asList(ruleIds)) ? 1 : 0);
    }
}
