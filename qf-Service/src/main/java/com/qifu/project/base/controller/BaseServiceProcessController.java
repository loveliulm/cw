package com.qifu.project.base.controller;

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
import com.qifu.project.base.domain.BaseServiceProcess;
import com.qifu.project.base.service.IBaseServiceProcessService;
import com.qifu.framework.web.controller.BaseController;
import com.qifu.framework.web.domain.AjaxResult;
import com.qifu.common.utils.poi.ExcelUtil;
import com.qifu.framework.web.page.TableDataInfo;

/**
 * 服务流程Controller
 *
 *
 * 06-23
 */
@AllArgsConstructor
@RestController
@RequestMapping("/system/service" )
public class BaseServiceProcessController extends BaseController {

    private final IBaseServiceProcessService iBaseServiceProcessService;



    /**
     * 查询服务流程列表
     */
    @PreAuthorize("@ss.hasPermi('system:process:list')")
    @GetMapping("/list")
    public TableDataInfo list()
    {
        startPage();
        LambdaQueryWrapper<BaseServiceProcess> lqw = new LambdaQueryWrapper<BaseServiceProcess>();
        List<BaseServiceProcess> list = iBaseServiceProcessService.list(lqw);
        return getDataTable(list);
    }

    /**
     * 导出服务流程列表
     */
    @PreAuthorize("@ss.hasPermi('system:process:export')" )
    @Log(title = "服务流程" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(BaseServiceProcess baseServiceProcess) {
        LambdaQueryWrapper<BaseServiceProcess> lqw = new LambdaQueryWrapper<BaseServiceProcess>(baseServiceProcess);
        List<BaseServiceProcess> list = iBaseServiceProcessService.list(lqw);
        ExcelUtil<BaseServiceProcess> util = new ExcelUtil<BaseServiceProcess>(BaseServiceProcess. class);
        return util.exportExcel(list, "process" );
    }

    /**
     * 获取服务流程详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:process:query')" )
    @GetMapping(value = "/{serviceProcessId}" )
    public AjaxResult getInfo(@PathVariable("serviceProcessId" ) Long serviceProcessId) {
        return AjaxResult.success(iBaseServiceProcessService.getById(serviceProcessId));
    }

    /**
     * 新增服务流程
     */
    @PreAuthorize("@ss.hasPermi('system:process:add')" )
    @Log(title = "服务流程" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BaseServiceProcess baseServiceProcess) {
        return toAjax(iBaseServiceProcessService.save(baseServiceProcess) ? 1 : 0);
    }

    /**
     * 修改服务流程
     */
    @PreAuthorize("@ss.hasPermi('system:process:edit')" )
    @Log(title = "服务流程" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BaseServiceProcess baseServiceProcess) {
        return toAjax(iBaseServiceProcessService.updateById(baseServiceProcess) ? 1 : 0);
    }

    /**
     * 删除服务流程
     */
    @PreAuthorize("@ss.hasPermi('system:process:remove')" )
    @Log(title = "服务流程" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{serviceProcessIds}" )
    public AjaxResult remove(@PathVariable Long[] serviceProcessIds) {
        return toAjax(iBaseServiceProcessService.removeByIds(Arrays.asList(serviceProcessIds)) ? 1 : 0);
    }
}
