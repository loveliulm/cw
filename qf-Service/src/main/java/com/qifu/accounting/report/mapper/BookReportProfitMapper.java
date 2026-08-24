package com.qifu.accounting.report.mapper;

import com.qifu.accounting.report.domain.BookReportProfit;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qifu.accounting.report.domain.vo.ReportAssetsParams;
import com.qifu.accounting.report.domain.vo.ReportProfitParams;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * 利润Mapper接口
 *
 *  YM
 * 09-11
 */
@Repository
public interface BookReportProfitMapper extends BaseMapper<BookReportProfit> {

    List<BookReportProfit> getList(ReportProfitParams params);

    BigDecimal endBalance(ReportProfitParams paramsVo);

    BigDecimal getSubjectInitHappen(ReportProfitParams paramsVo);

    BigDecimal getHappenBalance(ReportProfitParams params);

    BigDecimal getYearHappenBalance(ReportProfitParams params);

    BookReportProfit getProfitByTotal(Long bookId);

    BookReportProfit getProfit(@Param("bookId") Long bookId, @Param("rowNum") int row);
}
