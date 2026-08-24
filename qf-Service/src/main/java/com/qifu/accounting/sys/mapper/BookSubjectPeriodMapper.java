package com.qifu.accounting.sys.mapper;

import com.qifu.accounting.sys.domain.BookSubjectPeriod;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 科目账期期末余额Mapper接口
 *
 *  YM
 * 08-17
 */
@Repository
public interface BookSubjectPeriodMapper extends BaseMapper<BookSubjectPeriod> {

    BookSubjectPeriod selectByPeriod(@Param("subjectId") Long subjectId,@Param("period") Date period);

    void insertListByPeriod(@Param("bookId")Long bookId,@Param("period") Date period,@Param("previousPeriod")Date previousPeriod);

    void updateBalanceByPeriod(@Param("subjectId")Long subjectId, @Param("diffMoney")BigDecimal diffMoney,@Param("yearPeriod")Date yearPeriod);

    void updateBalanceByAfterPeriod(@Param("subjectId")Long subjectId, @Param("diffMoney")BigDecimal diffMoney,@Param("yearPeriod")Date yearPeriod);


    List<BookSubjectPeriod> selectListPeriodByBookId(@Param("yearPeriod") Date yearPeriod,@Param("subjectId")Long subjectId);
}
