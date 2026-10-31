package com.qifu.project.customer.service;

import com.qifu.project.customer.domain.TCollection;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qifu.project.customer.domain.vo.CollectionVo;

import java.util.List;

/**
 * 收款Service接口
 *
 *  YM
 * 06-28
 */
public interface ITCollectionService extends IService<TCollection> {
    List<CollectionVo> collectionList(CollectionVo collectionVo);

    /**
     * 根据合同ids删除收款
     * @param contractIds
     * @return
     */
    int deleteBycontractIds(long[] contractIds);

    /**
     * 根据id查询
     */
    TCollection selectById(Long id);
}
