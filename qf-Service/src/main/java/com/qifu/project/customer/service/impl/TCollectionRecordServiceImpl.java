package com.qifu.project.customer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qifu.common.utils.Asserts;
import com.qifu.common.utils.DateUtils;
import com.qifu.framework.web.domain.AjaxResult;
import com.qifu.project.common.enums.IsWarningEnum;
import com.qifu.project.customer.domain.TCollection;
import com.qifu.project.customer.domain.TContract;
import com.qifu.project.customer.service.ITCollectionService;
import com.qifu.project.customer.service.ITContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qifu.project.customer.mapper.TCollectionRecordMapper;
import com.qifu.project.customer.domain.TCollectionRecord;
import com.qifu.project.customer.service.ITCollectionRecordService;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * 收款明细Service业务层处理
 *
 *  YM
 * 06-28
 */
@Service
public class TCollectionRecordServiceImpl extends ServiceImpl<TCollectionRecordMapper, TCollectionRecord> implements ITCollectionRecordService {

    @Autowired
    private TCollectionRecordMapper tCollectionRecordMapper;
    @Autowired
    private ITCollectionService itCollectionService;
    @Autowired
    private ITContractService itContractService;

    @Override
    @Transactional
    public AjaxResult deleteById(Long id) {
        TCollectionRecord tCollectionRecord = tCollectionRecordMapper.selectById(id);
        Asserts.isNull(tCollectionRecord,"该收款记录不存在或已经失效，操作失败");
        //获取收款
        TCollection tCollection = itCollectionService.selectById(tCollectionRecord.getCollectionId());
        tCollection.setAmount(tCollection.getAmount().subtract(tCollectionRecord.getCollectionAmount()));
        tCollection.setDebt(tCollection.getDebt().add(tCollectionRecord.getCollectionAmount()));
        if(BigDecimal.ZERO.compareTo(tCollection.getDebt()) < 1){
            tCollection.setIsWarning(IsWarningEnum.已经欠费);
            Integer isEnd = 0;
            tCollection.setIsEnd(isEnd);
        }
        LambdaQueryWrapper<TCollectionRecord> lqw = new LambdaQueryWrapper<TCollectionRecord>();
        lqw.eq(TCollectionRecord::getCollectionId,tCollection.getId());
        lqw.notIn(TCollectionRecord::getId,id);
        lqw.orderByDesc(TCollectionRecord::getEndDate);
        lqw.last("limit 1");
        TCollectionRecord tcr = tCollectionRecordMapper.selectOne(lqw);
        //重新设置最后收款日期
        if(tcr != null){
            tCollection.setLastPayDate(tcr.getEndDate());
        }else{
            TContract tContract = itContractService.getById(tCollection.getContractId());
            tCollection.setLastPayDate(tContract.getContractStartDate());
        }

        //更新收款
        boolean b = itCollectionService.updateById(tCollection);
        Asserts.isTrue(b,"操作失败");

        int s = tCollectionRecordMapper.deleteById(id);
        Asserts.isTrue(s > 0 ,"删除失败");
        return AjaxResult.success();
    }
}
