package com.qifu.accounting.sys.mapper;

import com.qifu.accounting.sys.domain.BookAuxiliary;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qifu.accounting.sys.domain.vo.BookAuxiliaryVo;
import org.springframework.stereotype.Repository;

/**
 * 辅组核算Mapper接口
 *
 *  YM
 * 07-09
 */
@Repository
public interface BookAuxiliaryMapper extends BaseMapper<BookAuxiliary> {

    BookAuxiliaryVo getAuxiliary(Long auxiliaryId);
}
