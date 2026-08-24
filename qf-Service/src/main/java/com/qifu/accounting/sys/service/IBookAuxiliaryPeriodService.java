package com.qifu.accounting.sys.service;

import com.qifu.accounting.sys.domain.BookAuxiliaryPeriod;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 辅助核算月末余额Service接口
 *
 *  YM
 * 09-07
 */
public interface IBookAuxiliaryPeriodService extends IService<BookAuxiliaryPeriod> {
    void insertList(Long bookId, Date period);

    void updateBalance(Long subjectId, BigDecimal diffMoney,Long auxiliaryId);
}
