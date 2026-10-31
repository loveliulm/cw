package com.qifu.project.customer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.qifu.common.utils.StringDateUtil;
import com.qifu.project.commerce.domain.CommerceService;
import com.qifu.project.commerce.service.ICommerceServiceService;
import com.qifu.project.customer.domain.TCollection;
import com.qifu.project.customer.domain.TCustomer;
import com.qifu.project.customer.service.ITCollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qifu.project.customer.mapper.TContractMapper;
import com.qifu.project.customer.domain.TContract;
import com.qifu.project.customer.service.ITContractService;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 【请填写功能名称】Service业务层处理
 *
 *  YM
 * 06-18
 */
@Service
public class TContractServiceImpl extends ServiceImpl<TContractMapper, TContract> implements ITContractService {
    @Autowired
    private TContractMapper contractMapper;
    @Autowired
    private ITCollectionService itCollectionService;
    @Autowired
    private ICommerceServiceService commerceServiceService;

    public TContract findFirstByDeptId(Long deptId){
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.HOUR_OF_DAY,0);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.MILLISECOND,0);
        Date curTime = cal.getTime();
        LambdaQueryWrapper<TContract> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TContract::getDeptId, deptId)
                .ge(TContract::getCreateTime,curTime)
                .orderByDesc(TContract::getCreateTime)
                .last("limit 1");
        TContract contract = contractMapper.selectOne(wrapper);
        return contract;
    }

    @Transactional
    public int deleteContractByIds(long[] ids){
        //删除收款
        itCollectionService.deleteBycontractIds(ids);

        //删除工商服务
        commerceServiceService.deleteBycontractIds(ids);

        return contractMapper.deleteContractByIds(ids);
    }

    public List<TContract> selectContract(TContract tContract){
        return contractMapper.selectContract(tContract);
    }
}

