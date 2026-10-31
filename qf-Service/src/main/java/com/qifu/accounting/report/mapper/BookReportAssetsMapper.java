package com.qifu.accounting.report.mapper;

import com.qifu.accounting.report.domain.BookReportAssets;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qifu.accounting.report.domain.vo.ReportAssetsParams;
import com.qifu.accounting.sys.domain.BookSubject;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * 资产负债模板Mapper接口
 *
 *  YM
 * 09-03
 */
@Repository
public interface BookReportAssetsMapper extends BaseMapper<BookReportAssets> {

    void initInsetReportAssets(@Param("bookId") Long bookId,@Param("templateId") Long templateId);

    List<BookReportAssets> getList(ReportAssetsParams params);

    BigDecimal endBalance(ReportAssetsParams paramsVo);

    BigDecimal yearInitBalance(ReportAssetsParams paramsVo);

    BookSubject getSubject(ReportAssetsParams paramsVo);

    BookReportAssets getReportAssetsTotal(Long bookId);
    BookReportAssets getReportAssets(ReportAssetsParams params);
}
