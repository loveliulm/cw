package com.qifu.accounting.accountBook.service;

import com.qifu.accounting.accountBook.domain.VoucherTotal;
import com.qifu.accounting.accountBook.domain.vo.ParamsVo;

import java.util.List;

/**
 *  yangyang
 * @date 2020/7/31 10:14
 */
public interface VoucherTotalService {

    List<VoucherTotal> getList(ParamsVo paramsVo);
}
