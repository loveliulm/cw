package com.qifu.accounting.report.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qifu.accounting.report.domain.BookReportCash;
import com.qifu.accounting.report.domain.vo.ReportCashParams;

/**
 * 现金流量Mapper接口
 *
 *  YM
 * 09-11
 */
@Repository
public interface BookReportCashMapper extends BaseMapper<BookReportCash> {

    List<BookReportCash> getList(ReportCashParams params);

    BigDecimal endBalance(ReportCashParams paramsVo);

    BigDecimal getSubjectInitHappen(ReportCashParams paramsVo);

    BigDecimal getHappenBalance(ReportCashParams params);

    BigDecimal getYearHappenBalance(ReportCashParams params);

    BookReportCash getCashByTotal(Long bookId);

    BookReportCash getCash(@Param("bookId") Long bookId, @Param("rowNum") int row);
}
