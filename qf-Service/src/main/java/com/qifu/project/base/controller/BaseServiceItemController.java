package com.qifu.project.base.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import java.util.List;
import java.util.Arrays;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qifu.common.utils.StringUtils;
import com.qifu.project.system.mapper.SysUserConfigMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.aspectj.weaver.loadtime.Aj;
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
import com.qifu.project.base.domain.BaseServiceItem;
import com.qifu.project.base.service.IBaseServiceItemService;
import com.qifu.framework.web.controller.BaseController;
import com.qifu.framework.web.domain.AjaxResult;
import com.qifu.common.utils.poi.ExcelUtil;
import com.qifu.framework.web.page.TableDataInfo;

/**
 * 服务项目Controller
 *
 *
 * 06-19
 */
@Api(tags = "服务项目")
@AllArgsConstructor
@RestController
@RequestMapping("/base/item" )
public class BaseServiceItemController extends BaseController {

    private final IBaseServiceItemService iBaseServiceItemService;

    @Autowired
    private SysUserConfigMapper sysUserConfigMapper;

    /**
     * 查询服务项目列表
     */
    @ApiOperation("查询服务项目列表")
//    @PreAuthorize("@ss.hasPermi('base:item:list')")
    @GetMapping("/list")
    public TableDataInfo list(BaseServiceItem baseServiceItem)
    {
        startPage();
        LambdaQueryWrapper<BaseServiceItem> lqw = new LambdaQueryWrapper<BaseServiceItem>();
        if (StringUtils.isNotBlank(baseServiceItem.getServiceItemName())){
            lqw.like(BaseServiceItem::getServiceItemName ,baseServiceItem.getServiceItemName());
        }
        if (baseServiceItem.getServiceTypeId() != null){
            lqw.eq(BaseServiceItem::getServiceTypeId ,baseServiceItem.getServiceTypeId());
        }
        List<BaseServiceItem> list = iBaseServiceItemService.list(lqw);
        return getDataTable(list);
    }

    /**
     * 导出服务项目列表
     */
//    @PreAuthorize("@ss.hasPermi('base:item:export')" )
    @Log(title = "导出服务项目列表" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(BaseServiceItem baseServiceItem) {
        LambdaQueryWrapper<BaseServiceItem> lqw = new LambdaQueryWrapper<BaseServiceItem>(baseServiceItem);
        List<BaseServiceItem> list = iBaseServiceItemService.list(lqw);
        ExcelUtil<BaseServiceItem> util = new ExcelUtil<BaseServiceItem>(BaseServiceItem. class);
        return util.exportExcel(list, "item" );
    }

    /**
     * 获取服务项目详细信息
     */
//    @PreAuthorize("@ss.hasPermi('base:item:query')" )
    @GetMapping(value = "/{serviceItemId}" )
    public AjaxResult getInfo(@PathVariable("serviceItemId" ) Long serviceItemId) {
        BaseServiceItem baseServiceItem = iBaseServiceItemService.getById(serviceItemId);
        if (baseServiceItem.getServiceItemParentId() != 0) {
            baseServiceItem.setServiceItemParentName(iBaseServiceItemService.getById(baseServiceItem.getServiceItemParentId()).getServiceItemName());
            baseServiceItem.setSysUserConfigName(sysUserConfigMapper.selectById(baseServiceItem.getSysUserConfigId()).getName() + "（" + baseServiceItem.getServiceItemName() + "）");
        }
        return AjaxResult.success(baseServiceItem);
    }

    /**
     * 新增服务项目
     */
//    @PreAuthorize("@ss.hasPermi('base:item:add')" )
    @Log(title = "新增服务项目" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BaseServiceItem baseServiceItem) {
        return toAjax(iBaseServiceItemService.save(baseServiceItem) ? 1 : 0);
    }

    /**
     * 修改服务项目
     */
//    @PreAuthorize("@ss.hasPermi('base:item:edit')" )
    @Log(title = "修改服务项目" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BaseServiceItem baseServiceItem) {
        return toAjax(iBaseServiceItemService.updateById(baseServiceItem) ? 1 : 0);
    }

    /**
     * 删除服务项目
     */
//    @PreAuthorize("@ss.hasPermi('base:item:remove')" )
    @Log(title = "删除服务项目" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{serviceItemIds}" )
    public AjaxResult remove(@PathVariable Long[] serviceItemIds) {
        List<Long> longs = Arrays.asList(serviceItemIds);
        for (Long id : longs) {
            QueryWrapper<BaseServiceItem> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("service_item_parent_id", id);
            List<BaseServiceItem> list = iBaseServiceItemService.list(queryWrapper);
            if (list != null && list.size() > 0) {
                return AjaxResult.error("存在子级不可删除");
            }
        }
        return toAjax(iBaseServiceItemService.removeByIds(Arrays.asList(serviceItemIds)) ? 1 : 0);
    }
}
