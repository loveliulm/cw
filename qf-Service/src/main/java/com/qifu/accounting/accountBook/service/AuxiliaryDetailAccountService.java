package com.qifu.accounting.accountBook.service;

import com.qifu.accounting.accountBook.domain.AuxiliaryDetailAccount;
import com.qifu.accounting.accountBook.domain.BookBalance;
import com.qifu.accounting.accountBook.domain.vo.*;

import java.util.List;

/**
 * 辅助核算明细账
 *  yangyang
 * @date 2020/9/8 10:41
 */
public interface AuxiliaryDetailAccountService {
    List<SubjectVo> getSubjectList(AuxiliaryAccParamsVo params);

    List<AuxiliaryTypeVo> getAuxiliaryTypeList(Long subjectId);

    List<AuxiliaryVo> getAuxiliary(AuxiliaryAccParamsVo params);

    List<AuxiliaryDetailAccount> getList(AuxiliaryAccParamsVo params);

    AuxiliaryDetailAccount initBalanceByPeriod(AuxiliaryAccParamsVo params);

    AuxiliaryDetailAccount totalPeriod(AuxiliaryAccParamsVo params);

    AuxiliaryDetailAccount totalYear(AuxiliaryAccParamsVo params);

}
