package com.qifu.project.base.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import java.util.List;
import java.util.Arrays;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qifu.common.utils.StringUtils;
import com.qifu.project.base.domain.BaseServiceItem;
import com.qifu.project.base.service.IBaseServiceItemService;
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
import com.qifu.project.base.domain.BaseServiceType;
import com.qifu.project.base.service.IBaseServiceTypeService;
import com.qifu.framework.web.controller.BaseController;
import com.qifu.framework.web.domain.AjaxResult;
import com.qifu.common.utils.poi.ExcelUtil;
import com.qifu.framework.web.page.TableDataInfo;

/**
 * Controller
 *
 *
 * 06-17
 */
@AllArgsConstructor
@RestController
@RequestMapping("/base/type" )
public class BaseServiceTypeController extends BaseController {

    private final IBaseServiceTypeService iBaseServiceTypeService;

    @Autowired
    private IBaseServiceItemService iBaseServiceItemService;

    /**
     * 查询服务类型列表
     */
//    @PreAuthorize("@ss.hasPermi('base:type:list')")
    @GetMapping("/list")
    public TableDataInfo list(BaseServiceType baseServiceType)
    {
        startPage();
        LambdaQueryWrapper<BaseServiceType> lqw = new LambdaQueryWrapper<BaseServiceType>();
        if (StringUtils.isNotBlank(baseServiceType.getServiceTypeName())){
            lqw.like(BaseServiceType::getServiceTypeName ,baseServiceType.getServiceTypeName());
        }
        if (baseServiceType.getServiceClassId() != null){
            lqw.like(BaseServiceType::getServiceClassId ,baseServiceType.getServiceClassId());
        }
        List<BaseServiceType> list = iBaseServiceTypeService.list(lqw);
        return getDataTable(list);
    }

    /**
     * 导出服务类型列表
     */
//    @PreAuthorize("@ss.hasPermi('base:type:export')" )
    @Log(title = "导出服务类型列表" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(BaseServiceType baseServiceType) {
        LambdaQueryWrapper<BaseServiceType> lqw = new LambdaQueryWrapper<BaseServiceType>(baseServiceType);
        List<BaseServiceType> list = iBaseServiceTypeService.list(lqw);
        ExcelUtil<BaseServiceType> util = new ExcelUtil<BaseServiceType>(BaseServiceType. class);
        return util.exportExcel(list, "type" );
    }

    /**
     * 获取详细信息
     */
//    @PreAuthorize("@ss.hasPermi('base:type:query')" )
    @GetMapping(value = "/{serviceTypeId}" )
    public AjaxResult getInfo(@PathVariable("serviceTypeId" ) Long serviceTypeId) {
        return AjaxResult.success(iBaseServiceTypeService.getById(serviceTypeId));
    }

    /**
     * 新增服务类型
     */
//    @PreAuthorize("@ss.hasPermi('base:type:add')" )
    @Log(title = "新增服务类型" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BaseServiceType baseServiceType) {
        return toAjax(iBaseServiceTypeService.save(baseServiceType) ? 1 : 0);
    }

    /**
     * 修改服务类型
     */
//    @PreAuthorize("@ss.hasPermi('base:type:edit')" )
    @Log(title = "修改服务类型" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BaseServiceType baseServiceType) {
        return toAjax(iBaseServiceTypeService.updateById(baseServiceType) ? 1 : 0);
    }

    /**
     * 删除服务类型
     */
//    @PreAuthorize("@ss.hasPermi('base:type:remove')" )
    @Log(title = "删除服务类型" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{serviceTypeIds}" )
    public AjaxResult remove(@PathVariable Long[] serviceTypeIds) {
        List<Long> longs = Arrays.asList(serviceTypeIds);
        for (Long id : longs) {
            QueryWrapper<BaseServiceItem> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("service_type_id", id);
            List<BaseServiceItem> list = iBaseServiceItemService.list(queryWrapper);
            if (list != null && list.size() > 0) {
                return AjaxResult.error("存在子级不可删除");
            }
        }
        return toAjax(iBaseServiceTypeService.removeByIds(Arrays.asList(serviceTypeIds)) ? 1 : 0);
    }
}
