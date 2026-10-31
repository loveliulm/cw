package com.qifu.accounting.carryover.mapper;

import com.qifu.accounting.carryover.domain.BookCarryoverModel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 结转模板Mapper接口
 *
 *  YM
 * 07-23
 */
@Repository
public interface BookCarryoverModelMapper extends BaseMapper<BookCarryoverModel> {

    List<BookCarryoverModel> getListByTemplate(Long accountTemplateId);
}
