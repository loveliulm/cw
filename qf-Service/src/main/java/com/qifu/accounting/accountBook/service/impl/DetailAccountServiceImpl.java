package com.qifu.accounting.accountBook.service.impl;

import com.qifu.accounting.accountBook.domain.DetailAccount;
import com.qifu.accounting.accountBook.domain.vo.ParamsVo;
import com.qifu.accounting.accountBook.domain.vo.SubjectVo;
import com.qifu.accounting.accountBook.mapper.DetailAccountMapper;
import com.qifu.accounting.accountBook.service.DetailAccountService;
import com.qifu.accounting.common.enums.BalanceDirectionEnum;
import com.qifu.accounting.sys.domain.BookPeriod;
import com.qifu.accounting.sys.domain.BookSubject;
import com.qifu.accounting.sys.mapper.BookPeriodMapper;
import com.qifu.accounting.sys.mapper.BookSubjectMapper;
import com.qifu.common.utils.DateUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *  yangyang
 * @date 2020/7/20 15:32
 */
@Service
public class DetailAccountServiceImpl implements DetailAccountService {
    @Resource
    private DetailAccountMapper detailAccountMapper;
    @Resource
    private BookSubjectMapper subjectMapper;
    @Resource
    private BookPeriodMapper periodMapper;

    /**
     * 明细账-科目列表
     * @return
     */
    public List<SubjectVo> getSubjectList(ParamsVo params){
        return detailAccountMapper.getSubjectList(params);
    }

    /**
     * 明细账-列表
     * @return
     */
    public List<DetailAccount> getList(ParamsVo params){
        List<DetailAccount> list = new ArrayList<>();
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
    public DetailAccount startBalanceByPeriod(ParamsVo params){
        Date previousDate = DateUtils.getPreviousMonth(params.getStartPeriod());
        BookPeriod previousPeriod = periodMapper.selectByPeriod(params.getBookId(),previousDate);
        BookSubject subject = subjectMapper.selectById(params.getSubjectId());
        DetailAccount detailAccount = new DetailAccount();
        if(previousPeriod != null){
            params.setCurrentPeriod(previousDate);
            detailAccount = detailAccountMapper.selectPeriodInitBalance(params);
        }else {
            detailAccount.setYearPeriod(params.getStartPeriod());
            detailAccount.setSummary("期初余额");
            detailAccount.setBalance(subject.getBalanceInit());
        }
        detailAccount.setVoucherDate(params.getStartPeriod());
        return detailAccount;
    }


    /**
     * 查询本期发生额明细
     * @param params
     * @return
     */
    public List<DetailAccount> getListByPeriod(ParamsVo params){
        List<DetailAccount> list = detailAccountMapper.getListByPeriod(params);
        return list;
    }

    /**
     * 查询本期-本期合计
     * @param params
     * @return
     */
    public DetailAccount monthBalanceByPeriod(ParamsVo params){
        DetailAccount detailAccount = detailAccountMapper.monthBalanceByPeriod(params);
        detailAccount.setVoucherDate(DateUtils.getLastDayByYearPeriod(params.getCurrentPeriod()));
        detailAccount.setYearPeriod(DateUtils.getLastDayByYearPeriod(params.getCurrentPeriod()));
        return detailAccount;
    }

    /**
     * 查询本期-本年累计
     * @param params
     * @return
     */
    public DetailAccount yearBalanceByPeriod(ParamsVo params){
        DetailAccount detailAccount = detailAccountMapper.yearBalanceByPeriod(params);
        detailAccount.setVoucherDate(DateUtils.getLastDayByYearPeriod(params.getCurrentPeriod()));
        detailAccount.setYearPeriod(DateUtils.getLastDayByYearPeriod(params.getCurrentPeriod()));
        //判断第一期年份与本期年份是否相同,相同则加上科目的本年累计借方，本年累计贷方
        BookPeriod bookPeriodFirst = periodMapper.getPeriodFirst(params.getBookId());
        if(bookPeriodFirst.getYear() == DateUtils.getYear(params.getCurrentPeriod())){
            BookSubject subject = subjectMapper.selectById(params.getSubjectId());
            BigDecimal credit = detailAccount.getCreditTotal().add(subject.getCredit());
            BigDecimal debit = detailAccount.getDebitTotal().add(subject.getDebit());
            detailAccount.setCreditTotal(credit);
            detailAccount.setDebitTotal(debit);
        }
        return detailAccount;
    }

    //重置list
    private List<DetailAccount> resetList(List<DetailAccount> detailAccounts,Long subjectId){
        BookSubject subject = subjectMapper.selectById(subjectId);
        BigDecimal balance = BigDecimal.ZERO;
        BalanceDirectionEnum balanceDirection = null;
        BalanceDirectionEnum balanceDirectionText = null;
        for (DetailAccount detailAccount:detailAccounts) {
            if(!detailAccount.getSummary().equals("本年累计") && !detailAccount.getSummary().equals("本期合计")) {
                //贷
                if (BalanceDirectionEnum.CREDIT.name().equals(subject.getBalanceDirection().name())) {
                    if (detailAccount.getSummary().equals("期初余额")) {
                        balance = detailAccount.getBalance();
                    } else {
                        balance = balance.add(detailAccount.getCreditTotal()).subtract(detailAccount.getDebitTotal());
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
                    if (detailAccount.getSummary().equals("期初余额")) {
                        balance = detailAccount.getBalance();
                    } else {
                        balance = balance.add(detailAccount.getDebitTotal()).subtract(detailAccount.getCreditTotal());
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
            detailAccount.setBalance(balance);
            detailAccount.setBalanceDirection(balanceDirection);
            detailAccount.setBalanceDirectionText(balanceDirectionText);
        }
        return detailAccounts;
    }

}
