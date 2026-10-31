package com.qifu.accounting.sys.service;

import com.qifu.accounting.sys.domain.BookAuxiliary;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qifu.accounting.sys.domain.vo.BookAuxiliaryVo;

import java.util.List;

/**
 * 辅组核算Service接口
 *
 *  YM
 * 07-09
 */
public interface IBookAuxiliaryService extends IService<BookAuxiliary> {

    BookAuxiliary getOne(Long bookId, Long auxiliaryTypeId);

    List<BookAuxiliaryVo> getListByVoucherRecord(Long voucherRecordId);

    List<BookAuxiliaryVo> getListByVoucherModelRecord(Long voucherModelRecordId);

    void checkAuxiliaryName(BookAuxiliary bookAuxiliary);
}
