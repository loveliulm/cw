package com.qifu.accounting.report.mapper;

import com.qifu.accounting.report.domain.vo.FirstPageFinancial;
import com.qifu.accounting.report.domain.vo.FirstPageParams;
import com.qifu.accounting.report.domain.vo.FirstPageTax;
import com.qifu.accounting.sys.domain.BookConfig;
import com.qifu.accounting.sys.domain.BookPeriod;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 *  yangyang
 * @date 2020/9/22 14:49
 */
@Repository
public interface FirstPageMapper {
    BigDecimal subjectDebitTotalEndBalance(FirstPageParams params);

    BigDecimal voucherDebitTotalBalance(FirstPageParams params);

    FirstPageTax taxInfoByPeriod(@Param("bookPeriod") BookPeriod bookPeriod, @Param("configs") List<BookConfig> configs);

    List<FirstPageFinancial> subjectInfo(@Param("params")FirstPageParams params, @Param("configs")List<BookConfig> configs);
}
