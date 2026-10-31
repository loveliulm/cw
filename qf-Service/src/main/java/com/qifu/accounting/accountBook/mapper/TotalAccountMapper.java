package com.qifu.accounting.accountBook.mapper;

import com.qifu.accounting.accountBook.domain.TotalAccount;
import com.qifu.accounting.accountBook.domain.vo.ParamsVo;
import com.qifu.accounting.accountBook.domain.vo.TotalAccResult;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 *  yangyang
 * @date 2020/8/27 14:05
 */
@Repository
public interface TotalAccountMapper {
    List<TotalAccResult> getSubjectList(ParamsVo params);

    TotalAccount selectPeriodInitBalance(ParamsVo params);

    List<TotalAccount> balanceByPeriod(@Param("params") ParamsVo params, @Param("dates")List<Date> dates);
}
