package com.qifu.accounting.voucher.service;

import com.qifu.accounting.voucher.domain.BookVoucherModel;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qifu.accounting.voucher.domain.vo.BookVoucherModelWageVo;

import java.util.List;

/**
 * 凭证模板Service接口
 *
 *  YM
 * 07-16
 */
public interface IBookVoucherModelService extends IService<BookVoucherModel> {

    void add(BookVoucherModel bookVoucherModel);

    void delete(Long modelId);

    void edit(BookVoucherModel bookVoucherModel);

    BookVoucherModel getInfo(Long modelId);

    void InitWageVoucherModel(Long bookId,Long templateId);

    BookVoucherModelWageVo getInfoByWage(Long modelId);

    void editByWage(BookVoucherModel bookVoucherModel);
}
