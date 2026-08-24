package com.qifu.accounting.accountBook.mapper;

import com.qifu.accounting.accountBook.domain.BookBalance;
import com.qifu.accounting.accountBook.domain.QuantityTotalAccount;
import com.qifu.accounting.accountBook.domain.vo.ParamsVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 数量金额总账
 *  yangyang
 * @date 2020/9/10 14:45
 */
@Repository
public interface QuantityTotalAccountMapper {
    List<QuantityTotalAccount> getSubjectList(ParamsVo params);

    QuantityTotalAccount startBalanceByPeriod(ParamsVo params);

    QuantityTotalAccount monthBalanceByPeriod(ParamsVo params);

    QuantityTotalAccount yearBalanceByPeriod(ParamsVo params);
}
