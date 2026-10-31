package com.qifu.accounting.accountBook.service;

import com.qifu.accounting.accountBook.domain.DetailAccount;
import com.qifu.accounting.accountBook.domain.vo.ParamsVo;
import com.qifu.accounting.accountBook.domain.vo.SubjectVo;

import java.util.List;

/**
 *  yangyang
 * @date 2020/7/20 15:32
 */
public interface DetailAccountService {
    List<DetailAccount> getList(ParamsVo params);

    List<SubjectVo> getSubjectList(ParamsVo params);

    DetailAccount startBalanceByPeriod(ParamsVo params);

    DetailAccount monthBalanceByPeriod(ParamsVo params);

    DetailAccount yearBalanceByPeriod(ParamsVo params);
}
