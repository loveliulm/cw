package com.qifu.accounting.accountBook.mapper;

import com.qifu.accounting.accountBook.domain.AuxiliaryTotalAccount;
import com.qifu.accounting.accountBook.domain.vo.AuxiliaryAccParamsVo;
import com.qifu.accounting.accountBook.domain.vo.SubjectVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *  yangyang
 * @date 2020/9/9 9:38
 */
@Repository
public interface AuxiliaryTotalAccountMapper {
    List<SubjectVo> getSubjectList(AuxiliaryAccParamsVo params);

    List<AuxiliaryTotalAccount> getDetailList(AuxiliaryAccParamsVo params);

    AuxiliaryTotalAccount initBalance(AuxiliaryAccParamsVo params);

    AuxiliaryTotalAccount totalPeriod(AuxiliaryAccParamsVo params);

    AuxiliaryTotalAccount totalYear(AuxiliaryAccParamsVo params);

}
