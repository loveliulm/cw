package com.qifu.accounting.sys.mapper;

import com.qifu.accounting.accountBook.domain.vo.ParamsVo;
import com.qifu.accounting.sys.domain.BookPeriod;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * 账期Mapper接口
 *
 *  YM
 * 07-17
 */
@Repository
public interface BookPeriodMapper extends BaseMapper<BookPeriod> {

    BookPeriod selectByPeriod(@Param("bookId")Long bookId,@Param("period") Date previousPeriod);

    void updatePeriod(BookPeriod currentPeriod);

    BookPeriod getPeriodFirst(Long bookId);

    List<BookPeriod> getList(BookPeriod bookPeriod);
}
