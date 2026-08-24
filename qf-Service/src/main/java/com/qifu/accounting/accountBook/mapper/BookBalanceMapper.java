package com.qifu.accounting.accountBook.mapper;

import com.qifu.accounting.accountBook.domain.BookBalance;
import com.qifu.accounting.accountBook.domain.vo.ParamsVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *  yangyang
 * @date 2020/8/28 14:43
 */
@Repository
public interface BookBalanceMapper {
    List<BookBalance> getSubjectList(ParamsVo params);

    BookBalance startBalanceByPeriod(ParamsVo params);

    BookBalance monthBalanceByPeriod(ParamsVo params);

    BookBalance yearBalanceByPeriod(ParamsVo params);
}
