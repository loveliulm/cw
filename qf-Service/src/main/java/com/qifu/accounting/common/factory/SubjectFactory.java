package com.qifu.accounting.common.factory;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qifu.accounting.sys.domain.BookAuxiliary;
import com.qifu.accounting.sys.domain.BookSubject;
import com.qifu.accounting.sys.domain.BookSubjectAuxiliary;
import com.qifu.accounting.sys.domain.BookSubjectBalanceRecord;
import com.qifu.accounting.common.enums.BalanceDirectionEnum;
import com.qifu.accounting.common.enums.BalancePurposeEnum;
import com.qifu.accounting.sys.mapper.BookSubjectMapper;
import com.qifu.accounting.sys.mapper.BookSubjectPeriodMapper;
import com.qifu.accounting.sys.service.*;
import com.qifu.accounting.voucher.domain.BookVoucherSummary;
import com.qifu.accounting.voucher.service.IBookVoucherService;
import com.qifu.common.utils.Asserts;
import com.qifu.common.utils.StringDateUtil;
import com.qifu.common.utils.StringUtils;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *  yangyang
 * @date 2020/7/15 17:01
 * 科目金额封装类
 */
@Component
public class SubjectFactory {
    @Autowired
    private BookSubjectMapper subjectMapper;
    @Autowired
    private IBookSubjectBalanceRecordService balanceRecordService;
    @Autowired
    private IBookSubjectPeriodService subjectPeriodService;
    @Autowired
    private IBookVoucherService voucherService;
    @Autowired
    private IBookSubjectAuxiliaryService subjectAuxiliaryService;

    //科目编号规则：目前支持四级
    public static final String SUBJECT_CODE_RULE = "4:2:2:2";



    /**
     * 上级不能删除状态,
     * 初始化金额
     * @param bookSubject
     */
    public BookSubject addSubject(BookSubject bookSubject,BookSubject fatherSubject){
        List<BookSubject> list = getBrotherSubject(fatherSubject,bookSubject.getCode());

        //初始化金额
        BigDecimal balance = new BigDecimal(0);
        BigDecimal balanceInit = new BigDecimal(0);
        BigDecimal credit = new BigDecimal(0);
        BigDecimal debit = new BigDecimal(0);
        if(list.isEmpty()){
            fatherSubject.setDeletable(Boolean.FALSE);
            fatherSubject.setBalanceInitChecked(Boolean.FALSE);
            fatherSubject.setCreditChecked(Boolean.FALSE);
            fatherSubject.setDebitChecked(Boolean.FALSE);
            fatherSubject.setIsLeaf(Boolean.FALSE);
            subjectMapper.updateById(fatherSubject);
        }else{
            for (BookSubject b:list) {
                balanceInit = balanceInit.add(b.getBalanceInit());
                credit = credit.add(b.getCredit());
                debit = debit.add(b.getDebit());
                balance = balance.add(b.getBalance());
            }
        }

        BigDecimal oldBalance = fatherSubject.getBalance();
        BigDecimal newBalance = fatherSubject.getBalance().subtract(balance);
        bookSubject.setBalance(newBalance);
        bookSubject.setBalanceInit(fatherSubject.getBalanceInit().subtract(balanceInit));
        bookSubject.setCredit(fatherSubject.getCredit().subtract(credit));
        bookSubject.setDebit(fatherSubject.getDebit().subtract(debit));
        //其他继承父级
        if(isCopyFatherSubject(fatherSubject.getSubjectId())){
            bookSubject.setIsAuxiliary(fatherSubject.getIsAuxiliary());
            bookSubject.setIsSupplier(fatherSubject.getIsSupplier());
            bookSubject.setIsCustomer(fatherSubject.getIsCustomer());
            bookSubject.setIsProject(fatherSubject.getIsProject());
            bookSubject.setIsDepartment(fatherSubject.getIsDepartment());
            bookSubject.setIsPerson(fatherSubject.getIsPerson());
            bookSubject.setIsStock(fatherSubject.getIsStock());
            bookSubject.setIsCashFlow(fatherSubject.getIsCashFlow());
            bookSubject.setIsForCurrency(fatherSubject.getIsForCurrency());
            bookSubject.setIsCashFlow(fatherSubject.getIsCashFlow());
            bookSubject.setIsQuantity(fatherSubject.getIsQuantity());
            bookSubject.setQuantityUnit(fatherSubject.getQuantityUnit());
        }

        bookSubject.setNewYearBalance();
        int i = subjectMapper.insert(bookSubject);
        Asserts.isTrue(i == 1,"科目新增失败");

        //科目结账表新增科目及科目余额
        subjectPeriodService.insert(bookSubject,fatherSubject,!list.isEmpty());

        BookSubjectBalanceRecord bookSubjectBalanceRecord = new BookSubjectBalanceRecord(bookSubject.getSubjectId(),
                BalancePurposeEnum.新增科目,oldBalance,newBalance,balance);
        balanceRecordService.insert(bookSubjectBalanceRecord);
        return bookSubject;
    }

    public boolean isCopyFatherSubject(Long subjectId){
        if(voucherService.haveVoucherBySubject(subjectId)){
            return true;
        }
        BookSubjectAuxiliary bookSubjectAuxiliary = new BookSubjectAuxiliary();
        bookSubjectAuxiliary.setSubjectId(subjectId);
        List<BookSubjectAuxiliary> list = subjectAuxiliaryService.getList(bookSubjectAuxiliary);
        if(list.size() > 0){
            return true;
        }
        return false;
    }

    /**
     * 根据code获取兄弟级
     * @param fatherSubject
     * @param code
     * @return
     */
    public List<BookSubject>  getBrotherSubject(BookSubject fatherSubject,String code){
        LambdaQueryWrapper<BookSubject> lqw = new LambdaQueryWrapper<BookSubject>();
        lqw.like(BookSubject::getCode,fatherSubject.getCode())
                .eq(BookSubject::getBookId,fatherSubject.getBookId())
                .ne(BookSubject::getCode,code)
                .eq(BookSubject::getLevel,fatherSubject.getLevel()+1);
        List<BookSubject> list = subjectMapper.selectList(lqw);
        return list;
    }

    /**
     * 期初余额，本年累计-贷方余额，本年累计-借方余额父系所有发生变化
     * @param bookSubject
     * @param differenceAmount 录入的和以前数据相差的金额
     * YY: type 1：balanceInit && balance,2:debit,3:credit
     */
    public void editPaternalSubject(BookSubject bookSubject, BigDecimal differenceAmount, int type){
        int codeLength = bookSubject.getCode().length();
        //目前只支持4级
        if( codeLength<= 4 && codeLength > 10){
            return;
        }else{
            if(codeLength == 6){
                //上级
                BookSubject fatherSubject = editAmount(bookSubject,differenceAmount,type);
                subjectMapper.updateById(fatherSubject);
            }
            if(codeLength == 8){
                //上级
                BookSubject fatherSubject = editAmount(bookSubject,differenceAmount,type);
                subjectMapper.updateById(fatherSubject);
                //上上级
                BookSubject fatherSubject2 = editAmount(fatherSubject,differenceAmount,type);
                subjectMapper.updateById(fatherSubject2);
            }
            if(codeLength == 10){
                //上级
                BookSubject fatherSubject = editAmount(bookSubject,differenceAmount,type);
                subjectMapper.updateById(fatherSubject);
                //上上级
                BookSubject fatherSubject2 = editAmount(fatherSubject,differenceAmount,type);
                subjectMapper.updateById(fatherSubject2);
                //上上上级
                BookSubject fatherSubject3 = editAmount(fatherSubject2,differenceAmount,type);
                subjectMapper.updateById(fatherSubject3);
            }
        }
    }

    /**
     * 根据子级和差额修改上级金额
     * @param bookSubject 子级
     * @param differenceAmount 录入的和以前数据相差的金额
     * @param type 1：balanceInit&&balance,2:debit,3:credit
     * @return
     */
    public BookSubject editAmount(BookSubject bookSubject,BigDecimal differenceAmount,int type){
        BookSubject fatherSubject = fatherSubject(bookSubject.getCode(),bookSubject.getBookId());
        //如果没有差额直接返回
        if(StringUtils.isNotNull(differenceAmount) && differenceAmount.compareTo(BigDecimal.ZERO) == 0){
            return fatherSubject;
        }
        if(type == StringDateUtil.TYPE_1){
            BigDecimal oldBalance = fatherSubject.getBalance();
            BigDecimal newBalance = oldBalance.add(differenceAmount);
            BookSubjectBalanceRecord bookSubjectBalanceRecord = new BookSubjectBalanceRecord(bookSubject.getSubjectId(),
                    BalancePurposeEnum.期初余额变动,oldBalance,newBalance,differenceAmount);
            balanceRecordService.insert(bookSubjectBalanceRecord);

            fatherSubject.setBalanceInit(fatherSubject.getBalanceInit().add(differenceAmount));
            fatherSubject.setBalance(newBalance);

            //更新科目余额账期表的余额。
            subjectPeriodService.updateBalanceBySubjectId(fatherSubject.getBookId(),fatherSubject.getSubjectId(),differenceAmount);
        }else if(type == StringDateUtil.TYPE_2){
            fatherSubject.setDebit(fatherSubject.getDebit().add(differenceAmount));
        }else if(type == StringDateUtil.TYPE_3){
            fatherSubject.setCredit(fatherSubject.getCredit().add(differenceAmount));
        }
        fatherSubject.setNewYearBalance();
        fatherSubject.setUpdateTime(new Date());
        return fatherSubject;
    }


    /**
     * 根据code 和 bookId 获取父级科目
     * @param code
     * @param bookId
     * @return
     */
    public BookSubject fatherSubject(String code,Long bookId){
        String fatherCode = code.substring(0,code.length()-2);
        LambdaQueryWrapper<BookSubject> lqw = new LambdaQueryWrapper<BookSubject>();
        lqw.eq(BookSubject::getCode,fatherCode)
                .eq(BookSubject::getBookId,bookId);
        BookSubject fatherSubject = subjectMapper.selectOne(lqw);
        return fatherSubject;
    }

    /**
     * 根据code 和 bookId 获取科目
     * @param code
     * @param bookId
     * @return
     */
    public BookSubject getSubject(String code,Long bookId){
        LambdaQueryWrapper<BookSubject> lqw = new LambdaQueryWrapper<BookSubject>();
        lqw.eq(BookSubject::getCode,code)
                .eq(BookSubject::getBookId,bookId);
        BookSubject fatherSubject = subjectMapper.selectOne(lqw);
        return fatherSubject;
    }

    /**
     * 更新科目余额
     * @param subject
     * @param balance
     * @param purpose
     */
    public void updateBalance(BookSubject subject, BigDecimal balance, BalanceDirectionEnum balanceDirection, BalancePurposeEnum purpose){
        if(StringUtils.isNotNull(balance) && balance.compareTo(BigDecimal.ZERO) == 0){
            return;
        }
        BigDecimal oldBalance = subject.getBalance();
        BigDecimal newBalance = oldBalance;

        BigDecimal diffMoney = new BigDecimal(0);
        //余额方向相同则相加，相向则相减
        if(subject.getBalanceDirection().name().equals(balanceDirection.name())){
            newBalance = newBalance.add(balance);
            diffMoney = balance;
        }else{
            newBalance = newBalance.subtract(balance);
            diffMoney = balance.multiply(new BigDecimal(-1));
        }
        subject.setBalance(newBalance);
        subject.setUpdateTime(new Date());
        subjectMapper.updateById(subject);
        BookSubjectBalanceRecord bookSubjectBalanceRecord = new BookSubjectBalanceRecord(subject.getSubjectId(),purpose,oldBalance,newBalance,balance);
        balanceRecordService.insert(bookSubjectBalanceRecord);

        //更新科目余额账期表的余额。
        subjectPeriodService.updateBalanceBySubjectId(subject.getBookId(),subject.getSubjectId(),diffMoney);
    }
}



