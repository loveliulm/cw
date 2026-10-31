package com.qifu.accounting.accountBook.service;

import com.qifu.accounting.accountBook.domain.QuantityDetailAccount;
import com.qifu.accounting.accountBook.domain.vo.ParamsVo;
import com.qifu.accounting.accountBook.domain.vo.SubjectVo;

import java.util.List;

/**
 * 数量金额明细账
 *  yangyang
 * @date 2020/9/10 10:08
 */
public interface QuantityDetailAccountService {

    List<QuantityDetailAccount> getList(ParamsVo params);

    List<SubjectVo> getSubjectList(ParamsVo params);

    QuantityDetailAccount startBalanceByPeriod(ParamsVo params);

    QuantityDetailAccount monthBalanceByPeriod(ParamsVo params);

    QuantityDetailAccount yearBalanceByPeriod(ParamsVo params);
}
