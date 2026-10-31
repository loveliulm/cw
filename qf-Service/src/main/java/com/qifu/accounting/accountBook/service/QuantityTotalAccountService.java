package com.qifu.accounting.accountBook.service;

import com.qifu.accounting.accountBook.domain.QuantityTotalAccount;
import com.qifu.accounting.accountBook.domain.vo.ParamsVo;

import java.util.List;

/**
 * 数量金额总账
 *  yangyang
 * @date 2020/9/10 14:46
 */
public interface QuantityTotalAccountService {

    List<QuantityTotalAccount> getList(ParamsVo params);

    QuantityTotalAccount startBalanceByPeriod(ParamsVo params);

    QuantityTotalAccount monthBalanceByPeriod(ParamsVo params);

    QuantityTotalAccount yearBalanceByPeriod(ParamsVo params);
}
