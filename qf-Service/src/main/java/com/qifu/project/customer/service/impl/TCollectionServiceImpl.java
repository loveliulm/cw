package com.qifu.project.customer.service.impl;

import com.qifu.project.customer.domain.vo.CollectionVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qifu.project.customer.mapper.TCollectionMapper;
import com.qifu.project.customer.domain.TCollection;
import com.qifu.project.customer.service.ITCollectionService;

import java.util.List;

/**
 * 收款Service业务层处理
 *
 *  YM
 * 06-28
 */
@Service
public class TCollectionServiceImpl extends ServiceImpl<TCollectionMapper, TCollection> implements ITCollectionService {
    @Autowired
    private TCollectionMapper collectionMapper;

    public List<CollectionVo> collectionList(CollectionVo collectionVo){
        return collectionMapper.collectionList(collectionVo);
    }

    @Override
    public int deleteBycontractIds(long[] contractIds) {
        return collectionMapper.deleteBycontractIds(contractIds);
    }

    @Override
    public TCollection selectById(Long id) {
        return collectionMapper.selectById(id);
    }

}
