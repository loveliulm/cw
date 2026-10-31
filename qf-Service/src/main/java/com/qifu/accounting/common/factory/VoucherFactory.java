package com.qifu.accounting.common.factory;

import com.qifu.accounting.sys.domain.BookSubject;
import com.qifu.accounting.voucher.domain.BookVoucherRecord;
import com.qifu.accounting.common.enums.BalanceDirectionEnum;
import com.qifu.accounting.common.enums.BalancePurposeEnum;
import com.qifu.accounting.sys.mapper.BookSubjectMapper;
import com.qifu.common.utils.Asserts;
import com.qifu.common.utils.StringDateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 *  yangyang
 * @date 2020/7/15 18:38
 */
@Component
public class VoucherFactory {
    @Autowired
    private BookSubjectMapper subjectMapper;
    @Autowired
    private SubjectFactory subjectFactory;

    /**
     * 新增/删除凭证明细-科目余额计算。
     * @param bookVoucherRecord
     */
    public void updateBalance(BookVoucherRecord bookVoucherRecord, String delFlag, BalancePurposeEnum purpose){
        Asserts.isNull(bookVoucherRecord.getSubjectId(),"科目id不能为空！");
        //自己
        BookSubject subject = subjectMapper.selectById(bookVoucherRecord.getSubjectId());
        updateBalance(subject,bookVoucherRecord, delFlag, purpose);
        int codeLength = subject.getCode().length();
        //目前只支持4级
        if( codeLength<= 4 && codeLength > 10){
            return;
        }else{
            if(codeLength == 6){
                //上级
                BookSubject fatherSubject = subjectFactory.fatherSubject(subject.getCode(),subject.getBookId());
                updateBalance(fatherSubject,bookVoucherRecord, delFlag, purpose);
            }
            if(codeLength == 8){
                //上级
                BookSubject fatherSubject = subjectFactory.fatherSubject(subject.getCode(),subject.getBookId());
                updateBalance(fatherSubject,bookVoucherRecord, delFlag, purpose);
                //上上级
                BookSubject fatherSubject2 = subjectFactory.fatherSubject(fatherSubject.getCode(),fatherSubject.getBookId());
                updateBalance(fatherSubject2,bookVoucherRecord, delFlag, purpose);
            }
            if(codeLength == 10){
                //上级
                BookSubject fatherSubject = subjectFactory.fatherSubject(subject.getCode(),subject.getBookId());
                updateBalance(fatherSubject,bookVoucherRecord, delFlag, purpose);
                //上上级
                BookSubject fatherSubject2 = subjectFactory.fatherSubject(fatherSubject.getCode(),fatherSubject.getBookId());
                updateBalance(fatherSubject2,bookVoucherRecord, delFlag, purpose);
                //上上上级
                BookSubject fatherSubject3 = subjectFactory.fatherSubject(fatherSubject2.getCode(),fatherSubject2.getBookId());
                updateBalance(fatherSubject3,bookVoucherRecord, delFlag, purpose);
            }
        }
    }

    /**
     * YY:余额计算=余额方向相同加，余额方向相向减
     * @param subject
     * @param bookVoucherRecord
     * @param delFlag 0：新增，2：删除
     * @param purpose 余额明细表说明字段
     */
    private void updateBalance(BookSubject subject,BookVoucherRecord bookVoucherRecord, String delFlag, BalancePurposeEnum purpose){
        BigDecimal credit = bookVoucherRecord.getCredit();
        BigDecimal debit = bookVoucherRecord.getDebit();

        BalanceDirectionEnum voucherBalanceDirection = bookVoucherRecord.getBalanceDirection();

        BigDecimal balance = new BigDecimal(0);
        if(BalanceDirectionEnum.CREDIT.name().equals(voucherBalanceDirection.name()) && credit != null){
            if(StringDateUtil.Del_FLAG_0.equals(delFlag)){
                balance = balance.add(credit);
            }
            if(StringDateUtil.Del_FLAG_2.equals(delFlag)){
                balance = balance.subtract(credit);
            }
        }
        if(BalanceDirectionEnum.CREDIT.name().equals(voucherBalanceDirection.name()) && debit != null){
            if(StringDateUtil.Del_FLAG_0.equals(delFlag)){
                balance = balance.subtract(debit);
            }
            if(StringDateUtil.Del_FLAG_2.equals(delFlag)){
                balance = balance.add(debit);
            }
        }
        if(BalanceDirectionEnum.DEBIT.name().equals(voucherBalanceDirection.name()) && debit != null){
            if(StringDateUtil.Del_FLAG_0.equals(delFlag)){
                balance = balance.add(debit);
            }
            if(StringDateUtil.Del_FLAG_2.equals(delFlag)){
                balance = balance.subtract(debit);
            }
        }
        if(BalanceDirectionEnum.DEBIT.name().equals(voucherBalanceDirection.name()) && credit != null){
            if(StringDateUtil.Del_FLAG_0.equals(delFlag)){
                balance = balance.subtract(credit);
            }
            if(StringDateUtil.Del_FLAG_2.equals(delFlag)){
                balance = balance.add(credit);
            }
        }
        subjectFactory.updateBalance(subject,balance,voucherBalanceDirection, purpose);
    }
}
