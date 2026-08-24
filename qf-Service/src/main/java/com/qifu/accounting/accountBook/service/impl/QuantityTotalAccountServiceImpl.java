package com.qifu.accounting.accountBook.service.impl;

import com.qifu.accounting.accountBook.domain.QuantityTotalAccount;
import com.qifu.accounting.accountBook.domain.vo.ParamsVo;
import com.qifu.accounting.accountBook.mapper.QuantityTotalAccountMapper;
import com.qifu.accounting.accountBook.service.QuantityTotalAccountService;
import com.qifu.accounting.common.enums.BalanceDirectionEnum;
import com.qifu.accounting.sys.domain.BookPeriod;
import com.qifu.accounting.sys.domain.BookSubject;
import com.qifu.accounting.sys.mapper.BookPeriodMapper;
import com.qifu.accounting.sys.mapper.BookSubjectMapper;
import com.qifu.common.utils.DateUtils;
import com.qifu.framework.aspectj.lang.annotation.Excel;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

/**
 * 数量金额总账
 *  yangyang
 * @date 2020/9/10 14:46
 */
@Service
public class QuantityTotalAccountServiceImpl implements QuantityTotalAccountService {
    @Resource
    private QuantityTotalAccountMapper quantityTotalAccountMapper;
    @Resource
    private BookSubjectMapper subjectMapper;
    @Resource
    private BookPeriodMapper periodMapper;

    /**
     * 数量金额总账-查询列表
     * @param params
     * @return
     */
    public List<QuantityTotalAccount> getList(ParamsVo params){
        List<QuantityTotalAccount> list = quantityTotalAccountMapper.getSubjectList(params);
        BigDecimal startBalanceSum = BigDecimal.ZERO;
        Integer quantityPeriodDebitSum = 0;
        BigDecimal pricePeriodDebitSum = BigDecimal.ZERO;
        BigDecimal periodDebitSum = BigDecimal.ZERO;
        Integer quantityPeriodCreditSum = 0;
        BigDecimal pricePeriodCreditSum = BigDecimal.ZERO;
        BigDecimal periodCreditSum = BigDecimal.ZERO;
        Integer quantityYearDebitSum = 0;
        BigDecimal priceYearDebitSum = BigDecimal.ZERO;
        BigDecimal yearDebitSum = BigDecimal.ZERO;
        Integer quantityYearCreditSum = 0;
        BigDecimal priceYearCreditSum = BigDecimal.ZERO;
        BigDecimal yearCreditSum = BigDecimal.ZERO;
        BigDecimal endBalanceSum = BigDecimal.ZERO;
        Integer quantityEndBalanceSum = 0;
        BigDecimal priceEndBalanceSum = BigDecimal.ZERO;

        MathContext mc = new MathContext(2, RoundingMode.HALF_UP);

        for (int i = list.size()-1; i >= 0; i--) {
            params.setSubjectId(list.get(i).getSubjectId());
            //期初
            QuantityTotalAccount bookBalance1 = startBalanceByPeriod(params);
            //发生额
            QuantityTotalAccount bookBalance2 = monthBalanceByPeriod(params);
            //本年
            QuantityTotalAccount bookBalance3 = yearBalanceByPeriod(params);

            BigDecimal startBalance = bookBalance1.getStartBalance();
            //Integer quantityStartBalance = bookBalance1.getQuantityStartBalance();
            //BigDecimal priceStartBalance = bookBalance1.getPriceStartBalance();

            BigDecimal periodDebit = bookBalance2.getPeriodDebit();
            BigDecimal periodCredit = bookBalance2.getPeriodCredit();
            BigDecimal yearDebit = bookBalance3.getYearDebit();
            BigDecimal yearCredit = bookBalance3.getYearCredit();
            Integer quantityPeriodDebit = bookBalance2.getQuantityPeriodDebit();
            BigDecimal pricePeriodDebit = bookBalance2.getPricePeriodDebit();
            Integer quantityPeriodCredit = bookBalance2.getQuantityPeriodCredit();
            BigDecimal pricePeriodCredit = bookBalance2.getPricePeriodCredit();
            Integer quantityYearDebit = bookBalance3.getQuantityYearDebit();
            BigDecimal priceYearDebit = bookBalance3.getPriceYearDebit();
            Integer quantityYearCredit = bookBalance3.getQuantityYearCredit();
            BigDecimal priceYearCredit = bookBalance3.getPriceYearCredit();

            //期末
            BigDecimal endBalance = BigDecimal.ZERO;
            Integer quantityEndBalance = 0;
            BookSubject bookSubject = subjectMapper.selectById(list.get(i).getSubjectId());
            if(bookSubject != null && bookSubject.getBalanceDirection().name().equals(BalanceDirectionEnum.CREDIT.name())){
                endBalance = endBalance.add(periodCredit).subtract(periodDebit);
                quantityEndBalance = quantityEndBalance + quantityPeriodCredit - quantityPeriodDebit;
            }
            if(bookSubject != null && bookSubject.getBalanceDirection().name().equals(BalanceDirectionEnum.DEBIT.name())){
                endBalance = endBalance.add(periodDebit).subtract(periodCredit);
                quantityEndBalance = quantityEndBalance + quantityPeriodDebit - quantityPeriodCredit;
            }
            BigDecimal priceEndBalance = BigDecimal.ZERO;
            if(endBalance.doubleValue() != 0 && quantityEndBalance != 0){
                priceEndBalance = endBalance.divide(new BigDecimal(quantityEndBalance),mc);
            }
            list.get(i).setStartBalance(startBalance);
            list.get(i).setPeriodDebit(periodDebit);
            list.get(i).setPeriodCredit(periodCredit);
            list.get(i).setYearDebit(yearDebit);
            list.get(i).setYearCredit(yearCredit);
            list.get(i).setEndBalance(endBalance);

            list.get(i).setQuantityPeriodDebit(quantityPeriodDebit);
            list.get(i).setPricePeriodDebit(pricePeriodDebit);
            list.get(i).setQuantityPeriodCredit(quantityPeriodCredit);
            list.get(i).setPricePeriodCredit(pricePeriodCredit);
            list.get(i).setQuantityYearDebit(quantityYearDebit);
            list.get(i).setPriceYearDebit(priceYearDebit);
            list.get(i).setQuantityYearCredit(quantityYearCredit);
            list.get(i).setPriceYearCredit(priceYearCredit);
            list.get(i).setQuantityEndBalance(quantityEndBalance);
            list.get(i).setPriceEndBalance(priceEndBalance);

            //余额为0不显示
            if(params.getShowBalNotZero() !=null && params.getShowBalNotZero() && endBalance.doubleValue() ==0){
                list.remove(list.get(i));
            }


            startBalanceSum = startBalanceSum.add(startBalance);
            periodDebitSum = periodDebitSum.add(periodDebit);
            periodCreditSum = periodCreditSum.add(periodCredit);
            yearDebitSum = yearDebitSum.add(yearDebit);
            yearCreditSum = yearCreditSum.add(yearCredit);
            endBalanceSum = endBalanceSum.add(endBalance);

            quantityPeriodDebitSum = quantityPeriodDebitSum +quantityPeriodDebit;
            pricePeriodDebitSum = pricePeriodDebitSum.add(pricePeriodDebit);
            quantityPeriodCreditSum = quantityPeriodCreditSum + quantityYearCredit;
            pricePeriodCreditSum = pricePeriodCreditSum.add(pricePeriodCredit);
            quantityYearDebitSum = quantityYearDebitSum + quantityYearDebit;
            priceYearDebitSum = priceYearDebitSum.add(priceYearDebit);
            quantityYearCreditSum = quantityYearCreditSum + quantityYearCredit;
            priceYearCreditSum = priceYearCreditSum.add(priceYearCredit);
            quantityEndBalanceSum = quantityEndBalanceSum + quantityEndBalance;
        }

        if(endBalanceSum.doubleValue() != 0 && quantityEndBalanceSum != 0){
            priceEndBalanceSum = endBalanceSum.divide(new BigDecimal(quantityEndBalanceSum),mc);
        }

        //合计
        QuantityTotalAccount quantityTotal = new QuantityTotalAccount();
        quantityTotal.setSubjectName("合计");
        quantityTotal.setStartBalance(startBalanceSum);
        quantityTotal.setPeriodDebit(periodDebitSum);
        quantityTotal.setPeriodCredit(periodCreditSum);
        quantityTotal.setYearCredit(yearCreditSum);
        quantityTotal.setYearDebit(yearDebitSum);
        quantityTotal.setEndBalance(endBalanceSum);

        quantityTotal.setQuantityPeriodDebit(quantityPeriodDebitSum);
        quantityTotal.setPricePeriodDebit(pricePeriodDebitSum);
        quantityTotal.setQuantityPeriodCredit(quantityPeriodCreditSum);
        quantityTotal.setPricePeriodCredit(pricePeriodCreditSum);
        quantityTotal.setQuantityYearDebit(quantityYearDebitSum);
        quantityTotal.setPriceYearDebit(priceYearDebitSum);
        quantityTotal.setQuantityYearCredit(quantityYearCreditSum);
        quantityTotal.setPriceYearCredit(priceYearCreditSum);
        quantityTotal.setQuantityEndBalance(quantityEndBalanceSum);
        quantityTotal.setPriceEndBalance(priceEndBalanceSum);
        list.add(quantityTotal);
        return list;
    }


    /**
     * 查询本期期初余额
     * @return
     */
    public QuantityTotalAccount startBalanceByPeriod(ParamsVo params){
        Date previousDate = DateUtils.getPreviousMonth(params.getStartPeriod());
        BookPeriod previousPeriod = periodMapper.selectByPeriod(params.getBookId(),previousDate);

        QuantityTotalAccount quantityTotal = new QuantityTotalAccount();
        if(previousPeriod != null){
            params.setCurrentPeriod(previousDate);
            quantityTotal = quantityTotalAccountMapper.startBalanceByPeriod(params);
        }else {
            BookSubject subject = subjectMapper.selectById(params.getSubjectId());
            quantityTotal.setStartBalance(subject.getBalanceInit());
        }

        return quantityTotal;
    }

    /**
     * 查询本期-本期合计
     * @param params
     * @return
     */
    public QuantityTotalAccount monthBalanceByPeriod(ParamsVo params){
        QuantityTotalAccount quantityTotal = quantityTotalAccountMapper.monthBalanceByPeriod(params);
        return quantityTotal;
    }

    /**
     * 查询本期-本年累计发生额
     * @param params
     * @return
     */
    public QuantityTotalAccount yearBalanceByPeriod(ParamsVo params){
        QuantityTotalAccount quantityTotal = quantityTotalAccountMapper.yearBalanceByPeriod(params);
        //判断第一期年份与本期年份是否相同,相同则加上科目的本年累计借方，本年累计贷方
        BookPeriod bookPeriodFirst = periodMapper.getPeriodFirst(params.getBookId());
        if(bookPeriodFirst.getYear() == DateUtils.getYear(params.getStartPeriod())){
            BookSubject subject = subjectMapper.selectById(params.getSubjectId());
            BigDecimal credit = quantityTotal.getYearCredit().add(subject.getCredit());
            BigDecimal debit = quantityTotal.getYearDebit().add(subject.getDebit());
            quantityTotal.setYearCredit(credit);
            quantityTotal.setYearDebit(debit);
        }
        return quantityTotal;
    }
}
