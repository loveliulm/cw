package com.qifu.accounting.common.factory;

import com.qifu.accounting.common.enums.BalanceDirectionEnum;
import com.qifu.accounting.common.enums.SectionEnum;
import com.qifu.accounting.common.enums.VoucherTypeEnum;
import com.qifu.accounting.sys.domain.BookSubject;
import com.qifu.accounting.sys.domain.vo.BookAuxiliaryVo;
import com.qifu.accounting.sys.mapper.BookSubjectMapper;
import com.qifu.accounting.voucher.domain.BookVoucher;
import com.qifu.accounting.voucher.domain.BookVoucherModel;
import com.qifu.accounting.voucher.domain.BookVoucherModelRecord;
import com.qifu.accounting.voucher.domain.BookVoucherRecord;
import com.qifu.accounting.voucher.domain.vo.BookVoucherModelRecordVo;
import com.qifu.accounting.voucher.domain.vo.BookVoucherModelWageRecordVo;
import com.qifu.accounting.voucher.domain.vo.BookVoucherModelWageVo;
import com.qifu.accounting.voucher.mapper.BookVoucherMapper;
import com.qifu.accounting.voucher.mapper.BookVoucherModelMapper;
import com.qifu.accounting.voucher.mapper.BookVoucherModelRecordMapper;
import com.qifu.accounting.voucher.service.IBookVoucherModelService;
import com.qifu.accounting.voucher.service.IBookVoucherService;
import com.qifu.accounting.wage.domain.BookWage;
import com.qifu.accounting.wage.mapper.BookWageMapper;
import com.qifu.common.utils.Asserts;
import com.qifu.common.utils.DateUtils;
import com.qifu.common.utils.StringUtils;
import com.qifu.common.utils.bean.BeanUtils;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

/**
 *  yangyang
 * @date 2020/8/11 10:16
 */
@Component
public class WageFactory {
    @Autowired
    private BookVoucherModelMapper voucherModelMapper;

    @Autowired
    private BookVoucherModelRecordMapper voucherModelRecordMapper;

    @Autowired
    private IBookVoucherModelService voucherModelService;

    @Autowired
    private IBookVoucherService bookVoucherService;

    @Autowired
    private BookWageMapper wageMapper;

    @Autowired
    private BookSubjectMapper subjectMapper;

    @Autowired
    private BookVoucherMapper voucherMapper;


    /**
     * 计提、发放工资
     * @param bookId
     * @param yearPeriod
     * @param typeEnum 计提工资、发放工资
     * @return
     */
    public Map<String,Object> wageTemplate(Long bookId,Date yearPeriod,VoucherTypeEnum typeEnum){
        Map<String,Object> result = new HashMap<>();
        BookVoucherModelWageVo voucherModel = provisionWageModel(bookId,yearPeriod,typeEnum);
        //1.借贷方是否平衡，2.辅助核算是否完成。
        if((voucherModel.getCreditTotal().doubleValue() != voucherModel.getDebitTotal().doubleValue()) ||
                (voucherModel.getIsAuxiliaryFinished() != null && !voucherModel.getIsAuxiliaryFinished())){
            //如果不等返回模板显示到录凭证页面  success:false,生成凭证失败，返回凭证模板。true，生成凭证成功，返回录好的凭证。
            result.put("success",Boolean.FALSE);
            result.put("msg",typeEnum.name()+"借贷不平衡或者辅助核算信息不完整");
            result.put("data",voucherModel);
        }else{
            Asserts.isFalse(voucherModel.getCreditTotal().doubleValue() == 0,"金额为0，不能生成凭证");
            //新增凭证
            BookVoucher voucher = new BookVoucher();
            addVoucherWage(voucher,bookId,yearPeriod,voucherModel,typeEnum);
            result.put("success",Boolean.TRUE);
            result.put("msg","操作成功");
            result.put("data",voucher);
        }
        return result;
    }

    /**
     * 新增计提、发放工资
     * @param voucher
     * @param bookId
     * @param yearPeriod
     * @param voucherModel
     * @param typeEnum 计提工资、发放工资
     */
    public void addVoucherWage(BookVoucher voucher,Long bookId,Date yearPeriod,BookVoucherModelWageVo voucherModel,VoucherTypeEnum typeEnum){
        BookVoucher voucherParam = new BookVoucher();
        voucherParam.setBookId(bookId).setVoucherType(typeEnum).setYearPeriod(yearPeriod);
        List<BookVoucher> list = voucherMapper.getListByCarryoverId(voucherParam);
        Asserts.isTrue(list.isEmpty(),typeEnum+"已生成凭证，不能重复生成");

        voucher.setBookId(bookId)
                .setYearPeriod(yearPeriod)
                .setCreditTotal(voucherModel.getCreditTotal())
                .setDebitTotal(voucherModel.getDebitTotal())
                .setVoucherWord("记")
                .setVoucherType(typeEnum)
                .setVoucherDate(DateUtils.getLastDayByYearPeriod(yearPeriod));
        List<BookVoucherRecord> details = new ArrayList<>();
        List<BookVoucherModelWageRecordVo> modelRecordVos = voucherModel.getDetail();
        for (BookVoucherModelWageRecordVo modelRecordVo : modelRecordVos) {
            BookVoucherRecord voucherRecord = new  BookVoucherRecord();
            BeanUtils.copyBeanProp(voucherRecord,modelRecordVo);
            details.add(voucherRecord);
        }
        voucher.setDetails(details);
        bookVoucherService.add(voucher);
    }

    /**
     * 获取计提、发放工资模板并计算出金额
     * @param typeEnum 计提工资、发放工资
     * @return
     */
    public BookVoucherModelWageVo provisionWageModel(Long bookId,Date yearPeriod,VoucherTypeEnum typeEnum){
        BigDecimal creditTotal = new BigDecimal(0);
        BigDecimal debitTotal = new BigDecimal(0);
        //获取计提工资凭证模板。
        BookVoucherModel voucherModel = voucherModelMapper.getVoucherModelByWage(bookId,typeEnum);
        if(voucherModel.getSection().name().equals(SectionEnum.上月.name())){
            //上月
            yearPeriod = DateUtils.getPreviousMonth(yearPeriod);
        }
        BookVoucherModelWageVo voucherModelWageVo = voucherModelService.getInfoByWage(voucherModel.getModelId());
        //每一项总金额
        BookWage wage = wageMapper.getTotalBalanceByWageTotal(yearPeriod,bookId);
        for (BookVoucherModelWageRecordVo bookVoucherModelRecordVo : voucherModelWageVo.getDetail()) {
            if(bookVoucherModelRecordVo.getBalanceDirection().name().equals(BalanceDirectionEnum.CREDIT.name())){
                creditTotal = getTotalBalance(bookVoucherModelRecordVo,wage,creditTotal);
                bookVoucherModelRecordVo.setCredit(getTotalBalance(bookVoucherModelRecordVo,wage,BigDecimal.ZERO));
            }
            if(bookVoucherModelRecordVo.getBalanceDirection().name().equals(BalanceDirectionEnum.DEBIT.name())){
                debitTotal = getTotalBalance(bookVoucherModelRecordVo,wage,debitTotal);
                bookVoucherModelRecordVo.setDebit(getTotalBalance(bookVoucherModelRecordVo,wage,BigDecimal.ZERO));
            }
            //判断是否是辅助核算，是否设置。
            if(!isAuxiliaryFinished(bookVoucherModelRecordVo)){
                voucherModelWageVo.setIsAuxiliaryFinished(Boolean.FALSE);
            }
        }
        voucherModelWageVo.setCreditTotal(creditTotal);
        voucherModelWageVo.setDebitTotal(debitTotal);
        return voucherModelWageVo;
    }

    private Boolean isAuxiliaryFinished(BookVoucherModelWageRecordVo recordVo) {
        BookSubject bookSubject = subjectMapper.selectById(recordVo.getSubjectId());
        List<BookAuxiliaryVo> auxiliarys = recordVo.getAuxiliarys();
        BookVoucherModelRecord record = voucherModelRecordMapper.selectById(recordVo.getModelRecordId());
        if(bookSubject.getIsAuxiliary()){
            if(auxiliarys.isEmpty()){
                return false;
            }
            if((StringUtils.isNotNull(bookSubject.getIsCustomer()) && bookSubject.getIsCustomer()) && StringUtils.isNull(record.getCustomerId())){
                return false;
            }
            if((StringUtils.isNotNull(bookSubject.getIsPerson()) && bookSubject.getIsPerson()) && StringUtils.isNull(record.getPersonId())){
                return false;
            }
            if((StringUtils.isNotNull(bookSubject.getIsDepartment()) && bookSubject.getIsDepartment()) && StringUtils.isNull(record.getDepartmentId())){
                return false;
            }
            if((StringUtils.isNotNull(bookSubject.getIsStock()) && bookSubject.getIsStock()) && StringUtils.isNull(record.getStockId())){
                return false;
            }
            if((StringUtils.isNotNull(bookSubject.getIsSupplier()) && bookSubject.getIsSupplier()) && StringUtils.isNull(record.getSupplierId())){
                return false;
            }
            if((StringUtils.isNotNull(bookSubject.getIsProject()) && bookSubject.getIsProject()) && StringUtils.isNull(record.getProjectId())){
                return false;
            }
        }
        return true;
    }

    /**
     * 计算计提、发放工资金额
     * @param bookVoucherModelRecordVo
     * @param wage
     * @param balanceTotal
     * @return
     */
    private BigDecimal getTotalBalance(BookVoucherModelWageRecordVo bookVoucherModelRecordVo,BookWage wage,BigDecimal balanceTotal){
        switch (bookVoucherModelRecordVo.getFiledCode()){
            case MEDICARE_INSURANCE:
                balanceTotal = balanceTotal.add(wage.getMedicareInsurance());
                break;
            case ENDOWMENT_INSURANCE:
                balanceTotal = balanceTotal.add(wage.getEndowmentInsurance());
                break;
            case UNEMPLOYED_INSURANCE:
                balanceTotal = balanceTotal.add(wage.getUnemployedInsurance());
                break;
            case SICKNESS_INSURANCE:
                balanceTotal = balanceTotal.add(wage.getSicknessInsurance());
                break;
            case HOUSING_FUND:
                balanceTotal = balanceTotal.add(wage.getHousingFund());
                break;
            case WITH_HOLDING_TAX:
                balanceTotal = balanceTotal.add(wage.getWithHoldingTax());
                break;
            case NET_PAY:
                balanceTotal = balanceTotal.add(wage.getNetPay());
                break;
            case GROSS_PAY:
                balanceTotal = balanceTotal.add(wage.getGrossPay());
                break;
            case OTHER_DEDUCTIONS:
                balanceTotal = balanceTotal.add(wage.getOtherDeductions());
                break;
        }
        return balanceTotal;
    }
}
