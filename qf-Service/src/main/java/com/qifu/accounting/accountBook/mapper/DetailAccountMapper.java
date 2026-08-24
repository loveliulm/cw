package com.qifu.accounting.accountBook.mapper;

import com.qifu.accounting.accountBook.domain.DetailAccount;
import com.qifu.accounting.accountBook.domain.vo.ParamsVo;
import com.qifu.accounting.accountBook.domain.vo.SubjectVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 凭证明细Mapper接口
 *
 *  YM
 * 07-13
 */
@Repository
public interface DetailAccountMapper {

    List<DetailAccount> getListByPeriod(ParamsVo params);

    List<SubjectVo> getSubjectList(ParamsVo params);

    DetailAccount selectPeriodInitBalance(ParamsVo params);

    DetailAccount monthBalanceByPeriod(ParamsVo params);

    DetailAccount yearBalanceByPeriod(ParamsVo params);

}
