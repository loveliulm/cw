package com.qifu.accounting.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.qifu.accounting.fixedAsset.domain.BookFixedAssetCard;
import com.qifu.accounting.fixedAsset.mapper.BookFixedAssetCategoryMapper;
import com.qifu.accounting.fixedAsset.service.impl.BookFixedAssetCardServiceImpl;
import com.qifu.accounting.sys.domain.BookSubject;
import com.qifu.accounting.sys.domain.BookSubjectBalanceRecord;
import com.qifu.accounting.sys.domain.vo.BookSubjectVo;
import com.qifu.accounting.common.enums.BalancePurposeEnum;
import com.qifu.accounting.common.enums.BalanceDirectionEnum;
import com.qifu.accounting.common.factory.AuxiliaryFactory;
import com.qifu.accounting.common.factory.SubjectFactory;
import com.qifu.accounting.sys.domain.vo.TrialBalance;
import com.qifu.accounting.sys.mapper.BookSubjectMapper;
import com.qifu.accounting.sys.service.*;
import com.qifu.accounting.voucher.mapper.BookVoucherRecordMapper;
import com.qifu.accounting.voucher.service.IBookVoucherService;
import com.qifu.common.utils.Asserts;
import com.qifu.common.utils.SecurityUtils;
import com.qifu.common.utils.StringDateUtil;
import com.qifu.common.utils.StringUtils;
import com.qifu.framework.web.domain.AjaxResult;
import com.qifu.project.system.service.ISysDeptService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 科目Service业务层处理
 *
 *  YM
 * 07-03
 */
@Service
public class BookSubjectServiceImpl extends ServiceImpl<BookSubjectMapper, BookSubject> implements IBookSubjectService {
    @Autowired
    private BookSubjectMapper subjectMapper;

    @Autowired
    private IBookSubjectBalanceRecordService balanceRecordService;

    @Autowired
    private BookVoucherRecordMapper voucherRecordMapper;

    @Autowired
    private SubjectFactory subjectFactory;

    @Autowired
    private AuxiliaryFactory auxiliaryFactory;

    @Autowired
    private IBookSubjectPeriodService subjectPeriodService;

    @Autowired
    private IBookSubjectAuxiliaryService subjectAuxiliaryService;

    @Autowired
    private IBookPeriodService periodService;

    @Autowired
    private IBookVoucherService voucherService;

    @Autowired
    private ISysDeptService sysDeptService;

    /**
     * 初始化科目
     */
    public void insertInitSubject(@Param("bookId")Long bookId, @Param("templateId")Long templateId){
        subjectMapper.insertInitSubject(bookId,templateId);
    }

    public List<BookSubject> getlist(BookSubject bookSubject){
        return subjectMapper.getlist(bookSubject);
    }

    /**
     * 目前只支持4级
     * @param subjectId
     * @return
     */
    public String getSubjectCode(Long subjectId){
        BookSubject subject = subjectMapper.selectById(subjectId);
        Asserts.isNull(subject,"无父级科目，新增失败");
        LambdaQueryWrapper<BookSubject> lqw = new LambdaQueryWrapper<BookSubject>();
        lqw.eq(BookSubject::getBookId,subject.getBookId())
                .eq(BookSubject::getLevel,subject.getLevel()+1)
                .like(BookSubject::getCode,subject.getCode())
                .orderByDesc(BookSubject::getCode)
                .last("limit 1");
        BookSubject subject1 = subjectMapper.selectOne(lqw);
        if(subject1 != null){
            Integer i = Integer.parseInt(subject1.getCode());
            return i+1+"";
        }else{
            String str = subject.getCode()+"01";
            Asserts.isFalse(str.length() > 10,"层级溢出");
            return str;
        }
    }

    /**
     * 修改科目
     * @param bookSubject
     */
    public void edit(BookSubject bookSubject){
        if(bookSubject.getBalanceInit() != null){
            bookSubject.setBalanceInitDate(new Date());
        }
        if(StringUtils.isNotEmpty(bookSubject.getBalanceDirection().name())){
            bookSubject.setBalanceDirectionText(BalanceDirectionEnum.ralanceDirection(bookSubject.getBalanceDirection()));
        }
        bookSubject = auxiliaryFactory.isAuxiliarySet(bookSubject);
        bookSubject.setUpdateTime(new Date());
        BookSubject fatherSubject = subjectFactory.fatherSubject(bookSubject.getCode(),bookSubject.getBookId());
        if(fatherSubject != null && fatherSubject.getFullName() != null){
            bookSubject.setFullName(fatherSubject.getFullName() + '-' + bookSubject.getName());
        } else {
            bookSubject.setFullName(bookSubject.getName());
        }
        int i = subjectMapper.updateById(bookSubject);
        Asserts.isTrue(i == 1,"科目保存失败");
    }

    /**
     * 新增下级科目
     * @param bookSubject
     */
    public void add(BookSubject bookSubject){
        BookSubject oldSubject = subjectFactory.getSubject(bookSubject.getCode(),bookSubject.getBookId());
        Asserts.notNull(oldSubject,"该科目已存在不能重复新增!");
        bookSubject = auxiliaryFactory.isAuxiliarySet(bookSubject);
        BookSubject fatherSubject = subjectFactory.fatherSubject(bookSubject.getCode(),bookSubject.getBookId());
        bookSubject.setFullName(fatherSubject.getFullName() + '-' + bookSubject.getName());
        //新增自己，联动修改父级状态，及继承父级金额
        bookSubject = subjectFactory.addSubject(bookSubject,fatherSubject);
        //YY: 批量修改：父级凭证改为当前科目凭证。
        voucherRecordMapper.updateSubjectId(bookSubject.getSubjectId(),fatherSubject.getSubjectId());
        //YY: 批量修改：父级科目辅助核算余额表数据改为当前科目
        subjectAuxiliaryService.updateOneSubjectId(bookSubject.getSubjectId(),fatherSubject.getSubjectId());
        //YY: 批量修改：父级固定资产涉及得科目改为当前子级科目。
        //fixedAssetCardService.updateFixedAssetFatherSubjectId(bookSubject,fatherSubject);
        //YY: 批量修改：结转规则涉及得科目改为当前子级科目。
    }

    public Map addSubjectCheck(BookSubject bookSubject){
        BookSubject oldSubject = subjectFactory.getSubject(bookSubject.getCode(),bookSubject.getBookId());
        Asserts.notNull(oldSubject,"该科目已存在不能重复新增!");
        BookSubject fatherSubject = subjectFactory.fatherSubject(bookSubject.getCode(),bookSubject.getBookId());
        Map<Object,Object> result = new HashMap<>();
        result.put("Tips",false);
        result.put("TipsContent","");
        if(subjectFactory.isCopyFatherSubject(fatherSubject.getSubjectId())){
            result.put("Tips",true);
            result.put("TipsContent","上级科目已有业务发生，数据会自动转移至该科目。此操作不可逆，谨慎使用。是否继续新增？");
        }
        return result;
    }


    /**
     * 修改科目期初余额，本年累计-贷方余额，本年累计-借方余额父系所有发生变化
     * @param bookSubject
     */
    public void editBalance(BookSubject bookSubject){

        BookSubject oldBookSubject = subjectMapper.selectById(bookSubject.getSubjectId());
        //结过账的期初余额不能修改
        Asserts.isFalse(periodService.isCarryover(oldBookSubject.getBookId()),"您有结过账，不能修改期初余额");
        Asserts.isNull(oldBookSubject,"该科目不存在！");
        if(bookSubject.getBalanceInit() != null){
            //期初余额
            Asserts.isTrue(oldBookSubject.getBalanceInitChecked(),"该科目存在子级，期初余额不能修改");
            BigDecimal differenceBalance = bookSubject.getBalanceInit().subtract(oldBookSubject.getBalanceInit());

            if(StringUtils.isNotNull(differenceBalance) && differenceBalance.doubleValue() != 0){
                BigDecimal oldBalance = oldBookSubject.getBalance();
                BigDecimal newBalance = oldBalance.add(differenceBalance);
                BookSubjectBalanceRecord bookSubjectBalanceRecord = new BookSubjectBalanceRecord(bookSubject.getSubjectId(),
                        BalancePurposeEnum.期初余额变动,oldBalance,newBalance,differenceBalance);
                balanceRecordService.insert(bookSubjectBalanceRecord);

                //更新科目余额账期表的余额。
                subjectPeriodService.updateBalanceBySubjectId(oldBookSubject.getBookId(),oldBookSubject.getSubjectId(),differenceBalance);

                subjectFactory.editPaternalSubject(oldBookSubject,differenceBalance, StringDateUtil.TYPE_1);
                oldBookSubject.setBalanceInitDate(new Date());
                oldBookSubject.setBalance(newBalance);
                oldBookSubject.setBalanceInit(bookSubject.getBalanceInit());
            }
        }
        if(bookSubject.getDebit() != null){
            //本年累计-贷方余额
            Asserts.isTrue(oldBookSubject.getDebitChecked(),"该科目存在子级，本年累计（借方）金额不能修改");
            BigDecimal differenceBalance = bookSubject.getDebit().subtract(oldBookSubject.getDebit());
            if(StringUtils.isNotNull(differenceBalance) && differenceBalance.doubleValue() != 0){
                subjectFactory.editPaternalSubject(oldBookSubject,differenceBalance,StringDateUtil.TYPE_2);
                oldBookSubject.setDebit(bookSubject.getDebit());
            }
        }
        if(bookSubject.getCredit() != null){
            //本年累计-借方余额
            Asserts.isTrue(oldBookSubject.getCreditChecked(),"该科目存在子级，本年累计（贷方）金额不能修改");
            BigDecimal differenceBalance = bookSubject.getCredit().subtract(oldBookSubject.getCredit());
            if(StringUtils.isNotNull(differenceBalance) && differenceBalance.doubleValue() != 0){
                subjectFactory.editPaternalSubject(oldBookSubject,differenceBalance,StringDateUtil.TYPE_3);
                oldBookSubject.setCredit(bookSubject.getCredit());
            }
        }
        oldBookSubject.setNewYearBalance();
        oldBookSubject.setUpdateTime(new Date());
        subjectMapper.updateById(oldBookSubject);
    }

    /**
     * 科目删除
     * @param subject
     */
    public void delete(BookSubject subject){
        if(subject.getDebit() != null && subject.getDebit().compareTo(BigDecimal.ZERO) != 0) {
            BigDecimal differenceBalance = BigDecimal.ZERO.subtract(subject.getDebit());
            subjectFactory.editPaternalSubject(subject, differenceBalance, 2);
        }
        if(subject.getCredit() != null && subject.getCredit().compareTo(BigDecimal.ZERO) != 0){
            BigDecimal differenceBalance = BigDecimal.ZERO.subtract(subject.getCredit());
            subjectFactory.editPaternalSubject(subject,differenceBalance,3);
        }
        int i  = subjectMapper.deleteById(subject.getSubjectId());
        Asserts.isTrue(i == 1,"科目删除失败");

        BookSubject fatherSubject = subjectFactory.fatherSubject(subject.getCode(),subject.getBookId());
        List<BookSubject> list = subjectFactory.getBrotherSubject(fatherSubject,subject.getCode());
        if(list.isEmpty()){
            fatherSubject.setDeletable(Boolean.TRUE);
            fatherSubject.setBalanceInitChecked(Boolean.TRUE);
            fatherSubject.setCreditChecked(Boolean.TRUE);
            fatherSubject.setDebitChecked(Boolean.TRUE);
            fatherSubject.setIsLeaf(Boolean.TRUE);
            subjectMapper.updateById(fatherSubject);
        }
    }

    /**
     * 搜索所有科目-叶子科目
     * @param bookId
     * @return
     */
    public List<BookSubjectVo> searchLeaf(Long bookId){
        return subjectMapper.searchLeaf(bookId);
    }


    /**
     * 搜索所有科目
     * @param bookId
     * @return
     */
    public List<BookSubjectVo> searchOriginSubject(Long bookId){
        return subjectMapper.searchOriginSubject(bookId);
    }

    /**
     * 试算平衡
     * @param bookId
     * @return
     */
    public TrialBalance getTrialBalance(Long bookId){
        TrialBalance trialBalance = subjectMapper.getTrialBalance(bookId);
        if(trialBalance.getImbalance1().doubleValue() == 0 && trialBalance.getImbalance2().doubleValue() == 0){
            trialBalance.setIsImbalance(Boolean.TRUE);
        }else {
            trialBalance.setIsImbalance(Boolean.FALSE);
        }
        return trialBalance;
    }


    /**
     * 校验科目初始余额是否可以修改（1.当前账期不是第一期 2.账户有没有过期）
     * @param bookId
     */
    public Map<String,Object> subjectBalanceIsEdit(Long bookId){
        Map<String,Object> result = new HashMap<>();
        result.put("balanceIsEdit",true);
        result.put("message","操作成功");
        //1.当前账期不是第一期
        if(periodService.isCarryover(bookId)){
            result.put("balanceIsEdit",false);
            result.put("message","当前账期已结账");
            return result;
        }
        //账户已过期
        if(!SecurityUtils.isAdmin() && sysDeptService.checkDeptValidTime(SecurityUtils.companyId()) == 2) {
            result.put("balanceIsEdit",false);
            result.put("message","账户已过期");
            return result;
        }
        return result;
    }

}
