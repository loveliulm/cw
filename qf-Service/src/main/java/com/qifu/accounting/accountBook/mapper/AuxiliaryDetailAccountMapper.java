package com.qifu.accounting.accountBook.mapper;

import com.qifu.accounting.accountBook.domain.AuxiliaryDetailAccount;
import com.qifu.accounting.accountBook.domain.vo.AuxiliaryAccParamsVo;
import com.qifu.accounting.accountBook.domain.vo.AuxiliaryVo;
import com.qifu.accounting.accountBook.domain.vo.SubjectVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *  yangyang
 * 辅助核算明细账
 * @date 2020/9/8 10:41
 */
@Repository
public interface AuxiliaryDetailAccountMapper {
    List<SubjectVo> getSubjectList(AuxiliaryAccParamsVo params);

    List<AuxiliaryVo> getAuxiliary(AuxiliaryAccParamsVo params);

    List<AuxiliaryDetailAccount> getDetailList(AuxiliaryAccParamsVo params);

    AuxiliaryDetailAccount selectPeriodInitBalance(AuxiliaryAccParamsVo params);

    AuxiliaryDetailAccount totalPeriod(AuxiliaryAccParamsVo params);

    AuxiliaryDetailAccount totalYear(AuxiliaryAccParamsVo params);

}
