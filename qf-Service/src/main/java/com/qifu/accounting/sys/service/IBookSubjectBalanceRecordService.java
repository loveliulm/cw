package com.qifu.accounting.sys.service;

import com.qifu.accounting.sys.domain.BookSubjectBalanceRecord;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 科目余额明细Service接口
 *
 *  YM
 * 07-08
 */
public interface IBookSubjectBalanceRecordService extends IService<BookSubjectBalanceRecord> {

    int insert(BookSubjectBalanceRecord bookSubjectBalanceRecord);
}
