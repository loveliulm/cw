package com.qifu.accounting.voucher.mapper;

import com.qifu.accounting.common.domain.Page;
import com.qifu.accounting.voucher.domain.BookVoucher;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qifu.accounting.voucher.domain.BookVoucherRecord;
import com.qifu.accounting.voucher.domain.vo.BookVoucherParams;
import com.qifu.accounting.voucher.domain.vo.BookVoucherVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 凭证Mapper接口
 *
 *  YM
 * 07-13
 */
@Repository
public interface BookVoucherMapper extends BaseMapper<BookVoucher> {

    List<BookVoucherVo> getList(@Param("params")BookVoucherParams params, @Param("page")Page page);

    int getListTotal(BookVoucherParams params);

    //科目借方发生额
    BigDecimal getDebitTotal(@Param("subjectCode")String subjectCode, @Param("bookId")Long bookId, @Param("yearPeriod")Date yearPeriod);

    //科目贷方发生额
    BigDecimal getCreditTotal(@Param("subjectCode")String subjectCode, @Param("bookId")Long bookId, @Param("yearPeriod")Date yearPeriod);

    //结转损益-借方发生额（成本）
    BigDecimal getCarryForwardDebitBalance(@Param("bookId")Long bookId,@Param("period") Date period);

    //结转损益-贷方发生额（收入）
    BigDecimal getCarryForwardCreditBalance(@Param("bookId") Long bookId,@Param("period") Date period);

    //结转损益收入涉及的科目凭证  贷方
    List<BookVoucherRecord> getCarryForwardCreditVoucherRecords(@Param("bookId") Long bookId,@Param("period") Date period);

    //结转损益成本涉及的科目凭证  借方
    List<BookVoucherRecord> getCarryForwardDebitVoucherRecords(@Param("bookId") Long bookId,@Param("period") Date period);

    List<BookVoucher> getListByCarryoverId(BookVoucher bookVoucher);

    Integer getTotalByPeriod(@Param("bookId")Long bookId,@Param("period") Date currentPeriod);

    //查询本年利润金额。
    BigDecimal getCarryForwardBalance(@Param("bookId")Long bookId,@Param("yearPeriod") Date yearPeriod);
}
