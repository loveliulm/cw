package com.qifu.accounting.accountBook.service;

import com.qifu.accounting.accountBook.domain.TotalAccount;
import com.qifu.accounting.accountBook.domain.vo.ParamsVo;
import com.qifu.accounting.accountBook.domain.vo.TotalAccResult;

import java.util.List;

/**
 *  yangyang
 * @date 2020/8/27 14:06
 */
public interface TotalAccountService {
    List<TotalAccResult> getListAll(ParamsVo params);

    TotalAccount startBalanceByPeriod(ParamsVo params);
}
