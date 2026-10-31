package com.qifu.project.system.controller;

import com.qifu.common.utils.SecurityUtils;
import com.qifu.common.utils.bean.BeanConverUtil;
import com.qifu.common.utils.poi.ExcelUtil;
import com.qifu.framework.aspectj.lang.annotation.Log;
import com.qifu.framework.aspectj.lang.enums.BusinessType;
import com.qifu.framework.web.controller.BaseController;
import com.qifu.framework.web.domain.AjaxResult;
import com.qifu.framework.web.page.TableDataInfo;
import com.qifu.project.system.domain.SysUserConfig;
import com.qifu.project.system.domain.configvo.*;
import com.qifu.project.system.service.ISysUserConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 配置管理-系统配置
 */
@RestController
@RequestMapping("/config/system")
public class SysUserConfigController extends BaseController {
    @Autowired
    private ISysUserConfigService userConfigService;

    /**
     * 获取系统配置列表
     */
//    @PreAuthorize("@ss.hasPermi('config:system:list')")
    @GetMapping("/list")
    public TableDataInfo selectList(SysUserConfig info) {
        startPage();
        List<SysUserConfig> list = userConfigService.selectList(info);
        return getDataTable(list);
    }

    /**
     * 获取系统配置列表-不分页
     */
    @GetMapping("/getList")
    public AjaxResult getList(SysUserConfig info) {
        info.setUserId(SecurityUtils.userId());
        List<SysUserConfig> list = userConfigService.selectList(info);
        return AjaxResult.success(list);
    }

    /**
     * 根据id获取详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult selectById(@PathVariable Long id) {
        return AjaxResult.success(userConfigService.selectById(id));
    }

    /**
     * 新增通系统配置
     */
    @Log(title = "系统配置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysUserConfig info) {
        info.setCreateBy(SecurityUtils.getUsername());
        info.setUserId(SecurityUtils.userId());
        return toAjax(userConfigService.insertInfo(info));
    }

    /**
     * 修改系统配置
     */
    @Log(title = "系统配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysUserConfig info) {
        info.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(userConfigService.updateInfo(info));
    }

    /**
     * 删除系统配置
     */
    @Log(title = "系统配置", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    public AjaxResult delete(@PathVariable Long id) {
        return toAjax(userConfigService.deleteById(id));
    }

//    /**
//     * 删除系统配置
//     */
//    @Log(title = "系统配置", businessType = BusinessType.DELETE)
//    @DeleteMapping("/{ids}")
//    public AjaxResult remove(@PathVariable Long[] ids) {
//        return toAjax(userConfigService.deleteByIds(ids));
//    }

    /**
     * 导出系统配置
     */
    @Log(title = "系统配置", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SysUserConfig info) {
        List<SysUserConfig> list = userConfigService.selectList(info);
        if (info.getType() == 1) {
            ExcelUtil<TypeConfigVo1> util = new ExcelUtil<>(TypeConfigVo1.class);
            return util.exportExcel(BeanConverUtil.converList(list, TypeConfigVo1.class), "客户来源管理");
        } else if (info.getType() == 2) {
            ExcelUtil<TypeConfigVo2> util = new ExcelUtil<>(TypeConfigVo2.class);
            return util.exportExcel(BeanConverUtil.converList(list, TypeConfigVo2.class), "日记账分类管理");
        } else if (info.getType() == 3) {
            ExcelUtil<TypeConfigVo3> util = new ExcelUtil<>(TypeConfigVo3.class);
            return util.exportExcel(BeanConverUtil.converList(list, TypeConfigVo3.class), "日记账收支方式管理");
        } else if (info.getType() == 4) {
            ExcelUtil<TypeConfigVo4> util = new ExcelUtil<>(TypeConfigVo4.class);
            return util.exportExcel(BeanConverUtil.converList(list, TypeConfigVo4.class), "老板信息管理");
        } else if (info.getType() == 5) {
            ExcelUtil<TypeConfigVo5> util = new ExcelUtil<>(TypeConfigVo5.class);
            return util.exportExcel(BeanConverUtil.converList(list, TypeConfigVo5.class), "税务局信息管理");
        } else if (info.getType() == 6) {
            ExcelUtil<TypeConfigVo6> util = new ExcelUtil<>(TypeConfigVo6.class);
            return util.exportExcel(BeanConverUtil.converList(list, TypeConfigVo6.class), "工商局信息管理");
        } else if (info.getType() == 7) {
            ExcelUtil<TypeConfigVo7> util = new ExcelUtil<>(TypeConfigVo7.class);
            return util.exportExcel(BeanConverUtil.converList(list, TypeConfigVo7.class), "公司企业类型管理");
        } else if (info.getType() == 8) {
            ExcelUtil<TypeConfigVo8> util = new ExcelUtil<>(TypeConfigVo8.class);
            return util.exportExcel(BeanConverUtil.converList(list, TypeConfigVo8.class), "公司客户状态管理");
        } else if (info.getType() == 9) {
            ExcelUtil<TypeConfigVo9> util = new ExcelUtil<>(TypeConfigVo9.class);
            return util.exportExcel(BeanConverUtil.converList(list, TypeConfigVo9.class), "缴税税种信息管理");
        } else if (info.getType() == 10) {
            ExcelUtil<TypeConfigVo10> util = new ExcelUtil<>(TypeConfigVo10.class);
            return util.exportExcel(BeanConverUtil.converList(list, TypeConfigVo10.class), "税务登记信息管理");
        } else if (info.getType() == 11) {
            ExcelUtil<TypeConfigVo11> util = new ExcelUtil<>(TypeConfigVo11.class);
            return util.exportExcel(BeanConverUtil.converList(list, TypeConfigVo11.class), "登录身份信息管理");
        } else if (info.getType() == 21) {
            ExcelUtil<TypeConfigVo21> util = new ExcelUtil<>(TypeConfigVo21.class);
            return util.exportExcel(BeanConverUtil.converList(list, TypeConfigVo21.class), "物品类型管理");
        } else if (info.getType() == 22) {
            ExcelUtil<TypeConfigVo22> util = new ExcelUtil<>(TypeConfigVo22.class);
            return util.exportExcel(BeanConverUtil.converList(list, TypeConfigVo22.class), "物品名称管理");
        } else if (info.getType() == 23) {
            ExcelUtil<TypeConfigVo23> util = new ExcelUtil<>(TypeConfigVo23.class);
            return util.exportExcel(BeanConverUtil.converList(list, TypeConfigVo23.class), "物品库位置管理");
        } else if (info.getType() == 24) {
            ExcelUtil<TypeConfigVo24> util = new ExcelUtil<>(TypeConfigVo24.class);
            return util.exportExcel(BeanConverUtil.converList(list, TypeConfigVo24.class), "物品出库方式管理");
        }
        return AjaxResult.success();
    }
}
