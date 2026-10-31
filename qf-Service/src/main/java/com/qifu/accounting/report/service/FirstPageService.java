package com.qifu.accounting.report.service;

import com.qifu.accounting.report.domain.vo.FirstPageFinancial;
import com.qifu.accounting.report.domain.vo.FirstPageParams;
import com.qifu.accounting.report.domain.vo.FirstPageTax;
import com.qifu.accounting.voucher.domain.BookVoucher;

import java.util.List;
import java.util.Map;

/**
 *  yangyang
 * @date 2020/9/21 10:35
 */
public interface FirstPageService {
    Map reportInfo(FirstPageParams params);

    List<BookVoucher> voucherInfo(FirstPageParams params);

    List<FirstPageFinancial> subjectInfo(FirstPageParams params);

    List<FirstPageTax> taxInfo(FirstPageParams params);
}
