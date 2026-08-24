package com.qifu.accounting.common.factory;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.qifu.accounting.report.domain.BookReportCash;
import com.qifu.accounting.report.domain.BookReportCashRule;
import com.qifu.accounting.report.domain.vo.FirstPageParams;
import com.qifu.accounting.report.domain.vo.ReportCashParams;
import com.qifu.accounting.report.mapper.BookReportCashMapper;
import com.qifu.accounting.report.mapper.BookReportCashRuleMapper;
import com.qifu.accounting.sys.domain.BookPeriod;
import com.qifu.accounting.sys.mapper.BookPeriodMapper;
import com.qifu.common.utils.DateUtils;
import com.qifu.common.utils.StringUtils;

/**
 *  yangyang
 * @date 2020/9/15 17:01
 */
@Component
public class ReportCashFactory {
    @Resource
    private BookReportCashMapper reportCashMapper;

    @Resource
    private BookReportCashRuleMapper reportCashRuleMapper;

    @Resource
    private BookPeriodMapper periodMapper;



    public void resetList(ReportCashParams params, List<BookReportCash> list) {
        for (BookReportCash reportCash : list) {
            BigDecimal yearTotal = BigDecimal.ZERO;
            BigDecimal currentPeriodHappen = BigDecimal.ZERO;
            BigDecimal previousPeriodBalance = BigDecimal.ZERO;
            if(reportCash.getIsRule()){
                List<BookReportCashRule> rules = reportCashRuleMapper.getList(reportCash.getId());
                for (BookReportCashRule rule : rules) {
                    params.setSubjectCode(rule.getSubjectCode());
                    BigDecimal yearTotalOne = getYearHappenTotal(params);
                    BigDecimal currentPeriodHappenOne = getCurrentPeriodHappen(params);
                    BigDecimal previousPeriodBalanceOne = getPreviousPeriodBalance(params);
                    if(rule.getSign() == '+'){
                        yearTotal = yearTotal.add(yearTotalOne);
                        currentPeriodHappen = currentPeriodHappen.add(currentPeriodHappenOne);
                        previousPeriodBalance = previousPeriodBalance.add(previousPeriodBalanceOne);
                    }
                    if(rule.getSign() == '-'){
                        yearTotal = yearTotal.subtract(yearTotalOne);
                        currentPeriodHappen = currentPeriodHappen.subtract(currentPeriodHappenOne);
                        previousPeriodBalance = previousPeriodBalance.subtract(previousPeriodBalanceOne);
                    }
                }
            }
            //有行次
            if(reportCash.getCashTitle() != null && reportCash.getCashTitle().contains("行次：")){
                String CashTitle = reportCash.getCashTitle();
                String title = CashTitle.substring(CashTitle.indexOf("：")+1);
                char dateRule = '+';
                int row;
                if(title != null){
                    String[] array = title.split("行");
                    for (int i = 0; i < array.length; i++) {
                        String s = array[i];
                        if(StringUtils.isNotEmpty(s)){
                            if(i != array.length-1){
                                row = new Integer(s.substring(0,s.length()-1));
                            }else {
                                row = new Integer(s);
                            }
                            BigDecimal yearTotalOne = getYearHappenTotal(params);
                            BigDecimal currentPeriodHappenOne = getCurrentPeriodHappen(params);
                            BigDecimal previousPeriodBalanceOne = getPreviousPeriodBalance(params);
                            for (BookReportCash reportCash2 : list) {
                                if(reportCash2.getRowNum() != null && reportCash2.getRowNum() == row){
                                    yearTotalOne = reportCash2.getYearTotal();
                                    currentPeriodHappenOne = reportCash2.getCurrentPeriodHappen();
                                    previousPeriodBalanceOne = reportCash2.getPreviousPeriodBalance();
                                }
                            }
                            if(dateRule == '+'){
                                yearTotal = yearTotal.add(yearTotalOne);
                                currentPeriodHappen = currentPeriodHappen.add(currentPeriodHappenOne);
                                previousPeriodBalance = previousPeriodBalance.add(previousPeriodBalanceOne);
                            }
                            if(dateRule == '-'){
                                yearTotal = yearTotal.subtract(yearTotalOne);
                                currentPeriodHappen = currentPeriodHappen.subtract(currentPeriodHappenOne);
                                previousPeriodBalance = previousPeriodBalance.subtract(previousPeriodBalanceOne);
                            }
                            if(i != array.length-1){
                                dateRule = s.charAt(s.length()-1);
                            }

                        }
                    }
                }
            }
            reportCash.setYearTotal(yearTotal);
            reportCash.setCurrentPeriodHappen(currentPeriodHappen);
            reportCash.setPreviousPeriodBalance(previousPeriodBalance);
        }
    }


    /**
     * 查询本年累计发生额借方。
     * @return
     */
    public BigDecimal getYearHappenTotal(ReportCashParams params){
        //本年发生额。
        BigDecimal yearTotal = reportCashMapper.getYearHappenBalance(params);
        //判断第一期年份与本期年份是否相同,相同则加上科目的本年累计借方，本年累计贷方
        BookPeriod bookPeriodFirst = periodMapper.getPeriodFirst(params.getBookId());
        if(!Objects.isNull(params.getStartPeriod()) && bookPeriodFirst.getYear() == DateUtils.getYear(params.getStartPeriod())){
            //加上年初发生额
        	BigDecimal ncfse =  reportCashMapper.getSubjectInitHappen(params);
            yearTotal = yearTotal.add(Objects.isNull(ncfse)?new BigDecimal(0):ncfse);
        }
        return yearTotal;
    }

    /**
     * 查询本期发生额（不包含结转损益）
     * @return
     */
    public BigDecimal getCurrentPeriodHappen(ReportCashParams params){
        //本期发生额。
        BigDecimal currentPeriodHappen = reportCashMapper.getHappenBalance(params);
        return currentPeriodHappen;
    }

    /**
     * 上期金额（上期的余额-企业会计准则）
     * @return
     */
    public BigDecimal getPreviousPeriodBalance(ReportCashParams params){
        //本月期初余额=上期的余额
        Date current = params.getCurrentPeriod();
        params.setCurrentPeriod(DateUtils.getPreviousMonth(current));
        BigDecimal previousPeriodBalance = reportCashMapper.endBalance(params);
        params.setCurrentPeriod(current);
        if(previousPeriodBalance == null){
            return BigDecimal.ZERO;
        }
        return previousPeriodBalance;
    }


    /**
     * 首页报表-测算净利润。
     * @param params
     * @return
     */
    public BigDecimal CashTotalEndBalance(FirstPageParams params) {
        ReportCashParams paramsVo = new ReportCashParams();
        paramsVo.setBookId(params.getBookId());
        paramsVo.setCurrentPeriod(params.getYearPeriod());
        paramsVo.setStartPeriod(params.getYearPeriod());
        paramsVo.setEndPeriod(params.getYearPeriod());
        BookReportCash reportCash = reportCashMapper.getCashByTotal(params.getBookId());
        BigDecimal endBalance = CashEndBalance(reportCash.getCashTitle(),paramsVo);
        return endBalance;
    }


    private BigDecimal CashEndBalance(String CashTitle,ReportCashParams params){
        BigDecimal currentPeriodHappen = BigDecimal.ZERO;
        String title = CashTitle.substring(CashTitle.indexOf("：")+1);
        char dateRule = '+';
        int row;
        if(title != null){
            String[] array = title.split("行");
            for (int i = 0; i < array.length; i++) {
                String s = array[i];
                if(StringUtils.isNotEmpty(s)){
                    if(i != array.length-1){
                        row = new Integer(s.substring(0,s.length()-1));
                    }else {
                        row = new Integer(s);
                    }
                    BigDecimal currentPeriodHappenOne = BigDecimal.ZERO;
                    //根据查询该行次的规则是否包含行次。是继续执行当前方法，否则根据规则计算
                    BookReportCash reportCash = reportCashMapper.getCash(params.getBookId(),row);
                    if(reportCash.getCashTitle() != null && reportCash.getCashTitle().contains("行次：")){
                        currentPeriodHappenOne = CashEndBalance(reportCash.getCashTitle(),params);
                    }else {
                        List<BookReportCashRule> rules = reportCashRuleMapper.getList(reportCash.getId());
                        for (BookReportCashRule rule : rules) {
                            params.setSubjectCode(rule.getSubjectCode());
                            if(rule.getSign() == '+'){
                                currentPeriodHappenOne = currentPeriodHappenOne.add(getCurrentPeriodHappen(params));
                            }
                            if(rule.getSign() == '-'){
                                currentPeriodHappenOne = currentPeriodHappenOne.subtract(getCurrentPeriodHappen(params));
                            }
                        }
                    }

                    if(dateRule == '+'){
                        currentPeriodHappen = currentPeriodHappen.add(currentPeriodHappenOne);
                    }
                    if(dateRule == '-'){
                        currentPeriodHappen = currentPeriodHappen.subtract(currentPeriodHappenOne);
                    }
                    if(i != array.length-1){
                        dateRule = s.charAt(s.length()-1);
                    }

                }
            }
        }
        return currentPeriodHappen;
    }
}
