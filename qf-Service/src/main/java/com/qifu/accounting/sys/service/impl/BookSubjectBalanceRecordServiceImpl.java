package com.qifu.accounting.sys.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qifu.accounting.sys.mapper.BookSubjectBalanceRecordMapper;
import com.qifu.accounting.sys.domain.BookSubjectBalanceRecord;
import com.qifu.accounting.sys.service.IBookSubjectBalanceRecordService;

/**
 * 科目余额明细Service业务层处理
 *
 *  YM
 * 07-08
 */
@Service
public class BookSubjectBalanceRecordServiceImpl extends ServiceImpl<BookSubjectBalanceRecordMapper, BookSubjectBalanceRecord> implements IBookSubjectBalanceRecordService {

    @Autowired
    private BookSubjectBalanceRecordMapper recordMapper;

    public int insert(BookSubjectBalanceRecord bookSubjectBalanceRecord){
        int i = recordMapper.insert(bookSubjectBalanceRecord);
        return i;
    }
}
