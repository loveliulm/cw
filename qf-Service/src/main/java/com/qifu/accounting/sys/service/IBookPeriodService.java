package com.qifu.accounting.sys.service;

import com.qifu.accounting.sys.domain.BookPeriod;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qifu.accounting.sys.domain.vo.SeasonVo;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 账期Service接口
 *
 *  YM
 * 07-17
 */
public interface IBookPeriodService extends IService<BookPeriod> {

    BookPeriod getInfo(Long bookId);

    Date yearPeriod(Long bookId);

    Date maxPeriod(Long bookId);

    Boolean isCarryover(Long bookId);

    List<BookPeriod> getList(BookPeriod bookPeriod);

    List<SeasonVo> quarterList(BookPeriod bookPeriod);
}
