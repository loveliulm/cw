package com.qifu.accounting.accountBook.service;

import com.qifu.accounting.accountBook.domain.AuxiliaryDetailAccount;
import com.qifu.accounting.accountBook.domain.AuxiliaryTotalAccount;
import com.qifu.accounting.accountBook.domain.BookBalance;
import com.qifu.accounting.accountBook.domain.vo.AuxiliaryAccParamsVo;
import com.qifu.accounting.accountBook.domain.vo.ParamsVo;
import com.qifu.accounting.accountBook.domain.vo.SubjectVo;

import java.util.List;

/**
 *  yangyang
 * @date 2020/9/9 9:37
 */
public interface AuxiliaryTotalAccountService {
    List<SubjectVo> getSubjectList(AuxiliaryAccParamsVo params);

    List<AuxiliaryTotalAccount> getList(AuxiliaryAccParamsVo params);

    AuxiliaryTotalAccount initBalance(AuxiliaryAccParamsVo params);

    AuxiliaryTotalAccount totalPeriod(AuxiliaryAccParamsVo params);

    AuxiliaryTotalAccount totalYear(AuxiliaryAccParamsVo params);

}
