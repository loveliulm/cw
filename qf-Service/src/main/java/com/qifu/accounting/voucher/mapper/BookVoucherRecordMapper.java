package com.qifu.accounting.voucher.mapper;

import com.qifu.accounting.voucher.domain.BookVoucherRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qifu.accounting.voucher.domain.vo.BookVoucherRecordVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 凭证明细Mapper接口
 *
 *  YM
 * 07-13
 */
@Repository
public interface BookVoucherRecordMapper extends BaseMapper<BookVoucherRecord> {

    List<BookVoucherRecord> selectListByVoucherId(Long voucherId);

    List<BookVoucherRecordVo> ListByVoucherId(Long voucherId);

    void updateSubjectId(@Param("subjectId") Long subjectId,@Param("fatherSubjectId") Long fatherSubjectId);

    int haveVoucherBySubject(Long subjectId);
}
