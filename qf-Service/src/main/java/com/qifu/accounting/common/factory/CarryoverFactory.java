package com.qifu.accounting.common.factory;

import com.qifu.accounting.carryover.domain.BookCarryover;
import com.qifu.accounting.carryover.domain.BookCarryoverRule;
import com.qifu.accounting.carryover.mapper.BookCarryoverRuleMapper;
import com.qifu.accounting.common.enums.BalanceDirectionEnum;
import com.qifu.accounting.common.enums.CarryoverCodeEnum;
import com.qifu.accounting.common.enums.DateRuleEnum;
import com.qifu.accounting.common.enums.VoucherTypeEnum;
import com.qifu.accounting.sys.domain.BookPeriod;
import com.qifu.accounting.sys.domain.BookSubject;
import com.qifu.accounting.sys.mapper.BookSubjectMapper;
import com.qifu.accounting.sys.service.IBookPeriodService;
import com.qifu.accounting.voucher.domain.BookVoucher;
import com.qifu.accounting.voucher.domain.BookVoucherRecord;
import com.qifu.accounting.voucher.domain.vo.BookVoucherModelWageVo;
import com.qifu.accounting.voucher.mapper.BookVoucherMapper;
import com.qifu.accounting.voucher.service.IBookVoucherService;
import com.qifu.common.utils.Asserts;
import com.qifu.common.utils.DateUtils;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

/**
 * 结转工厂
 *  yangyang
 * @date 2020/8/11 10:13
 */
@Component
public class CarryoverFactory {
    @Autowired
    private IBookPeriodService periodService;

    @Autowired
    private WageFactory wageFactory;

    @Autowired
    private FixedAssetFactory fixedAssetFactory;

    @Autowired
    private BookSubjectMapper subjectMapper;

    @Autowired
    private SubjectFactory subjectFactory;

    @Autowired
    private BookVoucherMapper voucherMapper;

    @Autowired
    private BookCarryoverRuleMapper carryoverRuleMapper;

    @Autowired
    private IBookVoucherService bookVoucherService;


    /*--------------------------------测算金额---------------------------------*/

    /**
     * 测算每一项结转金额
     * @param carryover
     */
    public void resetCarryover(BookCarryover carryover,Date yearPeriod ){
        //获取当前账期
        Long bookId = carryover.getBookId();
        BookVoucher voucher = new BookVoucher();
        voucher.setBookId(bookId)
                .setVoucherType(VoucherTypeEnum.结转凭证)
                .setTypeClassId(carryover.getCarryoverId())
                .setYearPeriod(yearPeriod);
        List<BookVoucher> list = voucherMapper.getListByCarryoverId(voucher);
        if(!list.isEmpty() && list.size() == 1 && !CarryoverCodeEnum.carryForward.name().equals(carryover.getCode().name())){
            carryover.setVoucher1(list.get(0)).setMoney(list.get(0).getCreditTotal().toString());
            return;
        }
        switch (carryover.getCode()){
            case accruedWages:
                //计提工资
                voucher.setVoucherType(VoucherTypeEnum.计提工资).setTypeClassId(null);
                list = voucherMapper.getListByCarryoverId(voucher);
                if(list.isEmpty()){
                    accruedWages(carryover,bookId,yearPeriod);
                } else {
                    carryover.setVoucher1(list.get(0)).setMoney(list.get(0).getCreditTotal().toString());
                }
                break;
            case fixedAssetDepreciation:
                //计提折旧
                carryover.setMoney(fixedAssetFactory.getBalanceDepreciation(bookId,yearPeriod).toString());
                break;
            case exchangeForward:
                //结转汇兑损益
                break;
            case carryForward:
                //结转损益（注：结转损益有正负放在借贷不同方向得问题，得重新查询明细合计得本年利润科目。YY：如果会计制度是民间非营利，这里需要调整）
                if(!list.isEmpty() && list.size() == 1){
                    BigDecimal balance = voucherMapper.getCarryForwardBalance(bookId,yearPeriod);
                    carryover.setVoucher1(list.get(0)).setMoney(balance.toString());
                }else if(!list.isEmpty() && list.size() == 2){
                    BigDecimal balance = voucherMapper.getCarryForwardBalance(bookId,yearPeriod);
                    carryover.setVoucher1(list.get(0))
                            .setVoucher2(list.get(1))
                            .setMoney(balance.toString());
                }else{
                    carryover.setMoney(getCarryForwardBalance(bookId,yearPeriod).toString());
                }
                break;
            case yearProfit:
                //结转本年利润 也可以用公用的测算，但结转损益必须当前账期是12月才能进行。
                otherCarryover(carryover,bookId,yearPeriod);
                break;
            default:
                //结转销售成本，计提税金，自定义
                otherCarryover(carryover,bookId,yearPeriod);
                break;
        }
    }


    /**
     * 计提工资
     * @param carryover
     * @param bookId
     * @param yearPeriod
     */
    private void accruedWages(BookCarryover carryover,Long bookId,Date yearPeriod){
        BookVoucherModelWageVo voucherModel = wageFactory.provisionWageModel(bookId,yearPeriod, VoucherTypeEnum.计提工资);
        if(voucherModel.getCreditTotal().doubleValue() != voucherModel.getDebitTotal().doubleValue()){
            carryover.setMoney("?");
            carryover.setIsDBBalanceEquals(Boolean.FALSE);
        }else{
            carryover.setMoney(voucherModel.getCreditTotal().toString());
            carryover.setIsDBBalanceEquals(Boolean.TRUE);
        }
    }


    /**
     * 结转损益金额
     * @param bookId
     * @param period
     * @return
     */
    public BigDecimal getCarryForwardBalance(Long bookId, Date period){
        //收入
        BigDecimal carryForwardCreditBalance = voucherMapper.getCarryForwardCreditBalance(bookId,period);
        //成本
        BigDecimal carryForwardDebitBalance = voucherMapper.getCarryForwardDebitBalance(bookId,period);
        //本年利润 = 收入 -  成本
        BigDecimal carryForwardBalance = carryForwardCreditBalance.subtract(carryForwardDebitBalance);
        return carryForwardBalance;
    }

    /**
     * 结转本年利润
     * 结转销售成本，计提税金，自定义
     * @param carryover
     * @param bookId
     * @param yearPeriod
     */
    private void otherCarryover(BookCarryover carryover,Long bookId,Date yearPeriod){
        List<BookCarryoverRule> ruleList = carryoverRuleMapper.getListByCarryoverId(carryover.getCarryoverId());
        BigDecimal debitTotal = new BigDecimal(0);
        BigDecimal creditTotal = new BigDecimal(0);
        Boolean autoBalance = false;
        for (BookCarryoverRule carryoverRule : ruleList) {
            if(carryoverRule.getDirection().name().equals(BalanceDirectionEnum.DEBIT.name())){
                debitTotal = debitTotal.add(resetCarryoverRule(carryoverRule,bookId,yearPeriod ));
            }
            if(carryoverRule.getDirection().name().equals(BalanceDirectionEnum.CREDIT.name())){
                creditTotal = creditTotal.add(resetCarryoverRule(carryoverRule,bookId,yearPeriod ));
            }
            if(DateRuleEnum.自动平衡.name().equals(carryoverRule.getDateRule().name())){
                autoBalance = true;
            }
        }

        if(autoBalance && debitTotal.doubleValue() != creditTotal.doubleValue()){
            //自动平衡的情况:借方金额超过贷方
            BookCarryoverRule rule = carryoverRuleMapper.getLastCarryoverByAutoBalance(carryover.getCarryoverId());
            if(rule != null){
                carryover.setMoney(debitTotal.toString());
                carryover.setIsDBBalanceEquals(Boolean.TRUE);
            }else{
                carryover.setMoney("?");
                carryover.setIsDBBalanceEquals(Boolean.FALSE);
            }
        }else{
            //不考虑自动平衡
            if(debitTotal.doubleValue() != creditTotal.doubleValue()){
                //借贷方金额不平衡。
                carryover.setMoney("?");
                carryover.setIsDBBalanceEquals(Boolean.FALSE);
            } else {
                carryover.setMoney(debitTotal.toString());
                carryover.setIsDBBalanceEquals(Boolean.TRUE);
            }
        }
    }

    /**
     * 查询某一条规则的金额
     * @param carryoverRule
     * @param bookId
     * @return
     */
    private BigDecimal resetCarryoverRule(BookCarryoverRule carryoverRule,Long bookId,Date yearPeriod ){
        BookSubject ruleSubject = subjectFactory.getSubject(carryoverRule.getOriginSubjectCode(),bookId);
        BigDecimal balance = new BigDecimal(0);
        switch (carryoverRule.getDateRule()){
            case 期末余额:
                balance = ruleSubject.getBalance();
                break;
            case 本期发生额:
                BigDecimal debitTotal = voucherMapper.getDebitTotal(carryoverRule.getOriginSubjectCode(),bookId,yearPeriod);
                BigDecimal creditTotal = voucherMapper.getCreditTotal(carryoverRule.getOriginSubjectCode(),bookId,yearPeriod);
                if(ruleSubject.getBalanceDirection().name().equals(BalanceDirectionEnum.DEBIT.name())){
                    balance = debitTotal.subtract(creditTotal);
                }
                if(ruleSubject.getBalanceDirection().name().equals(BalanceDirectionEnum.CREDIT.name())){
                    balance = creditTotal.subtract(debitTotal);
                }
                break;
            case 自动平衡:
                balance = BigDecimal.ZERO;
                break;
        }
        return balance.multiply(carryoverRule.getPercentage()).multiply(new BigDecimal(0.01)).setScale(2, BigDecimal.ROUND_HALF_UP);
    }


    /*-----------------------------------结转生成凭证---------------------------------------*/

    /**
     * 结转生成凭证
     * @param carryover
     */
    public Map<String,Object> generateVoucher(BookCarryover carryover) {
        Map<String,Object> result = new HashMap<>();
        result.put("success",Boolean.TRUE);
        result.put("msg","操作成功");
        BookVoucher voucher = new BookVoucher();
        //获取当前账期
        Long bookId = carryover.getBookId();
        BookPeriod period = periodService.getInfo(bookId);
        Date yearPeriod = period.getYearPeriod();
        BookVoucher voucherParam = new BookVoucher();
        voucherParam.setBookId(bookId);
        voucherParam.setBookId(bookId)
                .setVoucherType(VoucherTypeEnum.结转凭证)
                .setTypeClassId(carryover.getCarryoverId())
                .setYearPeriod(yearPeriod);
        List<BookVoucher> list = voucherMapper.getListByCarryoverId(voucherParam);
        Asserts.isTrue(list.isEmpty(),"该结转模块已生成凭证，不能重复生成");
        switch (carryover.getCode()){
            case accruedWages:
                //计提工资
                result.clear();
                result = wageFactory.wageTemplate(bookId,yearPeriod,VoucherTypeEnum.计提工资);
                break;
            case fixedAssetDepreciation:
                //计提折旧
                fixedAssetFactory.insertDepreciationVoucher(bookId,yearPeriod,carryover.getCarryoverId(),voucher);
                result.put("data",voucher);
                break;
            case exchangeForward:
                //结转汇兑损益
                break;
            case carryForward:
                //结转损益
                BookVoucher voucher2 = new BookVoucher();
                carryForwardAddVoucher(carryover,bookId, yearPeriod,voucher,voucher2);
                Asserts.isFalse(voucher == null && voucher2 ==null,"测算金额为0，不能生成凭证");
                if(voucher != null){
                    result.put("data",voucher);
                }
                if(carryover.getProfitLossOpen() && voucher2 != null){
                    result.put("data2",voucher2);
                }
                break;
            case yearProfit:
                //结转本年利润
                 //Asserts.isTrue(DateUtils.getMonth(yearPeriod) == 12,"当前账期不是本年度最后一期，不能结转本年利润");
                OtherCarryoverAddVoucher(bookId,yearPeriod,carryover.getCarryoverId(),voucher);
                break;
            default:
                //结转销售成本，计提税金，自定义
                OtherCarryoverAddVoucher(bookId,yearPeriod,carryover.getCarryoverId(),voucher);
                result.put("data",voucher);
                break;
        }
        return result;
    }

    /**
     * 结转损益生成凭证
     * @param bookId
     * @param period
     * @return
     */
    private void carryForwardAddVoucher(BookCarryover carryover,Long bookId, Date period,BookVoucher voucher1,BookVoucher voucher2){
        //收入（贷）
        BigDecimal carryForwardCreditBalance = voucherMapper.getCarryForwardCreditBalance(bookId,period);
        //成本（借）
        BigDecimal carryForwardDebitBalance = voucherMapper.getCarryForwardDebitBalance(bookId,period);
        Asserts.isFalse(carryForwardCreditBalance.doubleValue() == 0 && carryForwardDebitBalance.doubleValue() ==0,"测算金额为0，无需生成凭证");
        //本年利润(贷) = 收入（贷） -  成本（借）
        BigDecimal carryForwardBalance = carryForwardCreditBalance.subtract(carryForwardDebitBalance);

        //收入科目凭证（贷）
        List<BookVoucherRecord>  carryForwardCreditVoucherRecords = voucherMapper.getCarryForwardCreditVoucherRecords(bookId,period);
        //成本科目凭证（借）
        List<BookVoucherRecord>  carryForwardDebitVoucherRecords = voucherMapper.getCarryForwardDebitVoucherRecords(bookId,period);

        BookSubject yearProfitSubject = subjectMapper.getYearProfitSubject(bookId);
        Asserts.isFalse(yearProfitSubject.getIsAuxiliary(),"结转科目设置包含辅组核算，不能生成凭证");
        if(carryover.getProfitLossOpen()){

            //分开结转 1.结转收入，2.结转成本。
            //1.结转收入(贷)
            if(carryForwardCreditBalance.doubleValue() != 0){
                addVoucher(carryForwardCreditVoucherRecords,BalanceDirectionEnum.CREDIT,BalanceDirectionEnum.贷,null,
                        carryForwardCreditBalance,carryForwardCreditBalance,yearProfitSubject.getSubjectId(),
                        period,bookId,carryover.getText(),voucher1,carryover.getCarryoverId());
            }

            //2.结转成本(借)
            if(carryForwardDebitBalance.doubleValue() != 0){
                addVoucher(carryForwardDebitVoucherRecords,BalanceDirectionEnum.DEBIT,BalanceDirectionEnum.借,carryForwardDebitBalance,
                        null,carryForwardDebitBalance,yearProfitSubject.getSubjectId(),
                        period,bookId,carryover.getText(),voucher2,carryover.getCarryoverId());
            }

        }else{
            //合起来结转(贷)
            //根据总的盈亏判断本年利润录入在借方还是贷方
            carryForwardCreditVoucherRecords.addAll(carryForwardDebitVoucherRecords);
            if(carryForwardBalance.doubleValue() > 0){
                addVoucher(carryForwardCreditVoucherRecords,BalanceDirectionEnum.CREDIT,BalanceDirectionEnum.贷,null,
                        carryForwardBalance,carryForwardBalance,yearProfitSubject.getSubjectId(),
                        period,bookId,carryover.getText(),voucher1,carryover.getCarryoverId());
            }
            if(carryForwardBalance.doubleValue() < 0){
                addVoucher(carryForwardCreditVoucherRecords,BalanceDirectionEnum.DEBIT,BalanceDirectionEnum.借,carryForwardBalance.multiply(new BigDecimal(-1)),
                        null,carryForwardBalance.multiply(new BigDecimal(-1)),yearProfitSubject.getSubjectId(),
                        period,bookId,carryover.getText(),voucher1,carryover.getCarryoverId());
            }

            Asserts.isFalse(carryForwardBalance.doubleValue() == 0,"测算金额为0，无需生成凭证");
        }
    }

    //结转损益生成凭证
    private void addVoucher(List<BookVoucherRecord> details, BalanceDirectionEnum b, BalanceDirectionEnum btext,
                            BigDecimal debit,BigDecimal credit,BigDecimal total,Long subjectId,
                            Date period, Long bookId, String summary,BookVoucher voucher,Long carryId) {
        BookVoucherRecord voucherRecord = new BookVoucherRecord();
        voucherRecord.setSummary(summary)
                .setDebit(debit)
                .setCredit(credit)
                .setBalanceDirection(b)
                .setBalanceDirectionText(btext)
                .setSubjectId(subjectId);
        details.add(voucherRecord);

        addBookVoucher(voucher,total,total,carryId,period,bookId,details);
    }


    /**
     * 结转本年利润
     * 结转销售成本，计提税金，自定义生成凭证
     * 结转科目涉及到辅组核算给与提示，不能保存凭证。
     * @param bookId
     * @param yearPeriod
     * @param carryoverId
     * @param voucher
     */
    private void OtherCarryoverAddVoucher(Long bookId,Date yearPeriod,Long carryoverId,BookVoucher voucher){
        List<BookCarryoverRule> ruleList = carryoverRuleMapper.getListByCarryoverId(carryoverId);
        BigDecimal debitTotal = new BigDecimal(0);
        BigDecimal creditTotal = new BigDecimal(0);
        List<BookVoucherRecord> details = new ArrayList<>();
        Boolean autoBalance = false;
        for (BookCarryoverRule carryoverRule : ruleList) {
            BookSubject carryoverSubject = subjectFactory.getSubject(carryoverRule.getSubjectCode(),bookId);
            Asserts.isFalse(carryoverSubject.getIsAuxiliary(),"结转科目设置包含辅组核算，不能生成凭证");
            BookVoucherRecord voucherRecord = new BookVoucherRecord();
            voucherRecord.setSummary(carryoverRule.getSummary())
                    .setBalanceDirection(carryoverRule.getDirection())
                    .setBalanceDirectionText(carryoverRule.getDirectionText())
                    .setSubjectId(carryoverSubject.getSubjectId());

            BigDecimal balance = resetCarryoverRule(carryoverRule,bookId,yearPeriod );
            if(carryoverRule.getDirection().name().equals(BalanceDirectionEnum.DEBIT.name())){
                voucherRecord.setDebit(balance);
                debitTotal = debitTotal.add(balance);
            }
            if(carryoverRule.getDirection().name().equals(BalanceDirectionEnum.CREDIT.name())){
                voucherRecord.setCredit(balance);
                creditTotal = creditTotal.add(balance);
            }

            if(DateRuleEnum.自动平衡.name().equals(carryoverRule.getDateRule().name())){
                autoBalance = true;
            }else {
                details.add(voucherRecord);
            }

        }

        BookVoucherRecord voucherRecord = new BookVoucherRecord();
        if(autoBalance && debitTotal.doubleValue() != creditTotal.doubleValue()){
            //自动平衡的情况:
            BookCarryoverRule rule = carryoverRuleMapper.getLastCarryoverByAutoBalance(carryoverId);
            Asserts.isNull(rule,"借贷方金额不平衡");
            BookSubject carryoverSubject = subjectFactory.getSubject(rule.getSubjectCode(),bookId);

            voucherRecord.setSummary(rule.getSummary())
                    .setBalanceDirection(rule.getDirection())
                    .setBalanceDirectionText(rule.getDirectionText())
                    .setSubjectId(carryoverSubject.getSubjectId());

            if(BalanceDirectionEnum.DEBIT.name().equals(rule.getDirection().name())){
                Asserts.isFalse(creditTotal.doubleValue() == 0,"借贷方金额不平衡");
                voucherRecord.setDebit(creditTotal.subtract(debitTotal));
                debitTotal = creditTotal;
            }
            if(BalanceDirectionEnum.CREDIT.name().equals(rule.getDirection().name())){
                Asserts.isFalse(debitTotal.doubleValue() == 0,"借贷方金额不平衡");
                voucherRecord.setCredit(debitTotal.subtract(creditTotal));
                creditTotal = debitTotal;
            }
            details.add(voucherRecord);
            addBookVoucher(voucher,debitTotal,creditTotal,carryoverId,yearPeriod,bookId,details);
        }else{
            //不考虑自动平衡
            Asserts.isFalse(debitTotal.doubleValue() != creditTotal.doubleValue(),"借贷方金额不平衡");
            Asserts.isFalse(debitTotal.doubleValue() == 0,"测算金额为0，无需生成凭证");
            addBookVoucher(voucher,debitTotal,creditTotal,carryoverId,yearPeriod,bookId,details);

        }
    }




    //结转生成凭证
    private void addBookVoucher(BookVoucher voucher,BigDecimal debitTotal,BigDecimal creditTotal,
                                Long carryoverId,Date period,Long bookId,List<BookVoucherRecord> details){
        voucher.setDebitTotal(debitTotal)
                .setCreditTotal(creditTotal)
                .setTypeClassId(carryoverId)
                .setVoucherType(VoucherTypeEnum.结转凭证)
                .setYearPeriod(period)
                .setBookId(bookId)
                .setDetails(details)
                .setVoucherWord("记")
                .setVoucherDate(DateUtils.getLastDayByYearPeriod(period));
        bookVoucherService.add(voucher);
    }

}
