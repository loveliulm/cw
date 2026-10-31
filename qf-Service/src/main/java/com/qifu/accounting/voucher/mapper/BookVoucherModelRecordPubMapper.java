package com.qifu.accounting.voucher.mapper;

import com.qifu.accounting.voucher.domain.BookVoucherModelRecordPub;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * 凭证模板明细公用Mapper接口
 *
 *  YM
 * 08-07
 */
public interface BookVoucherModelRecordPubMapper extends BaseMapper<BookVoucherModelRecordPub> {

    List<BookVoucherModelRecordPub> selectListByPubId(Long modelId);
}
