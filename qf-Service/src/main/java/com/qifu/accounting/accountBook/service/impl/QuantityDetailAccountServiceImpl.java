package com.qifu.accounting.accountBook.service.impl;

import com.qifu.accounting.accountBook.domain.QuantityDetailAccount;
import com.qifu.accounting.accountBook.domain.vo.ParamsVo;
import com.qifu.accounting.accountBook.domain.vo.SubjectVo;
import com.qifu.accounting.accountBook.mapper.QuantityDetailAccountMapper;
import com.qifu.accounting.accountBook.service.QuantityDetailAccountService;
import com.qifu.accounting.common.enums.BalanceDirectionEnum;
import com.qifu.accounting.sys.domain.BookPeriod;
import com.qifu.accounting.sys.domain.BookSubject;
import com.qifu.accounting.sys.mapper.BookPeriodMapper;
import com.qifu.accounting.sys.mapper.BookSubjectMapper;
import com.qifu.common.utils.DateUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 数量金额明细账
 *  yangyang
 * @date 2020/9/10 10:09
 */
@Service
public class QuantityDetailAccountServiceImpl implements QuantityDetailAccountService {
    @Resource
    private QuantityDetailAccountMapper quantityDetailAccountMapper;
    @Resource
    private BookSubjectMapper subjectMapper;
    @Resource
    private BookPeriodMapper periodMapper;

    /**
     * 数量金额明细账-科目列表
     * @return
     */
    public List<SubjectVo> getSubjectList(ParamsVo params){
        return quantityDetailAccountMapper.getSubjectList(params);
    }

    /**
     * 数量金额明细账-列表
     * @return
     */
    public List<QuantityDetailAccount> getList(ParamsVo params){
        List<QuantityDetailAccount> list = new ArrayList<>();
        //期初余额
        list.add(startBalanceByPeriod(params));
        List<Date> dates = DateUtils.getBetweenMonths(params.getStartPeriod(),params.getEndPeriod());
        for (Date currentPeriod : dates) {
            params.setCurrentPeriod(currentPeriod);
            //本期发生额明细
            list.addAll(getListByPeriod(params));
            //本期合计
            list.add(monthBalanceByPeriod(params));
            //本年累计
            list.add(yearBalanceByPeriod(params));
        }
        resetList(list,params.getSubjectId());
        return list;
    }


    /**
     * 查询本期期初余额
     * @return
     */
    public QuantityDetailAccount startBalanceByPeriod(ParamsVo params){
        Date previousDate = DateUtils.getPreviousMonth(params.getStartPeriod());
        BookPeriod previousPeriod = periodMapper.selectByPeriod(params.getBookId(),previousDate);
        BookSubject subject = subjectMapper.selectById(params.getSubjectId());
        QuantityDetailAccount quantityDetailAccount = new QuantityDetailAccount();
        if(previousPeriod != null){
            params.setCurrentPeriod(previousDate);
            quantityDetailAccount = quantityDetailAccountMapper.selectPeriodInitBalance(params);
        }else {
            quantityDetailAccount.setSummary("期初余额");
            quantityDetailAccount.setBalance(subject.getBalanceInit());
        }
        quantityDetailAccount.setVoucherDate(params.getStartPeriod());
        return quantityDetailAccount;
    }


    /**
     * 查询本期发生额明细
     * @param params
     * @return
     */
    public List<QuantityDetailAccount> getListByPeriod(ParamsVo params){
        List<QuantityDetailAccount> list = quantityDetailAccountMapper.getListByPeriod(params);
        return list;
    }

    /**
     * 查询本期-本期合计
     * @param params
     * @return
     */
    public QuantityDetailAccount monthBalanceByPeriod(ParamsVo params){
        QuantityDetailAccount quantityDetailAccount = quantityDetailAccountMapper.monthBalanceByPeriod(params);
        quantityDetailAccount.setVoucherDate(DateUtils.getLastDayByYearPeriod(params.getCurrentPeriod()));
        return quantityDetailAccount;
    }

    /**
     * 查询本期-本年累计
     * @param params
     * @return
     */
    public QuantityDetailAccount yearBalanceByPeriod(ParamsVo params){
        QuantityDetailAccount quantityDetailAccount = quantityDetailAccountMapper.yearBalanceByPeriod(params);
        quantityDetailAccount.setVoucherDate(DateUtils.getLastDayByYearPeriod(params.getCurrentPeriod()));
        //判断第一期年份与本期年份是否相同,相同则加上科目的本年累计借方，本年累计贷方
        BookPeriod bookPeriodFirst = periodMapper.getPeriodFirst(params.getBookId());
        if(bookPeriodFirst.getYear() == DateUtils.getYear(params.getCurrentPeriod())){
            BookSubject subject = subjectMapper.selectById(params.getSubjectId());
            BigDecimal credit = quantityDetailAccount.getCreditTotal().add(subject.getCredit());
            BigDecimal debit = quantityDetailAccount.getDebitTotal().add(subject.getDebit());
            quantityDetailAccount.setCreditTotal(credit);
            quantityDetailAccount.setDebitTotal(debit);
        }
        return quantityDetailAccount;
    }

    //重置list
    private List<QuantityDetailAccount> resetList(List<QuantityDetailAccount> detailAccounts,Long subjectId){
        BookSubject subject = subjectMapper.selectById(subjectId);
        BigDecimal balance = BigDecimal.ZERO;
        BalanceDirectionEnum balanceDirection = null;
        BalanceDirectionEnum balanceDirectionText = null;
        Integer balanceQuantity = 0;
        BigDecimal balancePrice = BigDecimal.ZERO;
        MathContext mc = new MathContext(2, RoundingMode.HALF_UP);
        for (QuantityDetailAccount quantityDetailAccount:detailAccounts) {
            if(!quantityDetailAccount.getSummary().equals("本年累计") && !quantityDetailAccount.getSummary().equals("本期合计")) {
                //贷
                if (BalanceDirectionEnum.CREDIT.name().equals(subject.getBalanceDirection().name())) {
                    if (quantityDetailAccount.getSummary().equals("期初余额")) {
                        balance = quantityDetailAccount.getBalance();
                    } else {
                        balance = balance.add(quantityDetailAccount.getCreditTotal()).subtract(quantityDetailAccount.getDebitTotal());
                        balanceQuantity = balanceQuantity + quantityDetailAccount.getCreditQuantity() -quantityDetailAccount.getDebitQuantity();
                        balancePrice = balance.divide(new BigDecimal(balanceQuantity),mc);
                    }
                    if (balance.compareTo(BigDecimal.ZERO) == -1) {
                        balanceDirection = BalanceDirectionEnum.DEBIT;
                        balanceDirectionText = BalanceDirectionEnum.借;
                    }
                    if (balance.compareTo(BigDecimal.ZERO) == 1) {
                        balanceDirection = BalanceDirectionEnum.CREDIT;
                        balanceDirectionText = BalanceDirectionEnum.贷;
                    }
                    if (balance.compareTo(BigDecimal.ZERO) == 0) {
                        balanceDirection = BalanceDirectionEnum.EQUALS;
                        balanceDirectionText = BalanceDirectionEnum.平;
                    }
                }
                //借
                if (BalanceDirectionEnum.DEBIT.name().equals(subject.getBalanceDirection().name())) {
                    if (quantityDetailAccount.getSummary().equals("期初余额")) {
                        balance = quantityDetailAccount.getBalance();
                    } else {
                        balance = balance.add(quantityDetailAccount.getDebitTotal()).subtract(quantityDetailAccount.getCreditTotal());
                        balanceQuantity = balanceQuantity + quantityDetailAccount.getDebitQuantity() -quantityDetailAccount.getCreditQuantity();
                        if(balanceQuantity != 0 && balance.doubleValue() != 0){
                            balancePrice = balance.divide(new BigDecimal(balanceQuantity),mc);
                        }

                    }
                    if (balance.compareTo(BigDecimal.ZERO) == -1) {
                        balanceDirection = BalanceDirectionEnum.CREDIT;
                        balanceDirectionText = BalanceDirectionEnum.贷;
                    }
                    if (balance.compareTo(BigDecimal.ZERO) == 1) {
                        balanceDirection = BalanceDirectionEnum.DEBIT;
                        balanceDirectionText = BalanceDirectionEnum.借;
                    }
                    if (balance.compareTo(BigDecimal.ZERO) == 0) {
                        balanceDirection = BalanceDirectionEnum.EQUALS;
                        balanceDirectionText = BalanceDirectionEnum.平;
                    }
                }
            }
            quantityDetailAccount.setBalance(balance);
            quantityDetailAccount.setBalanceDirection(balanceDirection);
            quantityDetailAccount.setBalanceDirectionText(balanceDirectionText);
            quantityDetailAccount.setBalanceQuantity(balanceQuantity);
            quantityDetailAccount.setBalancePrice(balancePrice);
        }
        return detailAccounts;
    }

}
