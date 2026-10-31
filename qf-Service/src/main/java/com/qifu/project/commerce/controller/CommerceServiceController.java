package com.qifu.project.commerce.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import java.util.*;

import com.qifu.common.utils.Asserts;
import com.qifu.common.utils.SecurityUtils;
import com.qifu.common.utils.StringDateUtil;
import com.qifu.common.utils.StringUtils;
import com.qifu.project.commerce.domain.vo.CommerceServiceVo;
import com.qifu.project.commerce.domain.vo.StatisticalData;
import com.qifu.project.customer.service.ITContractService;
import com.qifu.project.system.domain.SysUser;
import com.qifu.project.system.service.ISysRoleService;
import com.qifu.project.system.service.ISysUserService;
import io.netty.util.internal.StringUtil;
import io.swagger.annotations.*;
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
import com.qifu.project.commerce.domain.CommerceService;
import com.qifu.project.commerce.service.ICommerceServiceService;
import com.qifu.framework.web.controller.BaseController;
import com.qifu.framework.web.domain.AjaxResult;
import com.qifu.common.utils.poi.ExcelUtil;
import com.qifu.framework.web.page.TableDataInfo;

import javax.servlet.http.HttpServletRequest;

/**
 * 工商服务任务Controller
 *
 *  YM
 * 08-14
 */
@Api(tags = "工商服务")
@AllArgsConstructor
@RestController
@RequestMapping("/commerce/commerce")
public class CommerceServiceController extends BaseController {

    private final ICommerceServiceService iCommerceServiceService;

    @Autowired
    private ITContractService itContractService;
    @Autowired
    private ISysUserService sysUserService;

    /**
     * 查询工商服务任务列表
     */
    @PreAuthorize("@ss.hasPermi('commerce:commerce:list')")
    @GetMapping("/list")
    public TableDataInfo list(CommerceServiceVo commerceServiceVo, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        map.put("deptId", SecurityUtils.getDeptId());
        map.put("serviceTypeId", request.getParameter("serviceTypeId"));
        map.put("customerName", request.getParameter("customerName"));
        map.put("isDel", 0);
        map.put("status",request.getParameter("status"));
        map.put("serviceStatus",request.getParameter("serverStatus"));

        //判断登录用户是否公司管理员
        if(!SecurityUtils.isAdmin()){
            if(!sysUserService.selectUserIsCompanyAdmin(SecurityUtils.userId())){
                map.put("operatorUserId",SecurityUtils.userId());
            }
        }

        startPage();
        List<CommerceServiceVo> list = iCommerceServiceService.getList(map);
        return getDataTable(list);
    }

    /**
     * 导出工商服务任务列表
     */
    @PreAuthorize("@ss.hasPermi('commerce:commerce:export')")
    @Log(title = "工商服务任务", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(CommerceService commerceService,HttpServletRequest request) {
//        LambdaQueryWrapper<CommerceService> lqw = new LambdaQueryWrapper<CommerceService>(commerceService);
//        List<CommerceService> list = iCommerceServiceService.list(lqw);
        Map<String, Object> map = new HashMap<>();
        map.put("deptId", SecurityUtils.getDeptId());
        map.put("serviceTypeId", request.getParameter("serviceClassId"));
        List<CommerceServiceVo> list = iCommerceServiceService.getList(map);
        ExcelUtil<CommerceServiceVo> util = new ExcelUtil<CommerceServiceVo>(CommerceServiceVo.class);
        return util.exportExcel(list, "commerce");
    }

    /**
     * 获取工商服务任务详细信息
     */
    @PreAuthorize("@ss.hasPermi('commerce:commerce:query')")
    @GetMapping(value = "/{id}")
    @ApiOperation("获取工商服务详细信息")
    @ApiParam(name = "id", value = "工商服务id", required = true)
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(iCommerceServiceService.selectById(id));
    }

    /**
     * 新增工商服务任务
     */
    @PreAuthorize("@ss.hasPermi('commerce:commerce:add')")
    @Log(title = "工商服务任务", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CommerceService commerceService) {
        return toAjax(iCommerceServiceService.save(commerceService) ? 1 : 0);
    }

    /**
     * 修改工商服务任务
     */
    @PreAuthorize("@ss.hasPermi('commerce:commerce:edit')")
    @Log(title = "工商服务任务", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation("修改工商服务")
    public AjaxResult edit(@RequestBody CommerceService commerceService) {
        return toAjax(iCommerceServiceService.updateById(commerceService) ? 1 : 0);
    }

    /**
     * 删除工商服务任务
     */
    @PreAuthorize("@ss.hasPermi('commerce:commerce:remove')")
    @Log(title = "工商服务任务", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[ ] ids) {
        List<CommerceService> list = iCommerceServiceService.getListByIds(ids);
        long[] contractIds = new long[list.size()];
        for (int i = 0; i < list.size(); i++) {
            contractIds[i] = list.get(i).getContractId();
        }

        return toAjax(itContractService.deleteContractByIds(contractIds));
    }

    @ApiOperation("服务派工")
    @PreAuthorize("@ss.hasPermi('commerce:commerce:assign')")
    @PostMapping("/assign")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "服务id", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "operatorUserId", value = "派工人员id", required = true, dataType = "Long", paramType = "query")})
    public AjaxResult assign(CommerceService commerceService) {
        Asserts.isTrue(iCommerceServiceService.checkServiceStatus(commerceService.getId()),"服务停止状态不能派工");
        Asserts.isTrue(StringUtils.isNotNull(commerceService.getId()), "请选择服务");
        return toAjax(iCommerceServiceService.updateById(commerceService) ? 1 : 0);
    }

    @ApiOperation("获取首页工商服务统计数据")
    @GetMapping("/getIndexData")
    public AjaxResult getIndexData(String type){
        Map<String,Object> map = new HashMap<String,Object>();
        if(!SecurityUtils.isAdmin()){
            map.put("deptId",SecurityUtils.getDeptId());
        }
        if(StringUtils.isEmpty(type)){
            map.put("type","month");
        }else{
            map.put("type",type);
        }
        StatisticalData statisticalData = iCommerceServiceService.getIndexData(map);
        return AjaxResult.success(statisticalData);
    }

    @ApiOperation("停止服务")
    @PreAuthorize("@ss.hasPermi('commerce:commerce:stopService')")
    @GetMapping("/stopService/{id}")
    public AjaxResult stopService(@PathVariable Long id){
        Asserts.isNull(id,"请选择要停止的服务");
        Asserts.isTrue(iCommerceServiceService.checkServiceStatus(id),"所选服务已停止");
        CommerceService commerceService = new CommerceService();
        commerceService.setId(id);
        commerceService.setServiceStatus(StringDateUtil.COMMON_STATUS_2);
        return toAjax(iCommerceServiceService.updateById(commerceService) ? 1 : 0);
    }

    @ApiOperation("恢复服务")
    @PreAuthorize("@ss.hasPermi('commerce:commerce:restoreService')")
    @GetMapping("/restoreService/{id}")
    public AjaxResult restoreService(@PathVariable Long id){
        Asserts.isNull(id,"请选择要恢复的服务");
        Asserts.isFalse(iCommerceServiceService.checkServiceStatus(id),"所选服务不是停止状态");
        CommerceService commerceService = new CommerceService();
        commerceService.setId(id);
        commerceService.setServiceStatus(StringDateUtil.COMMON_STATUS_0);
        return toAjax(iCommerceServiceService.updateById(commerceService) ? 1 : 0);
    }
}
