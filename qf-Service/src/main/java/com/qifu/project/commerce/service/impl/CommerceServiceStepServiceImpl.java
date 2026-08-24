package com.qifu.project.commerce.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qifu.common.utils.Asserts;
import com.qifu.common.utils.StringDateUtil;
import com.qifu.common.utils.StringUtils;
import com.qifu.framework.web.domain.AjaxResult;
import com.qifu.project.base.domain.BaseServiceProcess;
import com.qifu.project.base.service.IBaseServiceItemService;
import com.qifu.project.base.service.IBaseServiceProcessService;
import com.qifu.project.commerce.domain.CommerceService;
import com.qifu.project.commerce.domain.vo.CommerceServiceStepVo;
import com.qifu.project.commerce.service.ICommerceServiceService;
import com.qifu.project.customer.domain.TContract;
import com.qifu.project.customer.service.ITContractService;
import org.apache.velocity.runtime.directive.Foreach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qifu.project.commerce.mapper.CommerceServiceStepMapper;
import com.qifu.project.commerce.domain.CommerceServiceStep;
import com.qifu.project.commerce.service.ICommerceServiceStepService;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 工商服务步骤Service业务层处理
 *
 *  YM
 * 08-14
 */
@Service
public class CommerceServiceStepServiceImpl extends ServiceImpl<CommerceServiceStepMapper, CommerceServiceStep> implements ICommerceServiceStepService {

    @Autowired
    private IBaseServiceProcessService baseServiceProcessService;
    @Autowired
    private ICommerceServiceService commerceServiceService;
    @Autowired
    private ITContractService itContractService;


    @Transactional
    @Override
    public AjaxResult add(Long commerceServiceId) {
        //获取工商服务
        CommerceService cs = commerceServiceService.getById(commerceServiceId);
        Asserts.isTrue(StringUtils.isNotNull(cs.getOperatorUserId()),"该服务未派工");
        Asserts.isFalse(StringDateUtil.TASK_TYPE_1.equals(cs.getStatus()),"该服务已开始");

        //获取合同
        TContract tContract = itContractService.getById(cs.getContractId());
        //获取服务项目的服务流程
        List<BaseServiceProcess> bspList = baseServiceProcessService.selectServiceProcesslist(tContract.getServiceItemId());
        List<CommerceServiceStep> list = new ArrayList<>();
        CommerceServiceStep css = null;
        for(BaseServiceProcess bsp : bspList){
            css = new CommerceServiceStep();
            css.setCommerceServiceId(commerceServiceId);
            css.setStepData(bsp.getProcessData());
            css.setStepName(bsp.getServiceProcessName());
            css.setStepOrder(bsp.getNumber().intValue());
            css.setStepDescribe(bsp.getProcessDescribe());
            css.setStepOutput(bsp.getProcessOutput());
            css.setStepStatus(StringDateUtil.ACCOUNTING_STATUS_0);
            css.setOperatorUserId(cs.getOperatorUserId());
            list.add(css);
        }
        cs.setStatus(StringDateUtil.TASK_TYPE_1);
        cs.setTaskStatus(bspList.get(0).getServiceProcessName());
        Asserts.isTrue(commerceServiceService.updateById(cs),"操作失败");

        boolean b = this.saveBatch(list);
        Asserts.isTrue(b,"操作失败");

        return AjaxResult.success();
    }

    @Override
    public List<CommerceServiceStepVo> getList(CommerceServiceStep commerceServiceStep) {
        return this.baseMapper.getList(commerceServiceStep);
    }


}
