package com.qifu.accounting.voucher.service;

import com.qifu.accounting.common.domain.Page;
import com.qifu.accounting.voucher.domain.BookVoucher;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qifu.accounting.voucher.domain.vo.BookVoucherParams;
import com.qifu.accounting.voucher.domain.vo.BookVoucherVo;

import java.util.List;

/**
 * 凭证总Service接口
 *
 *  YM
 * 07-13
 */
public interface IBookVoucherService extends IService<BookVoucher> {

    BookVoucher add(BookVoucher bookVoucher);

    void edit(BookVoucher bookVoucher);

    void delete(Long[] voucherIds);

    BookVoucher getInfo(Long voucherId);

    List<BookVoucherVo> getList(BookVoucherParams params ,Page page);

    int getListTotal(BookVoucherParams params);

    Long getCode(BookVoucher bookVoucher);

    void review(BookVoucher bookVoucher);

    boolean haveVoucherBySubject(Long subjectId);
}
