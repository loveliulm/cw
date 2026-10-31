package com.qifu.accounting.sys.service.impl;

import com.qifu.accounting.sys.domain.BookPeriod;
import com.qifu.accounting.sys.domain.BookSubject;
import com.qifu.accounting.sys.mapper.BookPeriodMapper;
import com.qifu.accounting.sys.mapper.BookSubjectMapper;
import com.qifu.accounting.sys.service.IBookPeriodService;
import com.qifu.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qifu.accounting.sys.mapper.BookAuxiliaryPeriodMapper;
import com.qifu.accounting.sys.domain.BookAuxiliaryPeriod;
import com.qifu.accounting.sys.service.IBookAuxiliaryPeriodService;

import javax.xml.crypto.Data;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 辅助核算月末余额Service业务层处理
 *
 *
 * 09-07
 */
@Service
public class BookAuxiliaryPeriodServiceImpl extends ServiceImpl<BookAuxiliaryPeriodMapper, BookAuxiliaryPeriod> implements IBookAuxiliaryPeriodService {

    @Autowired
    private BookAuxiliaryPeriodMapper auxiliaryPeriodMapper;

    @Autowired
    private BookPeriodMapper periodMapper;

    @Autowired
    private IBookPeriodService periodService;

    @Autowired
    private BookSubjectMapper bookSubjectMapper;

    /**
     * 辅助核算月末结转金额
     * @param bookId
     * @param period
     */
    public  void insertList(Long bookId, Date period){

        Date previousPeriod = DateUtils.getPreviousMonth(period);
        BookPeriod bookPeriod = periodMapper.selectByPeriod(bookId,previousPeriod);
        if(bookPeriod != null){
            auxiliaryPeriodMapper.insertList(bookId,period,previousPeriod);
        }else {
            auxiliaryPeriodMapper.insertList(bookId,period,null);
        }

    }


    public void updateBalance(Long subjectId, BigDecimal diffMoney, Long auxiliaryId){
        BookSubject subject = bookSubjectMapper.selectById(subjectId);
        Date yearPeriod = periodService.yearPeriod(subject.getBookId());
        auxiliaryPeriodMapper.updateBalanceByPeriod(subjectId,diffMoney,yearPeriod,auxiliaryId);
        auxiliaryPeriodMapper.updateBalanceByAfterPeriod(subjectId,diffMoney,yearPeriod,auxiliaryId);
    }
}
