package com.qifu.accounting.voucher.mapper;

import com.qifu.accounting.voucher.domain.BookVoucherSummary;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * 摘要Mapper接口
 *
 *  YM
 * 07-17
 */
public interface BookVoucherSummaryMapper extends BaseMapper<BookVoucherSummary> {

    List<BookVoucherSummary> getList(BookVoucherSummary bookVoucherSummary);
}
