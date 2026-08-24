package com.qifu.accounting.voucher.service;

import com.qifu.accounting.voucher.domain.BookVoucherSummary;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 摘要Service接口
 *
 *  YM
 * 07-17
 */
public interface IBookVoucherSummaryService extends IService<BookVoucherSummary> {

    List<BookVoucherSummary> getList(BookVoucherSummary bookVoucherSummary);
}
