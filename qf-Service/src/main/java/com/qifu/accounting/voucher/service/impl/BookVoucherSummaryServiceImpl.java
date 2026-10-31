package com.qifu.accounting.voucher.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qifu.accounting.voucher.mapper.BookVoucherSummaryMapper;
import com.qifu.accounting.voucher.domain.BookVoucherSummary;
import com.qifu.accounting.voucher.service.IBookVoucherSummaryService;

import javax.annotation.Resource;
import java.util.List;

/**
 * 摘要Service业务层处理
 *
 *  YM
 * 07-17
 */
@Service
public class BookVoucherSummaryServiceImpl extends ServiceImpl<BookVoucherSummaryMapper, BookVoucherSummary> implements IBookVoucherSummaryService {

    @Resource
    private BookVoucherSummaryMapper summaryMapper;

    public List<BookVoucherSummary> getList(BookVoucherSummary bookVoucherSummary){
        return summaryMapper.getList(bookVoucherSummary);
    }
}
