package com.qifu.accounting.accountBook.mapper;

import com.qifu.accounting.accountBook.domain.QuantityDetailAccount;
import com.qifu.accounting.accountBook.domain.vo.ParamsVo;
import com.qifu.accounting.accountBook.domain.vo.SubjectVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 数量金额明细账
 *  yangyang
 * @date 2020/9/10 10:09
 */
@Repository
public interface QuantityDetailAccountMapper {


    List<QuantityDetailAccount> getListByPeriod(ParamsVo params);

    List<SubjectVo> getSubjectList(ParamsVo params);

    QuantityDetailAccount selectPeriodInitBalance(ParamsVo params);

    QuantityDetailAccount monthBalanceByPeriod(ParamsVo params);

    QuantityDetailAccount yearBalanceByPeriod(ParamsVo params);
}
