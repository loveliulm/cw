package com.qifu.accounting.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qifu.accounting.sys.domain.BookSubjectPeriod;
import com.qifu.accounting.sys.domain.BookSubjectPeriodTemporary;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 科目账期余额临时表
 *  yangyang
 * @date 2020/9/25 9:20
 */
@Repository
public interface BookSubjectPeriodTemporaryMapper extends BaseMapper<BookSubjectPeriodTemporary> {

    BookSubjectPeriodTemporary selectByPeriod(@Param("subjectId") Long subjectId,@Param("period") Date period);

    void insertList(@Param("bookId") Long bookId, @Param("period") Date period);

    void deletes(@Param("bookId")Long bookId,@Param("period") Date nextDate);

    void updateBalanceByAfterPeriod(@Param("subjectId")Long subjectId, @Param("diffMoney") BigDecimal diffMoney, @Param("yearPeriod")Date yearPeriod);
}
