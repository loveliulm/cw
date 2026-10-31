package com.qifu.framework.task;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qifu.common.utils.DateUtils;
import com.qifu.common.utils.StringDateUtil;
import com.qifu.project.common.enums.CollectionStatusEnum;
import com.qifu.project.customer.domain.TContract;
import com.qifu.project.customer.mapper.TContractMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 *  yangyang
 * @date 2020/6/29 10:21
 */
@Component("contractStatusTask")
public class ContractStatusTask {
    @Autowired
    private TContractMapper contractMapper;

    /**
     * 每天執檢查合同表中結束日期是否到期更新合同狀態。
     * 1个月内属于即将到期
     */
    public void updateStatus(){
        LambdaQueryWrapper<TContract> lqw = new LambdaQueryWrapper<>();
        lqw.eq(TContract::getDelFlag, StringDateUtil.Del_FLAG_0);
        lqw.ne(TContract::getStatus, CollectionStatusEnum.已到期);
        List<TContract> list = contractMapper.selectList(lqw);

        list.forEach(tContract -> {
            //Date1.before(Date2)，当Date1小于Date2时，返回TRUE，当大于等于时，返回false；
            if(tContract.getContractEndDate().before(new Date())){
                tContract.setStatus(CollectionStatusEnum.已到期.name());
            }else{
                if(DateUtils.is30Days(tContract.getContractEndDate(),new Date())){
                    tContract.setStatus(CollectionStatusEnum.未到期.name());
                }else{
                    tContract.setStatus(CollectionStatusEnum.即将到期.name());
                }
            }
            tContract.setUpdateTime(new Date());
            contractMapper.updateStatusByEndDate(tContract);
        });

    }
}
