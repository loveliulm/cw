package com.qifu.project.customer.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import java.util.HashMap;
import java.util.List;
import java.util.Arrays;
import java.util.Map;

import com.qifu.common.utils.Asserts;
import com.qifu.common.utils.SecurityUtils;
import com.qifu.common.utils.StringDateUtil;
import com.qifu.common.utils.StringUtils;
import com.qifu.project.customer.domain.TCollectionRecord;
import com.qifu.project.customer.domain.TContract;
import com.qifu.project.customer.domain.vo.CollectionVo;
import com.qifu.project.customer.service.ITCollectionRecordService;
import com.qifu.project.customer.service.ITContractService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
import com.qifu.project.customer.domain.TCollection;
import com.qifu.project.customer.service.ITCollectionService;
import com.qifu.framework.web.controller.BaseController;
import com.qifu.framework.web.domain.AjaxResult;
import com.qifu.common.utils.poi.ExcelUtil;
import com.qifu.framework.web.page.TableDataInfo;

/**
 * 收款Controller
 *
 *  YM
 * 06-28
 */
@Api(tags = "收费管理")
@AllArgsConstructor
@RestController
@RequestMapping("/customer/collection" )
public class TCollectionController extends BaseController {

    private final ITCollectionService iTCollectionService;

    @Autowired
    private ITCollectionRecordService itCollectionRecordService;
    @Autowired
    private ITContractService itContractService;

    /**
     * 查询收款列表
     */
    @ApiOperation("收费管理-工商/代账服务收款-列表查询")
    @PreAuthorize("@ss.hasPermi('customer:collection:list')")
    @GetMapping("/list")
    public TableDataInfo list(CollectionVo collectionVo)
    {
        collectionVo.setDeptId(SecurityUtils.companyId());
        collectionVo.setIsDel(StringDateUtil.Del_FLAG_0);
        startPage();
        List<CollectionVo> list = iTCollectionService.collectionList(collectionVo);
        return getDataTable(list);
    }

    /**
     * 导出收款列表
     */
    @PreAuthorize("@ss.hasPermi('customer:collection:export')" )
    @Log(title = "收款" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(CollectionVo collectionVo) {
//        LambdaQueryWrapper<TCollection> lqw = new LambdaQueryWrapper<TCollection>(tCollection);
//        List<TCollection> list = iTCollectionService.list(lqw);
        collectionVo.setDeptId(SecurityUtils.companyId());
        collectionVo.setIsDel(StringDateUtil.Del_FLAG_0);
        List<CollectionVo> list = iTCollectionService.collectionList(collectionVo);
        ExcelUtil<CollectionVo> util = new ExcelUtil<CollectionVo>(CollectionVo. class);
        return util.exportExcel(list, "collection" );
    }

    /**
     * 获取收款详细信息
     */
    @PreAuthorize("@ss.hasPermi('customer:collection:query')" )
    @GetMapping(value = "/{id}" )
    public AjaxResult getInfo(@PathVariable("id" ) Long id) {
        Map<String,Object> map = new HashMap<>();
        //获取收款
        TCollection tCollection = iTCollectionService.selectById(id);
        Asserts.isNull(tCollection, "找不到该条记录，请刷新后重试！");
        //合同
        TContract contract = itContractService.getById(tCollection.getContractId());

        //获取收款明细
        LambdaQueryWrapper<TCollectionRecord> lqw = new LambdaQueryWrapper<TCollectionRecord>();
        lqw.eq(TCollectionRecord::getCollectionId,id).orderByAsc(TCollectionRecord::getId);
        List<TCollectionRecord> list = itCollectionRecordService.list(lqw);

        map.put("tCollection",tCollection);
        map.put("TCollectionRecords",list);
        map.put("contract",contract);
        if(list.size() > 0){
            TCollectionRecord tc = list.get(list.size()-1);
            map.put("beginTime",tc.getEndDate());
        }else{
            map.put("beginTime",contract.getContractStartDate());
        }

        return AjaxResult.success(map);
    }


    /**
     * 修改收款
     */
    @PreAuthorize("@ss.hasPermi('customer:collection:edit')" )
    @Log(title = "收款" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TCollection tCollection) {
        return toAjax(iTCollectionService.updateById(tCollection) ? 1 : 0);
    }

    /**
     * 删除收款
     */
    @PreAuthorize("@ss.hasPermi('system:collection:remove')" )
    @Log(title = "收款" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}" )
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(iTCollectionService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }
}
