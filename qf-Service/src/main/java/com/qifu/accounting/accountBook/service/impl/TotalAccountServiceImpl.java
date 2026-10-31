package com.qifu.accounting.accountBook.service.impl;

import com.qifu.accounting.accountBook.domain.TotalAccount;
import com.qifu.accounting.accountBook.domain.vo.ParamsVo;
import com.qifu.accounting.accountBook.domain.vo.TotalAccResult;
import com.qifu.accounting.accountBook.mapper.TotalAccountMapper;
import com.qifu.accounting.accountBook.service.TotalAccountService;
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
 * @date 2020/8/27 14:06
 */
@Service
public class TotalAccountServiceImpl implements TotalAccountService {
    @Resource
    private TotalAccountMapper totalAccountMapper;
    @Resource
    private BookPeriodMapper periodMapper;
    @Resource
    private BookSubjectMapper subjectMapper;

    /**
     * 总账-列表
     * @param params
     * @return
     */
    public List<TotalAccResult> getListAll(ParamsVo params) {
        List<Date> dates = DateUtils.getBetweenMonths(params.getStartPeriod(),params.getEndPeriod());
        List<TotalAccResult> list = totalAccountMapper.getSubjectList(params);
        for (int i = list.size()-1; i >= 0; i--) {
            params.setSubjectId(list.get(i).getSubjectId());
            List<TotalAccount> totalAccounts = new ArrayList<>();
            //期初余额
            totalAccounts.add(startBalanceByPeriod(params));

            List<TotalAccount> balanceByPeriods = totalAccountMapper.balanceByPeriod(params,dates);
            totalAccounts.addAll(balanceByPeriods);
            resetList(totalAccounts,params.getSubjectId());
            list.get(i).setDetails(totalAccounts);
            if(params.getShowBalNotZero() !=null && params.getShowBalNotZero() && totalAccounts.size()==3
                    && totalAccounts.get(0).getBalance().doubleValue() == 0
                    && totalAccounts.get(1).getBalance().doubleValue() == 0
                    && totalAccounts.get(2).getBalance().doubleValue() == 0
            ){
                list.remove(list.get(i));
            }
        }
        return list;
    }

    /**
     * 查询本期期初余额
     * @return
     */
    public TotalAccount startBalanceByPeriod(ParamsVo params) {
        Date previousDate = DateUtils.getPreviousMonth(params.getStartPeriod());
        //查询前一个月账期，1.如果存在，证明结账表中有数据直接取上个月的期末余额就行了，2.否则取当前科目的期初余额。
        BookPeriod previousPeriod = periodMapper.selectByPeriod(params.getBookId(),previousDate);
        BookSubject subject = subjectMapper.selectById(params.getSubjectId());
        TotalAccount totalAccount = new TotalAccount();
        if(previousPeriod != null){
            params.setCurrentPeriod(previousDate);
            totalAccount = totalAccountMapper.selectPeriodInitBalance(params);
        }else {
            totalAccount.setYearPeriod(params.getStartPeriod());
            totalAccount.setSummary("期初余额");
            totalAccount.setBalance(subject.getBalanceInit());
        }
        return totalAccount;
    }


    /**
     * 重置list
     * 本年累计重置，第一年要加上科目的年初余额
     * 余额重置，上一条余额与当前借贷方余额计算得出当前余额
     * @return
     */
    private List<TotalAccount> resetList(List<TotalAccount> totalAccounts,Long subjectId){
        BookSubject subject = subjectMapper.selectById(subjectId);
        BigDecimal balance = BigDecimal.ZERO;
        BalanceDirectionEnum balanceDirection = null;
        BalanceDirectionEnum balanceDirectionText = null;
        for (int i = 0; i < totalAccounts.size(); i++) {
            if(totalAccounts.get(i).getSummary().equals("本年累计")){
                //判断第一期年份与本期年份是否相同,相同则加上科目的本年累计借方，本年累计贷方
                ParamsVo params = new ParamsVo();
                params.setBookId(subject.getBookId());
                BookPeriod bookPeriodFirst = periodMapper.getPeriodFirst(params.getBookId());
                if(bookPeriodFirst.getYear() == DateUtils.getYear(totalAccounts.get(i).getYearPeriod())){
                    BigDecimal credit = totalAccounts.get(i).getCredit().add(subject.getCredit());
                    BigDecimal debit = totalAccounts.get(i).getDebit().add(subject.getDebit());
                    totalAccounts.get(i).setCredit(credit);
                    totalAccounts.get(i).setDebit(debit);
                }
            }else{
                //贷
                if(BalanceDirectionEnum.CREDIT.name().equals(subject.getBalanceDirection().name())){
                    if(totalAccounts.get(i).getSummary().equals("期初余额")){
                        balance = totalAccounts.get(i).getBalance();
                    }else{
                        balance = balance.add(totalAccounts.get(i).getCredit()).subtract(totalAccounts.get(i).getDebit());
                    }
                    if(balance.compareTo(BigDecimal.ZERO) == -1){
                        balanceDirection = BalanceDirectionEnum.DEBIT;
                        balanceDirectionText = BalanceDirectionEnum.借;
                    }
                    if(balance.compareTo(BigDecimal.ZERO) == 1){
                        balanceDirection = BalanceDirectionEnum.CREDIT;
                        balanceDirectionText = BalanceDirectionEnum.贷;
                    }
                    if(balance.compareTo(BigDecimal.ZERO) == 0){
                        balanceDirection = BalanceDirectionEnum.EQUALS;
                        balanceDirectionText = BalanceDirectionEnum.平;
                    }
                }
                //借
                if(BalanceDirectionEnum.DEBIT.name().equals(subject.getBalanceDirection().name())){
                    if(totalAccounts.get(i).getSummary().equals("期初余额")){
                        balance = totalAccounts.get(i).getBalance();
                    }else {
                        balance = balance.add(totalAccounts.get(i).getDebit()).subtract(totalAccounts.get(i).getCredit());
                    }
                    if(balance.compareTo(BigDecimal.ZERO) == -1){
                        balanceDirection = BalanceDirectionEnum.CREDIT;
                        balanceDirectionText = BalanceDirectionEnum.贷;
                    }
                    if(balance.compareTo(BigDecimal.ZERO) == 1){
                        balanceDirection = BalanceDirectionEnum.DEBIT;
                        balanceDirectionText = BalanceDirectionEnum.借;
                    }
                    if(balance.compareTo(BigDecimal.ZERO) == 0){
                        balanceDirection = BalanceDirectionEnum.EQUALS;
                        balanceDirectionText = BalanceDirectionEnum.平;
                    }
                }
            }
            totalAccounts.get(i).setBalance(balance);
            totalAccounts.get(i).setBalanceDirection(balanceDirection);
            totalAccounts.get(i).setBalanceDirectionText(balanceDirectionText);
        }
        return totalAccounts;
    }

}
