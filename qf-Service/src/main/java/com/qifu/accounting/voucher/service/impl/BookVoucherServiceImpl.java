package com.qifu.accounting.voucher.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qifu.accounting.common.domain.Page;
import com.qifu.accounting.sys.domain.BookSubject;
import com.qifu.accounting.sys.domain.vo.BookAuxiliaryVo;
import com.qifu.accounting.sys.mapper.BookSubjectMapper;
import com.qifu.accounting.sys.service.IBookAuxiliaryService;
import com.qifu.accounting.voucher.domain.BookVoucherRecord;
import com.qifu.accounting.voucher.domain.vo.BookVoucherParams;
import com.qifu.accounting.voucher.domain.vo.BookVoucherRecordVo;
import com.qifu.accounting.voucher.domain.vo.BookVoucherVo;
import com.qifu.accounting.common.enums.BalanceDirectionEnum;
import com.qifu.accounting.common.enums.BalancePurposeEnum;
import com.qifu.accounting.common.factory.VoucherFactory;
import com.qifu.accounting.voucher.mapper.BookVoucherRecordMapper;
import com.qifu.common.utils.Asserts;
import com.qifu.common.utils.SecurityUtils;
import com.qifu.common.utils.StringDateUtil;
import com.qifu.common.utils.StringUtils;
import com.qifu.framework.aspectj.lang.annotation.DataScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qifu.accounting.voucher.mapper.BookVoucherMapper;
import com.qifu.accounting.voucher.domain.BookVoucher;
import com.qifu.accounting.voucher.service.IBookVoucherService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 凭证总Service业务层处理
 *
 *  YM
 * 07-13
 */
@Service
public class BookVoucherServiceImpl extends ServiceImpl<BookVoucherMapper, BookVoucher> implements IBookVoucherService {
    @Autowired
    private BookVoucherMapper voucherMapper;

    @Autowired
    private BookVoucherRecordMapper voucherRecordMapper;

    @Autowired
    private VoucherFactory voucherFactory;
    @Autowired
    private IBookAuxiliaryService auxiliaryService;

    @Autowired
    private BookSubjectMapper subjectMapper;




    /**
     * 查询凭证
     * @param params 当前账期，凭证code
     * @return
     */
    public List<BookVoucherVo> getList(BookVoucherParams params, Page page){
        List<BookVoucherVo> list = voucherMapper.getList(params, page);
        list.forEach(bookVoucherVo -> {
            List<BookVoucherRecordVo> recordVos = voucherRecordMapper.ListByVoucherId(bookVoucherVo.getVoucherId());
            recordVos.forEach(bookVoucherRecordVo -> {
                List<BookAuxiliaryVo> auxiliaryVos = auxiliaryService.getListByVoucherRecord(bookVoucherRecordVo.getVoucherRecordId());
                bookVoucherRecordVo.setAuxiliarys(auxiliaryVos);
            });
            bookVoucherVo.setDetails(recordVos);
        });
        return list;
    }
    public int getListTotal( BookVoucherParams params){
        return voucherMapper.getListTotal(params);
    }

    /**
     * 凭证详情
     * @param voucherId
     * @return
     */
    public BookVoucher getInfo(Long voucherId){
        BookVoucher bookVoucher = voucherMapper.selectById(voucherId);
        List<BookVoucherRecordVo> recordVos = voucherRecordMapper.ListByVoucherId(voucherId);
        recordVos.forEach(bookVoucherRecordVo -> {
            List<BookAuxiliaryVo> auxiliaryVos = auxiliaryService.getListByVoucherRecord(bookVoucherRecordVo.getVoucherRecordId());
            bookVoucherRecordVo.setAuxiliarys(auxiliaryVos);
        });
        bookVoucher.setDetail(recordVos);
        return bookVoucher;
    }

    /**
     * 新增凭证
     * @param bookVoucher
     */
    public BookVoucher add(BookVoucher bookVoucher){
        bookVoucher.setCreateTime(new Date());
        bookVoucher.setUpdateTime(new Date());
        bookVoucher.setCreateUserId(SecurityUtils.userId());
        bookVoucher.setCreateUserName(SecurityUtils.getUsername());
        if(bookVoucher.getVoucherNo()==null){
            bookVoucher.setVoucherNo(getCode(bookVoucher));
        }
        bookVoucher.setVoucherCode();
        bookVoucher.setDelFlag(StringDateUtil.Del_FLAG_0);
        int i = voucherMapper.insert(bookVoucher);
        Asserts.isTrue(i == 1,"添加凭证失败");

        List<BookVoucherRecord> records = bookVoucher.getDetails();
        records.forEach(bookVoucherRecord -> {
            verification(bookVoucherRecord);
            voucherFactory.updateBalance(bookVoucherRecord,StringDateUtil.Del_FLAG_0,BalancePurposeEnum.录凭证);
            bookVoucherRecord.setVoucherRecordId(null);
            bookVoucherRecord.setVoucherId(bookVoucher.getVoucherId());
            bookVoucherRecord.setCreateTime(new Date());
            bookVoucherRecord.setUpdateTime(new Date());
            bookVoucherRecord.setBalanceDirectionText(BalanceDirectionEnum.ralanceDirection(bookVoucherRecord.getBalanceDirection()));
            int e = voucherRecordMapper.insert(bookVoucherRecord);
            Asserts.isTrue(e == 1,"添加凭证失败");
        });
        return bookVoucher;
    }

    //录凭证校验辅助核算是否完整
    private void verification(BookVoucherRecord bookVoucherRecord){
        BookSubject subject = subjectMapper.selectById(bookVoucherRecord.getSubjectId());
        if(subject.getIsAuxiliary()){
            if(subject.getIsSupplier()){
                Asserts.isNull(bookVoucherRecord.getSupplierId(),"科目【"+subject.getFullName()+"】已开启辅助核算，请检查录入完整信息");
            }
            if(subject.getIsCustomer()){
                Asserts.isNull(bookVoucherRecord.getCustomerId(),"科目【"+subject.getFullName()+"】已开启辅助核算，请检查录入完整信息");
            }
            if(subject.getIsPerson()){
                Asserts.isNull(bookVoucherRecord.getPersonId(),"科目【"+subject.getFullName()+"】已开启辅助核算，请检查录入完整信息");
            }
            if(subject.getIsProject()){
                Asserts.isNull(bookVoucherRecord.getProjectId(),"科目【"+subject.getFullName()+"】已开启辅助核算，请检查录入完整信息");
            }
            if(subject.getIsDepartment()){
                Asserts.isNull(bookVoucherRecord.getDepartmentId(),"科目【"+subject.getFullName()+"】已开启辅助核算，请检查录入完整信息");
            }
            if(subject.getIsStock()){
                Asserts.isNull(bookVoucherRecord.getStockId(),"科目【"+subject.getFullName()+"】已开启辅助核算，请检查录入完整信息");
            }
        }
    }

    /**
     * 修改凭证
     * @param bookVoucher
     */
    public void edit(BookVoucher bookVoucher) {
        bookVoucher.setUpdateTime(new Date());
        int i = voucherMapper.updateById(bookVoucher);
        Asserts.isTrue(i == 1, "修改凭证失败");
        List<BookVoucherRecord> oldVoucherRecords = voucherRecordMapper.selectListByVoucherId(bookVoucher.getVoucherId());
        //先删除旧的凭证
        updateDelFlag(oldVoucherRecords,BalancePurposeEnum.修改凭证);

        List<BookVoucherRecord> records = bookVoucher.getDetails();
        records.forEach(bookVoucherRecord -> {
                voucherFactory.updateBalance(bookVoucherRecord,StringDateUtil.Del_FLAG_0,BalancePurposeEnum.修改凭证);
                bookVoucherRecord.setVoucherId(bookVoucher.getVoucherId());
                bookVoucherRecord.setCreateTime(new Date());
                bookVoucherRecord.setUpdateTime(new Date());
                bookVoucherRecord.setBalanceDirectionText(BalanceDirectionEnum.ralanceDirection(bookVoucherRecord.getBalanceDirection()));
                int e = voucherRecordMapper.insert(bookVoucherRecord);
                Asserts.isTrue(e == 1,"修改凭证失败");
        });
    }

    /**
     * 删除凭证
     * @param voucherIds
     */
    public void delete(Long[] voucherIds){
        for (int i = 0; i < voucherIds.length; i++) {
            BookVoucher voucher = voucherMapper.selectById(voucherIds[i]);
            Asserts.isFalse(voucher.getStatus() == 1,"该凭证已审核不能进行操作");
            voucher.setDelFlag(StringDateUtil.Del_FLAG_2);
            voucher.setUpdateTime(new Date());
            int e = voucherMapper.updateById(voucher);
            Asserts.isTrue(e == 1,"删除凭证失败");

            List<BookVoucherRecord> voucherRecords = voucherRecordMapper.selectListByVoucherId(voucherIds[i]);
            updateDelFlag(voucherRecords,BalancePurposeEnum.删除凭证);
        }
    }

    //删除单个凭证
    private void updateDelFlag(List<BookVoucherRecord> voucherRecords,BalancePurposeEnum purpose){
        voucherRecords.forEach(record -> {
            voucherFactory.updateBalance(record,StringDateUtil.Del_FLAG_2,purpose);
            record.setDelFlag(StringDateUtil.Del_FLAG_2);
            record.setUpdateTime(new Date());
            voucherRecordMapper.updateById(record);
        });
    }


    /**
     * 获取code
     * @param bookVoucher
     * @return
     */
    public Long getCode(BookVoucher bookVoucher){
        LambdaQueryWrapper<BookVoucher> lqw = new LambdaQueryWrapper<BookVoucher>();
        lqw.eq(BookVoucher::getBookId,bookVoucher.getBookId())
                .eq(BookVoucher::getVoucherWord,bookVoucher.getVoucherWord())
                .eq(BookVoucher::getDelFlag,StringDateUtil.Del_FLAG_0)
                .eq(BookVoucher::getYearPeriod,bookVoucher.getYearPeriod())
                .orderByDesc(BookVoucher::getVoucherNo)
                .last("limit 1");
        BookVoucher voucher = voucherMapper.selectOne(lqw);
        if(voucher == null || voucher.getVoucherNo() == null){
            return (long)1;
        }
        return voucher.getVoucherNo() + 1;
    }

    /**
     * 审核
     * @param bookVoucher
     */
    public void review(BookVoucher bookVoucher){
        int i = voucherMapper.updateById(bookVoucher);
        Asserts.isTrue(i == 1,"凭证审核失败");
    }

    /**
     * 根据科目查询是否有凭证
     * 有：返回true,没有：返回false
     */
    public boolean haveVoucherBySubject(Long subjectId){
        int i = voucherRecordMapper.haveVoucherBySubject(subjectId);
        if(i > 0){
            return true;
        }else {
            return false;
        }
    }
}
