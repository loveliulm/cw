package com.qifu.accounting.sys.service.impl;

import com.qifu.accounting.common.enums.AuxiliaryTypeEnum;
import com.qifu.accounting.sys.domain.BookSubject;
import com.qifu.accounting.sys.domain.vo.BookSubjectAuxiliaryVo;
import com.qifu.accounting.sys.mapper.BookSubjectMapper;
import com.qifu.accounting.sys.service.IBookAuxiliaryPeriodService;
import com.qifu.accounting.sys.service.IBookSubjectService;
import com.qifu.accounting.voucher.service.IBookVoucherService;
import com.qifu.common.utils.Asserts;
import com.qifu.common.utils.StringDateUtil;
import com.qifu.framework.web.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qifu.accounting.sys.mapper.BookSubjectAuxiliaryMapper;
import com.qifu.accounting.sys.domain.BookSubjectAuxiliary;
import com.qifu.accounting.sys.service.IBookSubjectAuxiliaryService;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 科目辅组核算关联Service业务层处理
 *
 *
 *
 */
@Service
public class BookSubjectAuxiliaryServiceImpl extends ServiceImpl<BookSubjectAuxiliaryMapper, BookSubjectAuxiliary> implements IBookSubjectAuxiliaryService {

    @Autowired
    private BookSubjectAuxiliaryMapper subjectAuxiliaryMapper;
    @Autowired
    private BookSubjectMapper bookSubjectMapper;
    @Autowired
    private IBookSubjectService subjectService;
    @Autowired
    private IBookVoucherService voucherService;
    @Autowired
    private IBookAuxiliaryPeriodService auxiliaryPeriodService;

    public List<BookSubjectAuxiliary> getList(BookSubjectAuxiliary bookSubjectAuxiliary){
        return subjectAuxiliaryMapper.getList(bookSubjectAuxiliary);
    }

    /**
     * 科目辅组核算余额保存
     * @param subjectAuxiliaryVo
     */
    public void add(BookSubjectAuxiliaryVo subjectAuxiliaryVo) throws Exception {
        List<BookSubjectAuxiliary> supplierList = subjectAuxiliaryVo.getSupplierList();
        List<BookSubjectAuxiliary> customerList = subjectAuxiliaryVo.getCustomerList();
        List<BookSubjectAuxiliary> projectList = subjectAuxiliaryVo.getProjectList();
        List<BookSubjectAuxiliary> departmentList = subjectAuxiliaryVo.getDepartmentList();
        List<BookSubjectAuxiliary> personList = subjectAuxiliaryVo.getPersonList();
        List<BookSubjectAuxiliary> stockList = subjectAuxiliaryVo.getStockList();
        BookSubject bookSubject = bookSubjectMapper.selectById(subjectAuxiliaryVo.getSubjectId());
        if(voucherService.haveVoucherBySubject(bookSubject.getSubjectId())){
            //科目余额发生变动，更新不能修改字段
            bookSubject.setIsEdit(Boolean.FALSE);
            subjectService.updateById(bookSubject);
            throw new Exception("科目【"+bookSubject.getCode()+":"+bookSubject.getName()+"】余额发生变动，不能对其进行修改!");
        }
        bookSubject.setBalanceInit(null);
        bookSubject.setCredit(null);
        bookSubject.setDebit(null);
        if(bookSubject.getIsSupplier()){
            insert(supplierList,bookSubject, AuxiliaryTypeEnum.供应商);
        }
        if(bookSubject.getIsCustomer()){
            insert(customerList,bookSubject,AuxiliaryTypeEnum.客户);
        }
        if(bookSubject.getIsProject()){
            insert(projectList,bookSubject,AuxiliaryTypeEnum.项目);
        }
        if(bookSubject.getIsDepartment()){
            insert(departmentList,bookSubject,AuxiliaryTypeEnum.部门);
        }
        if(bookSubject.getIsPerson()){
            insert(personList,bookSubject,AuxiliaryTypeEnum.人员);
        }
        if(bookSubject.getIsStock()){
            insert(stockList,bookSubject,AuxiliaryTypeEnum.存货);
        }
        subjectService.editBalance(bookSubject);
    }

    /**
     * 新增每一类型辅助核算
     * @param list
     * @param subject
     * @param type
     */
    private void insert(List<BookSubjectAuxiliary> list,BookSubject subject,AuxiliaryTypeEnum type){
        //Asserts.isFalse(list.isEmpty(),"【"+type+"】辅组核算不能为空");
        BigDecimal balance = BigDecimal.ZERO;
        BigDecimal credit = BigDecimal.ZERO;
        BigDecimal debit = BigDecimal.ZERO;
        for (BookSubjectAuxiliary bookSubjectAuxiliary : list) {
            if(bookSubjectAuxiliary.getBalance() == null){
                bookSubjectAuxiliary.setBalance(BigDecimal.ZERO);
            }
            if(bookSubjectAuxiliary.getCredit() == null){
                bookSubjectAuxiliary.setCredit(BigDecimal.ZERO);
            }
            if(bookSubjectAuxiliary.getDebit() == null){
                bookSubjectAuxiliary.setDebit(BigDecimal.ZERO);
            }
            if(bookSubjectAuxiliary.getAuxiliaryId() == null){
                Asserts.isFalse(bookSubjectAuxiliary.getBalance().doubleValue() != 0 ||
                        bookSubjectAuxiliary.getCredit().doubleValue() != 0 ||
                        bookSubjectAuxiliary.getDebit().doubleValue() != 0,
                        "【"+type+"】辅组核算录入不完整！");
            }else{
                balance = balance.add(bookSubjectAuxiliary.getBalance());
                credit = credit.add(bookSubjectAuxiliary.getCredit());
                debit = debit.add(bookSubjectAuxiliary.getDebit());
                bookSubjectAuxiliary.setSubjectId(subject.getSubjectId());
                bookSubjectAuxiliary.setDelFlag(StringDateUtil.Del_FLAG_0);
                bookSubjectAuxiliary.setCreateTime(new Date());
                bookSubjectAuxiliary.setUpdateTime(new Date());
                //更新已结账辅助核算的金额-增加
                auxiliaryPeriodService.updateBalance(bookSubjectAuxiliary.getSubjectId(),bookSubjectAuxiliary.getBalance(),bookSubjectAuxiliary.getAuxiliaryId());
                subjectAuxiliaryMapper.insert(bookSubjectAuxiliary);
            }
        }
        //Asserts.isFalse(balance.doubleValue() ==0 && credit.doubleValue() == 0 && debit.doubleValue() == 0,"【"+type+"】辅助核算余额不能为空");
        Asserts.isFalse(subject.getBalanceInit() != null && subject.getBalanceInit().doubleValue() != balance.doubleValue(),"辅助核算项目之间的期初余额不平衡");
        Asserts.isFalse(subject.getCredit() != null && subject.getCredit().doubleValue() != credit.doubleValue(),"辅助核算项目之间的本年累计贷方不平衡");
        Asserts.isFalse(subject.getDebit() != null && subject.getDebit().doubleValue() != debit.doubleValue(),"辅助核算项目之间的本年累计借方不平衡");
        subject.setBalanceInit(balance);
        subject.setCredit(credit);
        subject.setDebit(debit);
    }


    /**
     * 修改
     * @param subjectAuxiliaryVo
     */
    public void edit(BookSubjectAuxiliaryVo subjectAuxiliaryVo) throws Exception{
        //删除老的数据
        BookSubjectAuxiliary auxiliary = new BookSubjectAuxiliary();
        auxiliary.setSubjectId(subjectAuxiliaryVo.getSubjectId());
        List<BookSubjectAuxiliary> list = subjectAuxiliaryMapper.getList(auxiliary);
        for (BookSubjectAuxiliary subjectAuxiliary : list) {
            //更新已结账辅助核算的金额-减少
            auxiliaryPeriodService.updateBalance(subjectAuxiliary.getSubjectId(),subjectAuxiliary.getBalance().multiply(new BigDecimal(-1)),subjectAuxiliary.getAuxiliaryId());
            subjectAuxiliary.setDelFlag(StringDateUtil.Del_FLAG_2);
            subjectAuxiliaryMapper.updateById(subjectAuxiliary);
        }
        //新增新的数据
        add(subjectAuxiliaryVo);
    }

    /**
     * 提示判断
     * @param subjectId
     */
    public void judge(Long subjectId){
        Asserts.isNull(subjectId,"科目id不能为空");
        BookSubject subject = bookSubjectMapper.selectById(subjectId);
        Asserts.isTrue(subject.getIsAuxiliary(),"该科目未开启辅助核算，操作失败");
    }

    //批量更新科目id
    public void updateOneSubjectId(Long subjectId,Long fatherSubjectId){
        subjectAuxiliaryMapper.updateOneSubjectId(subjectId,fatherSubjectId);
    }


}
