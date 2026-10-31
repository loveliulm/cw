package com.qifu.accounting.accountBook.service;

import com.qifu.accounting.accountBook.domain.BookBalance;
import com.qifu.accounting.accountBook.domain.vo.ParamsVo;

import java.util.List;

/**
 *  yangyang
 * @date 2020/8/28 14:43
 */
public interface BookBalanceService {
    List<BookBalance> getList(ParamsVo params);

    BookBalance startBalanceByPeriod(ParamsVo params);

    BookBalance monthBalanceByPeriod(ParamsVo params);

    BookBalance yearBalanceByPeriod(ParamsVo params);
}
