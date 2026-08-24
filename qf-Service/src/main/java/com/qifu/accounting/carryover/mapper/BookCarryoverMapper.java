package com.qifu.accounting.carryover.mapper;

import com.qifu.accounting.carryover.domain.BookCarryover;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 结转Mapper接口
 *
 *  YM
 * 07-23
 */
@Repository
public interface BookCarryoverMapper extends BaseMapper<BookCarryover> {

    List<BookCarryover> getList(@Param("bookId")Long bookId,@Param("month") int month);

}
