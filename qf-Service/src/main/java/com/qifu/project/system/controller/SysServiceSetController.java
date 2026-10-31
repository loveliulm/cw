package com.qifu.project.system.controller;

import com.qifu.framework.web.controller.BaseController;
import com.qifu.framework.web.page.TableDataInfo;
import com.qifu.project.base.domain.BaseServiceProcess;
import com.qifu.project.base.domain.vo.*;
import com.qifu.project.base.service.IBaseServiceItemService;
import com.qifu.project.base.service.IBaseServiceProcessService;
import com.qifu.project.base.service.IBaseServiceTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *  yangyang
 * 服务设置
 * @date 2020/6/23 16:14
 */
@Api(tags = "服务设置")
@RestController
@RequestMapping("/system/service")
public class SysServiceSetController extends BaseController {
    @Autowired
    private IBaseServiceTypeService serviceTypeService;
    @Autowired
    private IBaseServiceProcessService serviceProcessService;
    @Autowired
    private IBaseServiceItemService serviceItemService;

    /**
     * 工商服务类型（包含项目）
     */
    @ApiOperation("工商服务类型-左侧树（包含项目）")
    @GetMapping("/type/tree")
    public TableDataInfo selectServiceTypelistName()
    {
        List<ServiceClassVo> list = serviceTypeService.selectServiceTypelistName();
        return getDataTable(list);
    }

    /**
     * 工商服务类型（包含项目）
     */
    @ApiOperation("工商服务类型（包含项目）")
    @GetMapping("/item")
    public TableDataInfo selectServiceTypelistName(Long serviceTypeId)
    {
        List<ServiceItemVo> serviceItemVos = serviceItemService.serviceTypeItemList(serviceTypeId);
        return getDataTable(serviceItemVos);
    }

    /**
     * 工商服务流程-左侧树形（包含项目）
     */
    @ApiOperation("工商服务流程-左侧树形（包含项目）")
    @GetMapping("/type")
    public TableDataInfo selectServiceTypelist()
    {
        List<BaseServiceTypeVo> list = serviceTypeService.selectServiceTypelist();
        return getDataTable(list);
    }

    /**
     * 工商服务流程（资料类容）
     */
    @ApiOperation("工商服务流程（资料类容）")
    @GetMapping("/process")
    public TableDataInfo selectServiceProcesslist(Long serviceItemId)
    {
        List<BaseServiceProcess> list = serviceProcessService.selectServiceProcesslist(serviceItemId);
        return getDataTable(list);
    }
}
