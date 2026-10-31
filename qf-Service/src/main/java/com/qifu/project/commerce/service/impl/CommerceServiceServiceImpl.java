package com.qifu.project.commerce.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qifu.common.utils.Asserts;
import com.qifu.common.utils.StringDateUtil;
import com.qifu.project.commerce.domain.CommerceServiceStep;
import com.qifu.project.commerce.domain.vo.CommerceServiceVo;
import com.qifu.project.commerce.domain.vo.StatisticalData;
import com.qifu.project.commerce.service.ICommerceServiceStepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qifu.project.commerce.mapper.CommerceServiceMapper;
import com.qifu.project.commerce.domain.CommerceService;
import com.qifu.project.commerce.service.ICommerceServiceService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 工商服务任务Service业务层处理
 *
 *  YM
 * 08-14
 */
@Service
public class CommerceServiceServiceImpl extends ServiceImpl<CommerceServiceMapper, CommerceService> implements ICommerceServiceService {

    @Autowired
    private CommerceServiceMapper commerceServiceMapper;
    @Autowired
    private ICommerceServiceStepService commerceServiceStepService;

    @Override
    public List<CommerceServiceVo> getList(Map<String, Object> map) {
        List<CommerceServiceVo> list = commerceServiceMapper.getList(map);
        if (list != null && list.size() > 0) {
            for (CommerceServiceVo commerceServiceVo : list) {
                LambdaQueryWrapper<CommerceServiceStep> lqw = new LambdaQueryWrapper();
                lqw.eq(CommerceServiceStep::getCommerceServiceId, commerceServiceVo.getId());
//                lqw.eq(CommerceServiceStep::getStepStatus, 1);
                List<CommerceServiceStep> stepList = commerceServiceStepService.list(lqw);
                commerceServiceVo.setServiceStepList(stepList);
                commerceServiceVo.setNowStep(stepList.stream().filter(info -> info.getStepStatus() == 1).collect(Collectors.toList()).size());
            }
        }

        return list;
    }

    @Override
    public int deleteBycontractIds(long[] contractIds) {
        return commerceServiceMapper.deleteBycontractIds(contractIds);
    }

    @Override
    public CommerceServiceVo selectById(Long id) {
        return commerceServiceMapper.selectById(id);
    }

    @Override
    public List<CommerceService> getListByIds(Long[] ids) {
        return commerceServiceMapper.getListByIds(ids);
    }

    /**
     * 检查服务是否完成
     * @param id
     */
    @Override
    public void checkEnd(long id){
        LambdaQueryWrapper<CommerceServiceStep> lqw = new LambdaQueryWrapper();
        lqw.eq(CommerceServiceStep::getCommerceServiceId,id);
        List<CommerceServiceStep> stepList = commerceServiceStepService.list(lqw);
        List<CommerceServiceStep> stepList2 = stepList.stream().filter(s -> s.getStepStatus() == 0).collect(Collectors.toList());
        if(stepList2.size() == 0){
            CommerceService cs = new CommerceService();
            cs.setId(id);
            cs.setStatus(StringDateUtil.TASK_TYPE_2);
            int i = commerceServiceMapper.updateById(cs);
        }
    }

    @Override
    public StatisticalData getIndexData(Map<String, Object> map) {
        return commerceServiceMapper.getIndexData(map);
    }

    @Override
    public boolean checkServiceStatus(Long id) {
        CommerceService commerceService = commerceServiceMapper.selectById(id);
        if (commerceService.getServiceStatus() == StringDateUtil.COMMON_STATUS_2) {
            return false;
        }
        return true;
    }
}
