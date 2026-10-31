package com.qifu.project.customer.mapper;

import com.qifu.project.customer.domain.TCollection;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qifu.project.customer.domain.vo.CollectionVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 收款Mapper接口
 *
 *  YM
 * 08-28
 */
@Repository
public interface TCollectionMapper extends BaseMapper<TCollection> {

    List<CollectionVo> collectionList(CollectionVo collectionVo);

    /**
     * 根据合同ids删除收款
     * @param contractIds
     * @return
     */
    int deleteBycontractIds(long[] contractIds);

    TCollection selectById(@Param("id") Long id);
}
