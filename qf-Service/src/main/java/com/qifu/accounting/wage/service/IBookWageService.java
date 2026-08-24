package com.qifu.accounting.wage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qifu.accounting.common.enums.VoucherTypeEnum;
import com.qifu.accounting.sys.domain.BookAccountTemplate;
import com.qifu.accounting.voucher.domain.BookVoucherModel;
import com.qifu.accounting.wage.domain.BookWage;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *  yangyang
 * @date 2020/7/20 15:32
 */
public interface IBookWageService extends IService<BookWage> {
    Map<?,?> wageTemplate(Long bookId, VoucherTypeEnum typeEnum);

    List<BookWage> getList(BookWage bookWage);

    BookWage getWageById(Long id);

    String importList(List<BookWage> list);

    Map<Object, Object> checkWages(Long bookId);
}
