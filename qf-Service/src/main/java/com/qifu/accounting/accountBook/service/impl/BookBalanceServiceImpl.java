package com.qifu.accounting.accountBook.service.impl;

import com.qifu.accounting.accountBook.domain.BookBalance;
import com.qifu.accounting.accountBook.domain.vo.ParamsVo;
import com.qifu.accounting.accountBook.mapper.BookBalanceMapper;
import com.qifu.accounting.accountBook.service.BookBalanceService;
import com.qifu.accounting.common.enums.BalanceDirectionEnum;
import com.qifu.accounting.sys.domain.BookPeriod;
import com.qifu.accounting.sys.domain.BookSubject;
import com.qifu.accounting.sys.mapper.BookPeriodMapper;
import com.qifu.accounting.sys.mapper.BookSubjectMapper;
import com.qifu.common.utils.DateUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 科目余额表
 *  yangyang
 * @date 2020/8/28 14:43
 */
@Service
public class BookBalanceServiceImpl implements BookBalanceService {
    @Resource
    private BookBalanceMapper bookBalanceMapper;
    @Resource
    private BookSubjectMapper subjectMapper;
    @Resource
    private BookPeriodMapper periodMapper;

    /**
     * 科目余额表-查询列表
     * @param params
     * @return
     */
    public List<BookBalance> getList(ParamsVo params){
        List<BookBalance> list = bookBalanceMapper.getSubjectList(params);
        BigDecimal startDebitSum = BigDecimal.ZERO;
        BigDecimal startCreditSum = BigDecimal.ZERO;
        BigDecimal periodDebitSum = BigDecimal.ZERO;
        BigDecimal periodCreditSum = BigDecimal.ZERO;
        BigDecimal yearDebitSum = BigDecimal.ZERO;
        BigDecimal yearCreditSum = BigDecimal.ZERO;
        BigDecimal endDebitSum = BigDecimal.ZERO;
        BigDecimal endCreditSum = BigDecimal.ZERO;

        for (int i = list.size()-1; i >= 0; i--) {
            params.setSubjectId(list.get(i).getSubjectId());
            //期初
            BookBalance bookBalance1 = startBalanceByPeriod(params);
            //发生额
            BookBalance bookBalance2 = monthBalanceByPeriod(params);
            //本年
            BookBalance bookBalance3 = yearBalanceByPeriod(params);

            BigDecimal startDebit = bookBalance1.getStartDebit();
            BigDecimal startCredit = bookBalance1.getStartCredit();
            BigDecimal periodDebit = bookBalance2.getPeriodDebit();
            BigDecimal periodCredit = bookBalance2.getPeriodCredit();
            BigDecimal yearDebit = bookBalance3.getYearDebit();
            BigDecimal yearCredit = bookBalance3.getYearCredit();

            //期末
            BigDecimal endDebit = BigDecimal.ZERO;
            BigDecimal endCredit = BigDecimal.ZERO;
            BookSubject bookSubject = subjectMapper.selectById(list.get(i).getSubjectId());
            if(bookSubject != null && bookSubject.getBalanceDirection().name().equals(BalanceDirectionEnum.CREDIT.name())){
                endCredit = startCredit.add(periodCredit).subtract(periodDebit);
            }
            if(bookSubject != null && bookSubject.getBalanceDirection().name().equals(BalanceDirectionEnum.DEBIT.name())){
                endDebit = startDebit.add(periodDebit).subtract(periodCredit);
            }
            list.get(i).setStartDebit(startDebit);
            list.get(i).setStartCredit(startCredit);
            list.get(i).setPeriodDebit(periodDebit);
            list.get(i).setPeriodCredit(periodCredit);
            list.get(i).setYearDebit(yearDebit);
            list.get(i).setYearCredit(yearCredit);
            list.get(i).setEndDebit(endDebit);
            list.get(i).setEndCredit(endCredit);

            //余额为0不显示
            if(params.getShowBalNotZero() !=null && params.getShowBalNotZero() && endCredit.doubleValue() ==0 && endDebit.doubleValue() == 0){
                list.remove(list.get(i));
            }


            startDebitSum = startDebitSum.add(startDebit);
            startCreditSum = startCreditSum.add(startCredit);
            periodDebitSum = periodDebitSum.add(periodDebit);
            periodCreditSum = periodCreditSum.add(periodCredit);
            yearDebitSum = yearDebitSum.add(yearDebit);
            yearCreditSum = yearCreditSum.add(yearCredit);
            endDebitSum = endDebitSum.add(endDebit);
            endCreditSum = endCreditSum.add(endCredit);
        }
        //合计
        BookBalance bookBalance = new BookBalance();
        bookBalance.setSubjectName("合计");
        bookBalance.setStartDebit(startDebitSum);
        bookBalance.setStartCredit(startCreditSum);
        bookBalance.setPeriodDebit(periodDebitSum);
        bookBalance.setPeriodCredit(periodCreditSum);
        bookBalance.setYearCredit(yearCreditSum);
        bookBalance.setYearDebit(yearDebitSum);
        bookBalance.setEndDebit(endDebitSum);
        bookBalance.setEndCredit(endCreditSum);
        list.add(bookBalance);
        return list;
    }


    /**
     * 查询本期期初余额
     * @return
     */
    public BookBalance startBalanceByPeriod(ParamsVo params){
        Date previousDate = DateUtils.getPreviousMonth(params.getStartPeriod());
        BookPeriod previousPeriod = periodMapper.selectByPeriod(params.getBookId(),previousDate);

        BookBalance bookBalance = new BookBalance();
        if(previousPeriod != null){
            params.setCurrentPeriod(previousDate);
            bookBalance = bookBalanceMapper.startBalanceByPeriod(params);
        }else {
            BookSubject subject = subjectMapper.selectById(params.getSubjectId());
            if(subject.getBalanceDirection().name().equals(BalanceDirectionEnum.CREDIT.name())){
                bookBalance.setStartCredit(subject.getBalanceInit());
                bookBalance.setStartDebit(BigDecimal.ZERO);
            }
            if(subject.getBalanceDirection().name().equals(BalanceDirectionEnum.DEBIT.name())){
                bookBalance.setStartDebit(subject.getBalanceInit());
                bookBalance.setStartCredit(BigDecimal.ZERO);
            }
        }

        return bookBalance;
    }

    /**
     * 查询本期-本期合计
     * @param params
     * @return
     */
    public BookBalance monthBalanceByPeriod(ParamsVo params){
        BookBalance detailAccount = bookBalanceMapper.monthBalanceByPeriod(params);
        return detailAccount;
    }

    /**
     * 查询本期-本年累计发生额
     * @param params
     * @return
     */
    public BookBalance yearBalanceByPeriod(ParamsVo params){
        BookBalance bookBalance = bookBalanceMapper.yearBalanceByPeriod(params);
        //判断第一期年份与本期年份是否相同,相同则加上科目的本年累计借方，本年累计贷方
        BookPeriod bookPeriodFirst = periodMapper.getPeriodFirst(params.getBookId());
        if(bookPeriodFirst.getYear() == DateUtils.getYear(params.getStartPeriod())){
            BookSubject subject = subjectMapper.selectById(params.getSubjectId());
            BigDecimal credit = bookBalance.getYearCredit().add(subject.getCredit());
            BigDecimal debit = bookBalance.getYearDebit().add(subject.getDebit());
            bookBalance.setYearCredit(credit);
            bookBalance.setYearDebit(debit);
        }
        return bookBalance;
    }
}
