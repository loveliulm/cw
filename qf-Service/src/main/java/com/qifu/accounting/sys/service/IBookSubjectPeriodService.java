package com.qifu.accounting.sys.service;

import com.qifu.accounting.sys.domain.BookSubject;
import com.qifu.accounting.sys.domain.BookSubjectPeriod;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 科目账期期末余额Service接口
 *
 *  YM
 * 08-17
 */
public interface IBookSubjectPeriodService extends IService<BookSubjectPeriod> {

    void insertListByPeriod(Long bookId, Date period);

    void updateBalanceBySubjectId(Long bookId,Long subjectId, BigDecimal diffMoney);

    void insert(BookSubject bookSubject,BookSubject fatherSubject,Boolean isSon);
}
