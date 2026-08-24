package com.qifu.accounting.wage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qifu.accounting.wage.domain.BookWage;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.awt.print.Book;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 凭证明细Mapper接口
 *
 *  YM
 * 07-13
 */
@Repository
public interface BookWageMapper extends BaseMapper<BookWage> {

    BookWage getTotalBalanceByWageTotal(@Param("yearPeriod") Date yearPeriod, @Param("bookId") Long bookId);

    List<BookWage> getList(BookWage bookWage);

    BookWage getWageById(Long id);
}
