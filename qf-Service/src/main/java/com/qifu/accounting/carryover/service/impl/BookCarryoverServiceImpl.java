package com.qifu.accounting.carryover.service.impl;

import com.qifu.accounting.carryover.domain.BookCarryoverRule;
import com.qifu.accounting.carryover.mapper.BookCarryoverRuleMapper;
import com.qifu.accounting.common.enums.BalanceDirectionEnum;
import com.qifu.accounting.common.enums.CarryoverCodeEnum;
import com.qifu.accounting.common.enums.PeriodStatusEnum;
import com.qifu.accounting.common.factory.CarryoverFactory;
import com.qifu.accounting.sys.domain.BookPeriod;
import com.qifu.accounting.sys.domain.vo.TrialBalance;
import com.qifu.accounting.sys.mapper.BookPeriodMapper;
import com.qifu.accounting.sys.mapper.BookSubjectMapper;
import com.qifu.accounting.sys.mapper.BookSubjectPeriodMapper;
import com.qifu.accounting.sys.mapper.BookSubjectPeriodTemporaryMapper;
import com.qifu.accounting.sys.service.*;
import com.qifu.accounting.voucher.mapper.BookVoucherMapper;
import com.qifu.common.utils.Asserts;
import com.qifu.common.utils.DateUtils;
import com.qifu.common.utils.StringDateUtil;
import com.qifu.common.utils.StringUtils;
import com.qifu.project.customer.domain.TAccountBook;
import com.qifu.project.customer.mapper.TAccountBookMapper;
import net.bytebuddy.asm.Advice;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qifu.accounting.carryover.mapper.BookCarryoverMapper;
import com.qifu.accounting.carryover.domain.BookCarryover;
import com.qifu.accounting.carryover.service.IBookCarryoverService;

import java.math.BigDecimal;
import java.util.*;

/**
 * 结转Service业务层处理
 *
 *  YM
 * 07-23
 */
@Service
public class BookCarryoverServiceImpl extends ServiceImpl<BookCarryoverMapper, BookCarryover> implements IBookCarryoverService {


    @Autowired
    private BookCarryoverMapper carryoverMapper;

    @Autowired
    private CarryoverFactory carryoverFactory;

    @Autowired
    private BookCarryoverRuleMapper carryoverRuleMapper;

    @Autowired
    private BookSubjectMapper bookSubjectMapper;

    @Autowired
    private IBookSubjectService bookSubjectService;

    @Autowired
    private IBookPeriodService bookPeriodService;

    @Autowired
    private IBookSubjectPeriodService bookSubjectPeriodService;

    @Autowired
    private IBookAuxiliaryPeriodService bookAuxiliaryPeriodService;

    @Autowired
    private IBookPeriodService periodService;

    @Autowired
    private BookPeriodMapper periodMapper;

    @Autowired
    private TAccountBookMapper accountBookMapper;

    @Autowired
    private BookVoucherMapper voucherMapper;

    @Autowired
    private BookSubjectPeriodTemporaryMapper subjectPeriodTemporaryMapper;
    /**
     * 查询列表
     * @param bookCarryover
     * @return
     */
    public List<BookCarryover> getList(BookCarryover bookCarryover){
        Date yearPeriod = periodService.yearPeriod(bookCarryover.getBookId());
        int month = DateUtils.getMonth(yearPeriod);
        List<BookCarryover> list = carryoverMapper.getList(bookCarryover.getBookId(),month);

        return list;
    }


    /**
     * 新增结转
     * @param bookCarryover
     */
    public void add(BookCarryover bookCarryover){
        bookCarryover.setCreateTime(new Date())
                .setUpdateTime(new Date())
                .setMoney("?")
                .setIsModel(Boolean.FALSE);
        carryoverMapper.insert(bookCarryover);
        List<BookCarryoverRule> rules = bookCarryover.getRules();
        rules.forEach(carryoverRule -> {
            carryoverRule.setBookId(bookCarryover.getBookId())
                    .setCarryoverId(bookCarryover.getCarryoverId())
                    .setCode(CarryoverCodeEnum.other)
                    .setDirectionText(BalanceDirectionEnum.ralanceDirection(carryoverRule.getDirection()))
                    .setCreateTime(new Date())
                    .setUpdateTime(new Date())
                    .setIsModel(Boolean.FALSE);
            carryoverRuleMapper.insert(carryoverRule);
        });

    }

    /**
     * 修改规则
     * @param bookCarryover
     */
    public void edit(BookCarryover bookCarryover){
        bookCarryover.setUpdateTime(new Date());
        carryoverMapper.updateById(bookCarryover);
        //删除以前的规则，录入新的规则
        carryoverRuleMapper.deleteByCarryoverId(bookCarryover.getCarryoverId());
        List<BookCarryoverRule> rules = bookCarryover.getRules();
        rules.forEach(carryoverRule -> {
            carryoverRule.setBookId(bookCarryover.getBookId())
                    .setCarryoverId(bookCarryover.getCarryoverId())
                    .setCode(CarryoverCodeEnum.other)
                    .setDirectionText(BalanceDirectionEnum.ralanceDirection(carryoverRule.getDirection()))
                    .setCreateTime(new Date())
                    .setUpdateTime(new Date())
                    .setIsModel(Boolean.FALSE);
            carryoverRuleMapper.insert(carryoverRule);
        });
    }

    /**
     * 测算-多选
     * @param carryoverIds
     * @return
     */
    public Map<?,?> calculation(Long[] carryoverIds, Long bookId){
        Date yearPeriod = periodService.yearPeriod(bookId);
        int month = DateUtils.getMonth(yearPeriod);
        Map<Object,Object> result = new HashMap<>();
        result.put("success",Boolean.TRUE);
        result.put("msg","操作成功");
        List<BookCarryover> list = carryoverMapper.getList(bookId,month);
        for (BookCarryover carryover : list) {
            if( Arrays.binarySearch(carryoverIds, carryover.getCarryoverId()) >= 0){
                carryoverFactory.resetCarryover(carryover,yearPeriod);
            }
            if(!carryover.getIsDBBalanceEquals()){
                result.put("success",Boolean.FALSE);
                result.put("msg","金额带有？符号的结转借贷方金额不平衡，请核对。");
            }
        }
        result.put("data",list);
        return result;
    }

    /**
     * 测算所有
     * @param bookId
     * @return
     */
    public Map<?,?> calculationAll(Long bookId){
        Date yearPeriod = periodService.yearPeriod(bookId);
        int month = DateUtils.getMonth(yearPeriod);
        Map<Object,Object> result = new HashMap<>();
        result.put("success",Boolean.TRUE);
        result.put("msg","操作成功");
        List<BookCarryover> list = carryoverMapper.getList(bookId,month);
        for (BookCarryover carryover : list) {
            carryoverFactory.resetCarryover(carryover,yearPeriod);
            if(!carryover.getIsDBBalanceEquals()){
                result.put("success",Boolean.FALSE);
                result.put("msg","金额带有？符号的结转借贷方金额不平衡，请核对。");
            }
        }
        result.put("data",list);
        result.put("total",list.size());
        return result;
    }

    /**
     * 结转生成凭证
     * @param carryoverId
     * @return
     */
    public Map<?,?> generateVoucher(Long carryoverId){
        BookCarryover carryover = carryoverMapper.selectById(carryoverId);
        Map<String,Object> result = carryoverFactory.generateVoucher(carryover);
        return result;
    }

    /**
     * 重置销售成本百分比
     * @param carryoverId
     * @param percentage
     */
    public void resetSalesCostPercentage(Long carryoverId, BigDecimal percentage){
        BookCarryover bookCarryover = carryoverMapper.selectById(carryoverId);
        Asserts.isNull(bookCarryover,"结转模块不存在");
        Asserts.isTrue(CarryoverCodeEnum.salesCost.name().equals(bookCarryover.getCode().toString().trim()),"不是结转销售成本，设置失败");
        carryoverRuleMapper.resetSalesCostPercentage(carryoverId, percentage);
    }

    /**
     * 查询销售成本百分比
     * @param carryoverId
     * @return
     */
    public BigDecimal selectSalesCostPercentage(Long carryoverId){
        List<BookCarryoverRule> carryoverRules = carryoverRuleMapper.getListByCarryoverId(carryoverId);
        if(!carryoverRules.isEmpty()){
            return carryoverRules.get(0).getPercentage();
        }
        return BigDecimal.ZERO;
    }

    public Boolean selectProfitLossOpen(Long carryoverId){
        BookCarryover carryover = carryoverMapper.selectById(carryoverId);
        return carryover.getProfitLossOpen();
    }

    /**
     * 结转损益分开结转设置
     * @param carryoverId
     * @param profitLossOpen
     */
    public void updateProfitLossOpen(Long carryoverId, Boolean profitLossOpen){
        BookCarryover bookCarryover = carryoverMapper.selectById(carryoverId);
        Asserts.isNull(bookCarryover,"结转模块不存在");
        Asserts.isTrue(CarryoverCodeEnum.carryForward.name().equals(bookCarryover.getCode().toString().trim()),"不是结转损益，设置失败");
        bookCarryover.setProfitLossOpen(profitLossOpen);
        carryoverMapper.updateById(bookCarryover);
    }


    /**
     * 结账
     * @param bookId
     */
    public void carryoverNextPeriod(Long bookId){
        TrialBalance trialBalance = bookSubjectService.getTrialBalance(bookId);
        Asserts.isTrue(trialBalance.getIsImbalance(),"结账失败，财务初始余额不平衡！");


        //1.修改当前账期状态
        BookPeriod currentPeriod = bookPeriodService.getInfo(bookId);
        Date periodDate = currentPeriod.getYearPeriod();
        currentPeriod.setIsCurrentPeriod(Boolean.FALSE);
        currentPeriod.setStatus(PeriodStatusEnum.已结账);
        periodMapper.updatePeriod(currentPeriod);

        //测算结转损益是否还有金额未结转
        BigDecimal carryForwardBalance = carryoverFactory.getCarryForwardBalance(bookId,periodDate);
        Asserts.isFalse(carryForwardBalance != null && carryForwardBalance.doubleValue() != 0,"结转损益存在未结转的金额，不能结账，请先结转金额！");
        //如果是年底，测算本年利润是否还有金额未结转 待后期完善

        //2.新增or修改新的账期
        Date nextDate = DateUtils.nextPeriod(periodDate);
        BookPeriod nextPeriod = periodMapper.selectByPeriod(bookId,nextDate);
        if (StringUtils.isNotNull(nextPeriod)){
            nextPeriod.setIsCurrentPeriod(Boolean.TRUE);
            periodMapper.updatePeriod(nextPeriod);
            //3.查询是否时最后一期，是：科目余额加上这一期的科目发生额。否：直接去科目账期表种更新
            Date nextNextDate = DateUtils.nextPeriod(nextDate);
            BookPeriod nextNextPeriod = periodMapper.selectByPeriod(bookId,nextNextDate);
            if (StringUtils.isNotNull(nextNextPeriod)){
                //不是最后一期
                bookSubjectMapper.updateSubjectsBalance(bookId,nextDate);
            }else{
                //YY:最后一期 方案一：手动加上发生额。方案二：反结账新增最新账期余额临时表
                //YY:注---辅助核算不用这么处理。（因为辅助核算不存在实时余额）
                bookSubjectMapper.updateSubjectsBalanceByTemporary(bookId,nextDate);
                //更新完删除
                subjectPeriodTemporaryMapper.deletes(bookId,nextDate);
            }
        }else {
            nextPeriod = new BookPeriod(bookId,nextDate,Boolean.TRUE, StringDateUtil.Del_FLAG_0);
            periodMapper.insert(nextPeriod);

            //4.科目金额结转
            bookSubjectPeriodService.insertListByPeriod(bookId,periodDate);
            //5.辅助核算金额结账
            bookAuxiliaryPeriodService.insertList(bookId,periodDate);
        }

        //3.修改账套的当前账期
        TAccountBook accountBook = new TAccountBook();
        accountBook.setBookId(bookId);
        accountBook.setCurrentPeriod(nextDate);
        accountBookMapper.updateById(accountBook);
    }


    /**
     * 反结账
     * @param bookId
     */
    public void backCarryoverPeriod(Long bookId){
        //1.修改当前账套状态
        BookPeriod currentPeriod = bookPeriodService.getInfo(bookId);
        Date periodDate = currentPeriod.getYearPeriod();
        currentPeriod.setIsCurrentPeriod(Boolean.FALSE);
        periodMapper.updatePeriod(currentPeriod);

        //2.修改上一个账套状态
        Date previousDate = DateUtils.getPreviousMonth(currentPeriod.getYearPeriod());
        BookPeriod previousPeriod = periodMapper.selectByPeriod(bookId,previousDate);
        Asserts.isNull(previousPeriod,"当前账套不存在上期，不能反结账");
        previousPeriod.setIsCurrentPeriod(Boolean.TRUE);
        previousPeriod.setStatus(PeriodStatusEnum.反结账);
        periodMapper.updatePeriod(previousPeriod);

        //3.修改账套的当前账期
        TAccountBook accountBook = new TAccountBook();
        accountBook.setBookId(bookId);
        accountBook.setCurrentPeriod(previousDate);
        accountBookMapper.updateById(accountBook);

        Date nextDate = DateUtils.nextPeriod(periodDate);
        BookPeriod nextPeriod = periodMapper.selectByPeriod(bookId,nextDate);
        //最新账期反结账。账期临时表中存储当前数据
        if(StringUtils.isNull(nextPeriod)){
            subjectPeriodTemporaryMapper.insertList(bookId,periodDate);
        }
        //4.修改科目余额
        bookSubjectMapper.updateSubjectsBalance(bookId,previousDate);
    }

    /**
     * 查询当前账期录入的凭证数量
     * @param bookId
     * @return
     */
    public Integer voucherNum(Long bookId){
        Date currentPeriod = bookPeriodService.yearPeriod(bookId);
        return voucherMapper.getTotalByPeriod(bookId,currentPeriod);
    }

}
