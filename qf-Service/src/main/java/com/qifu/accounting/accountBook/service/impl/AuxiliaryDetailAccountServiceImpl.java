package com.qifu.accounting.accountBook.service.impl;

import com.qifu.accounting.accountBook.domain.AuxiliaryDetailAccount;
import com.qifu.accounting.accountBook.domain.AuxiliaryTotalAccount;
import com.qifu.accounting.accountBook.domain.vo.AuxiliaryAccParamsVo;
import com.qifu.accounting.accountBook.domain.vo.AuxiliaryTypeVo;
import com.qifu.accounting.accountBook.domain.vo.AuxiliaryVo;
import com.qifu.accounting.accountBook.domain.vo.SubjectVo;
import com.qifu.accounting.accountBook.mapper.AuxiliaryDetailAccountMapper;
import com.qifu.accounting.accountBook.service.AuxiliaryDetailAccountService;
import com.qifu.accounting.common.enums.AuxiliaryTypeEnum;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 辅助核算明细账
 *  yangyang
 * @date 2020/9/8 10:41
 */
@Service
public class AuxiliaryDetailAccountServiceImpl implements AuxiliaryDetailAccountService {
    @Resource
    private AuxiliaryDetailAccountMapper mapper;

    @Resource
    private BookSubjectMapper bookSubjectMapper;

    @Resource
    private BookPeriodMapper periodMapper;

    @Resource
    private BookSubjectAuxiliaryMapper subjectAuxiliaryMapper;

    public List<SubjectVo> getSubjectList(AuxiliaryAccParamsVo params){
        return mapper.getSubjectList(params);
    }

    public List<AuxiliaryTypeVo> getAuxiliaryTypeList(Long subjectId){
        BookSubject subject = bookSubjectMapper.selectById(subjectId);
        List<AuxiliaryTypeVo> list = new ArrayList<>();
        if(subject.getIsSupplier()){
            AuxiliaryTypeVo auxiliaryTypeVo = new AuxiliaryTypeVo((long)1, AuxiliaryTypeEnum.供应商.name());
            list.add(auxiliaryTypeVo);
        }
        if(subject.getIsCustomer()){
            AuxiliaryTypeVo auxiliaryTypeVo = new AuxiliaryTypeVo((long)2, AuxiliaryTypeEnum.客户.name());
            list.add(auxiliaryTypeVo);
        }
        if(subject.getIsProject()){
            AuxiliaryTypeVo auxiliaryTypeVo = new AuxiliaryTypeVo((long)3, AuxiliaryTypeEnum.项目.name());
            list.add(auxiliaryTypeVo);
        }
        if(subject.getIsDepartment()){
            AuxiliaryTypeVo auxiliaryTypeVo = new AuxiliaryTypeVo((long)4, AuxiliaryTypeEnum.部门.name());
            list.add(auxiliaryTypeVo);
        }
        if(subject.getIsPerson()){
            AuxiliaryTypeVo auxiliaryTypeVo = new AuxiliaryTypeVo((long)5, AuxiliaryTypeEnum.人员.name());
            list.add(auxiliaryTypeVo);
        }
        if(subject.getIsStock()){
            AuxiliaryTypeVo auxiliaryTypeVo = new AuxiliaryTypeVo((long)6, AuxiliaryTypeEnum.存货.name());
            list.add(auxiliaryTypeVo);
        }
        return list;
    }

    public List<AuxiliaryVo> getAuxiliary(AuxiliaryAccParamsVo params){
        return mapper.getAuxiliary(params);
    }

    /**
     * 列表
     * @param params
     * @return
     */
    public List<AuxiliaryDetailAccount> getList(AuxiliaryAccParamsVo params){
        List<AuxiliaryDetailAccount> list = new ArrayList<>();
        list.add(initBalanceByPeriod(params));
        List<Date> dates = DateUtils.getBetweenMonths(params.getStartPeriod(),params.getEndPeriod());
        for (Date currentPeriod : dates) {
            params.setCurrentPeriod(currentPeriod);
            //本期发生额明细
            list.addAll(getDetailList(params));
            //本期合计
            list.add(totalPeriod(params));
            //本年累计
            list.add(totalYear(params));
        }
        resetList(list, params.getSubjectId());
        return list;
    }

    /**
     * 本期发生额明细
     * @param params
     * @return
     */
    public List<AuxiliaryDetailAccount>  getDetailList(AuxiliaryAccParamsVo params){
        List<AuxiliaryDetailAccount> list = mapper.getDetailList(params);
        return list;
    }

    /**
     * 期初余额
     * @param params
     * @return
     */
    public AuxiliaryDetailAccount initBalanceByPeriod(AuxiliaryAccParamsVo params){
        Date previousDate = DateUtils.getPreviousMonth(params.getStartPeriod());
        //查询前一个月账期，1.如果存在，证明结账表中有数据直接取上个月的期末余额就行了，2.否则取当前科目的期初余额。
        BookPeriod previousPeriod = periodMapper.selectByPeriod(params.getBookId(),previousDate);
        AuxiliaryDetailAccount auxiliaryDetailAccount = new AuxiliaryDetailAccount();
        if(previousPeriod != null){
            params.setCurrentPeriod(previousDate);
            auxiliaryDetailAccount = mapper.selectPeriodInitBalance(params);
        }else {
            BookSubjectAuxiliary subjectAuxiliary = subjectAuxiliaryMapper.selectByTotalBalance(params);
            auxiliaryDetailAccount.setSummary("期初余额");
            auxiliaryDetailAccount.setBalance(subjectAuxiliary.getBalance());
        }
        auxiliaryDetailAccount.setVoucherDate(params.getStartPeriod());
        return auxiliaryDetailAccount;
    }

    /**
     * 本期合计
     * @param params
     * @return
     */
    public AuxiliaryDetailAccount totalPeriod(AuxiliaryAccParamsVo params){
        AuxiliaryDetailAccount detail = mapper.totalPeriod(params);
        detail.setVoucherDate(DateUtils.getLastDayByYearPeriod(params.getCurrentPeriod()));
        return detail;
    }

    /**
     * 本年累计
     * @param params
     * @return
     */
    public AuxiliaryDetailAccount totalYear(AuxiliaryAccParamsVo params){
        AuxiliaryDetailAccount detail = mapper.totalYear(params);
        detail.setVoucherDate(DateUtils.getLastDayByYearPeriod(params.getCurrentPeriod()));
        //判断第一期年份与本期年份是否相同,相同则加上科目初始化的辅助核算余额
        BookPeriod bookPeriodFirst = periodMapper.getPeriodFirst(params.getBookId());
        if(bookPeriodFirst.getYear() == DateUtils.getYear(params.getCurrentPeriod())){
            BookSubjectAuxiliary subjectAuxiliary = subjectAuxiliaryMapper.selectByTotalBalance(params);
            BigDecimal credit = detail.getCreditTotal().add(subjectAuxiliary.getCredit());
            BigDecimal debit = detail.getDebitTotal().add(subjectAuxiliary.getDebit());
            detail.setCreditTotal(credit);
            detail.setDebitTotal(debit);
        }
        return detail;
    }


    //重置list
    private List<AuxiliaryDetailAccount> resetList(List<AuxiliaryDetailAccount> list, Long subjectId){
        BookSubject subject = bookSubjectMapper.selectById(subjectId);
        BigDecimal balance = BigDecimal.ZERO;
        BalanceDirectionEnum balanceDirection = null;
        BalanceDirectionEnum balanceDirectionText = null;
        for (AuxiliaryDetailAccount auxiliaryDetailAccount:list) {
            if(!auxiliaryDetailAccount.getSummary().equals("本年累计") && !auxiliaryDetailAccount.getSummary().equals("本期合计")){
                //贷
                if(BalanceDirectionEnum.CREDIT.name().equals(subject.getBalanceDirection().name())){
                    if(auxiliaryDetailAccount.getSummary().equals("期初余额")){
                        balance = auxiliaryDetailAccount.getBalance();
                    }else{
                        balance = balance.add(auxiliaryDetailAccount.getCreditTotal()).subtract(auxiliaryDetailAccount.getDebitTotal());
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
                    if(auxiliaryDetailAccount.getSummary().equals("期初余额")){
                        balance = auxiliaryDetailAccount.getBalance();
                    }else {
                        balance = balance.add(auxiliaryDetailAccount.getDebitTotal()).subtract(auxiliaryDetailAccount.getCreditTotal());
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
            auxiliaryDetailAccount.setBalance(balance);
            auxiliaryDetailAccount.setBalanceDirection(balanceDirection);
            auxiliaryDetailAccount.setBalanceDirectionText(balanceDirectionText);
        }
        return list;
    }
}
