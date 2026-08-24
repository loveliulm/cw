package com.qifu.accounting.carryover.service;

import com.qifu.accounting.carryover.domain.BookCarryover;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 结转Service接口
 *
 *  YM
 * 07-23
 */
public interface IBookCarryoverService extends IService<BookCarryover> {

    List<BookCarryover> getList(BookCarryover bookCarryover);

    void resetSalesCostPercentage(Long carryoverId, BigDecimal percentage);

    BigDecimal selectSalesCostPercentage(Long carryoverId);

    Boolean selectProfitLossOpen(Long carryoverId);

    void updateProfitLossOpen(Long carryoverId, Boolean profitLossOpen);

    void add(BookCarryover bookCarryover);

    void edit(BookCarryover bookCarryover);

    Map<?,?> calculation(Long[] carryoverIds, Long bookId);

    Map<?,?> calculationAll(Long bookId);

    Map<?,?> generateVoucher(Long carryoverId);

    void carryoverNextPeriod(Long bookId);

    void backCarryoverPeriod(Long bookId);

    Integer voucherNum(Long bookId);
}
