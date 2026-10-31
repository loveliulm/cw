package com.qifu.accounting.accountBook.service.impl;

import com.qifu.accounting.accountBook.domain.AuxiliaryTotalAccount;
import com.qifu.accounting.accountBook.domain.vo.AuxiliaryAccParamsVo;
import com.qifu.accounting.accountBook.domain.vo.SubjectVo;
import com.qifu.accounting.accountBook.mapper.AuxiliaryTotalAccountMapper;
import com.qifu.accounting.accountBook.service.AuxiliaryTotalAccountService;
import com.qifu.accounting.common.enums.BalanceDirectionEnum;
import com.qifu.accounting.sys.domain.BookPeriod;
import com.qifu.accounting.sys.domain.BookSubject;
import com.qifu.accounting.sys.domain.BookSubjectAuxiliary;
import com.qifu.accounting.sys.mapper.BookPeriodMapper;
import com.qifu.accounting.sys.mapper.BookSubjectAuxiliaryMapper;
import com.qifu.accounting.sys.mapper.BookSubjectMapper;
import com.qifu.common.utils.DateUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 *  yangyang
 * @date 2020/9/9 9:37
 */
@Service
public class AuxiliaryTotalAccountServiceImpl implements AuxiliaryTotalAccountService {

    @Resource
    private AuxiliaryTotalAccountMapper mapper;

    @Resource
    private BookSubjectMapper bookSubjectMapper;

    @Resource
    private BookSubjectAuxiliaryMapper subjectAuxiliaryMapper;

    @Resource
    private BookPeriodMapper periodMapper;

    public List<SubjectVo> getSubjectList(AuxiliaryAccParamsVo params){
        return mapper.getSubjectList(params);
    }

    public List<AuxiliaryTotalAccount> getList(AuxiliaryAccParamsVo params){
        List<AuxiliaryTotalAccount> list = mapper.getDetailList(params);
        BigDecimal startDebitSum = BigDecimal.ZERO;
        BigDecimal startCreditSum = BigDecimal.ZERO;
        BigDecimal periodDebitSum = BigDecimal.ZERO;
        BigDecimal periodCreditSum = BigDecimal.ZERO;
        BigDecimal yearDebitSum = BigDecimal.ZERO;
        BigDecimal yearCreditSum = BigDecimal.ZERO;
        BigDecimal endDebitSum = BigDecimal.ZERO;
        BigDecimal endCreditSum = BigDecimal.ZERO;

        for (int i = list.size()-1; i >= 0; i--) {
            params.setAuxiliaryId(list.get(i).getAuxiliaryId());
            //期初
            AuxiliaryTotalAccount total1 = initBalance(params);
            //发生额
            AuxiliaryTotalAccount total2 = totalPeriod(params);
            //本年
            AuxiliaryTotalAccount total3 = totalYear(params);

            BigDecimal startDebit = total1.getStartDebit();
            BigDecimal startCredit = total1.getStartCredit();
            BigDecimal periodDebit = total2.getPeriodDebit();
            BigDecimal periodCredit = total2.getPeriodCredit();
            BigDecimal yearDebit = total3.getYearDebit();
            BigDecimal yearCredit = total3.getYearCredit();

            //期末
            BigDecimal endDebit = BigDecimal.ZERO;
            BigDecimal endCredit = BigDecimal.ZERO;
            BookSubject bookSubject = bookSubjectMapper.selectById(params.getSubjectId());
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
        AuxiliaryTotalAccount auxiliaryTotal = new AuxiliaryTotalAccount();
        auxiliaryTotal.setAuxiliaryName("合计");
        auxiliaryTotal.setStartDebit(startDebitSum);
        auxiliaryTotal.setStartCredit(startCreditSum);
        auxiliaryTotal.setPeriodDebit(periodDebitSum);
        auxiliaryTotal.setPeriodCredit(periodCreditSum);
        auxiliaryTotal.setYearCredit(yearCreditSum);
        auxiliaryTotal.setYearDebit(yearDebitSum);
        auxiliaryTotal.setEndDebit(endDebitSum);
        auxiliaryTotal.setEndCredit(endCreditSum);
        list.add(auxiliaryTotal);
        return list;
    }

    /**
     * 查询本期期初余额
     * @return
     */
    public AuxiliaryTotalAccount initBalance(AuxiliaryAccParamsVo params){
        Date previousDate = DateUtils.getPreviousMonth(params.getStartPeriod());
        //查询前一个月账期，1.如果存在，证明结账表中有数据直接取上个月的期末余额就行了，2.否则取当前科目的期初余额。
        BookPeriod previousPeriod = periodMapper.selectByPeriod(params.getBookId(),previousDate);
        AuxiliaryTotalAccount total = new AuxiliaryTotalAccount();
        if(previousPeriod != null){
            params.setCurrentPeriod(previousDate);
            total = mapper.initBalance(params);
        }else{
            BookSubject subject = bookSubjectMapper.selectById(params.getSubjectId());
            BookSubjectAuxiliary subjectAuxiliary = subjectAuxiliaryMapper.selectByTotalBalance(params);
            if(subject.getBalanceDirection().name().equals(BalanceDirectionEnum.CREDIT.name())){
                total.setStartCredit(subjectAuxiliary.getBalance());
                total.setStartDebit(BigDecimal.ZERO);
            }
            if(subject.getBalanceDirection().name().equals(BalanceDirectionEnum.DEBIT.name())){
                total.setStartDebit(subjectAuxiliary.getBalance());
                total.setStartCredit(BigDecimal.ZERO);
            }
        }
        return total;
    }

    /**
     * 查询本期-本期合计
     * @param params
     * @return
     */
    public AuxiliaryTotalAccount totalPeriod(AuxiliaryAccParamsVo params){
        AuxiliaryTotalAccount currentPeriodTotal = mapper.totalPeriod(params);
        return currentPeriodTotal;
    }

    /**
     * 查询本期-本年累计
     * @param params
     * @return
     */
    public AuxiliaryTotalAccount totalYear(AuxiliaryAccParamsVo params){
        AuxiliaryTotalAccount currentYearTotal = mapper.totalYear(params);
        //判断第一期年份与本期年份是否相同,相同则加上科目的本年累计借方，本年累计贷方
        BookPeriod bookPeriodFirst = periodMapper.getPeriodFirst(params.getBookId());
        if(bookPeriodFirst.getYear() == DateUtils.getYear(params.getStartPeriod())){
            BookSubjectAuxiliary subjectAuxiliary = subjectAuxiliaryMapper.selectByTotalBalance(params);
            BigDecimal credit = currentYearTotal.getYearCredit().add(subjectAuxiliary.getCredit());
            BigDecimal debit = currentYearTotal.getYearDebit().add(subjectAuxiliary.getDebit());
            currentYearTotal.setYearCredit(credit);
            currentYearTotal.setYearDebit(debit);
        }
        return currentYearTotal;
    }
}
