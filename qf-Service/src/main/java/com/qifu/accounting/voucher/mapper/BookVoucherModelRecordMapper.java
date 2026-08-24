package com.qifu.accounting.voucher.mapper;

import com.qifu.accounting.voucher.domain.BookVoucherModelRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qifu.accounting.voucher.domain.BookVoucherModelRecordPub;
import com.qifu.accounting.voucher.domain.vo.BookVoucherModelRecordVo;
import com.qifu.accounting.voucher.domain.vo.BookVoucherModelWageRecordVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 凭证模板明细Mapper接口
 *
 *  YM
 * 07-16
 */
@Repository
public interface BookVoucherModelRecordMapper extends BaseMapper<BookVoucherModelRecord> {

    List<BookVoucherModelRecordVo> selectListByModelId(Long modelId);

    void InitInsertList(@Param("list")List<BookVoucherModelRecordPub> list,@Param("modelId")Long modelId, @Param("bookId")Long bookId);

    List<BookVoucherModelWageRecordVo> selectWageRecordListByModelId(Long modelId);

    void deleteOldVoucherModelWage(Long modelId);
}
