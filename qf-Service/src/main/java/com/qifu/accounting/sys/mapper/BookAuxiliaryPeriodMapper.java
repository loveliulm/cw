package com.qifu.accounting.sys.mapper;

import com.qifu.accounting.sys.domain.BookAuxiliaryPeriod;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 辅助核算月末余额Mapper接口
 *
 *  YM
 * 09-07
 */
@Repository
public interface BookAuxiliaryPeriodMapper extends BaseMapper<BookAuxiliaryPeriod> {

    void insertList(@Param("bookId")Long bookId, @Param("yearPeriod") Date period, @Param("previousPeriod")Date previousPeriod);

    void updateBalanceByPeriod(@Param("subjectId")Long subjectId, @Param("diffMoney") BigDecimal diffMoney, @Param("yearPeriod")Date yearPeriod, @Param("auxiliaryId")Long auxiliaryId);

    void updateBalanceByAfterPeriod(@Param("subjectId")Long subjectId, @Param("diffMoney")BigDecimal diffMoney,@Param("yearPeriod")Date yearPeriod,@Param("auxiliaryId")Long auxiliaryId);

}
