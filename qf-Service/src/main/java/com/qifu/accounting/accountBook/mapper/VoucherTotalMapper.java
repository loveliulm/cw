package com.qifu.accounting.accountBook.mapper;

import com.qifu.accounting.accountBook.domain.VoucherTotal;
import com.qifu.accounting.accountBook.domain.vo.ParamsVo;
import com.qifu.accounting.accountBook.domain.vo.SubjectVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *  yangyang
 * @date 2020/7/31 10:31
 */
@Repository
public interface VoucherTotalMapper {
    VoucherTotal voucherBalance(@Param("params") ParamsVo paramsVo,@Param("subjectId") Long subjectId);

    List<VoucherTotal> getSubjectList(ParamsVo paramsVo);
}
