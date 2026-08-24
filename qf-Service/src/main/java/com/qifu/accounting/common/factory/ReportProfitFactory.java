package com.qifu.accounting.common.factory;

import com.qifu.accounting.accountBook.domain.BookBalance;
import com.qifu.accounting.common.enums.PeriodStatusEnum;
import com.qifu.accounting.report.domain.BookReportAssets;
import com.qifu.accounting.report.domain.BookReportProfit;
import com.qifu.accounting.report.domain.BookReportProfitRule;
import com.qifu.accounting.report.domain.vo.FirstPageParams;
import com.qifu.accounting.report.domain.vo.ReportAssetsParams;
import com.qifu.accounting.report.domain.vo.ReportProfitParams;
import com.qifu.accounting.report.mapper.BookReportProfitMapper;
import com.qifu.accounting.report.mapper.BookReportProfitRuleMapper;
import com.qifu.accounting.sys.domain.BookPeriod;
import com.qifu.accounting.sys.domain.BookSubject;
import com.qifu.accounting.sys.mapper.BookPeriodMapper;
import com.qifu.accounting.sys.mapper.BookSubjectMapper;
import com.qifu.accounting.sys.service.IBookSubjectService;
import com.qifu.common.utils.DateUtils;
import com.qifu.common.utils.StringUtils;
import org.springframework.stereotype.Component;
import sun.misc.Contended;

import javax.annotation.Resource;
import javax.xml.crypto.Data;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 *  yangyang
 * @date 2020/9/15 17:01
 */
@Component
public class ReportProfitFactory {
    @Resource
    private BookReportProfitMapper reportProfitMapper;

    @Resource
    private BookReportProfitRuleMapper reportProfitRuleMapper;

    @Resource
    private BookPeriodMapper periodMapper;



    public void resetList(ReportProfitParams params, List<BookReportProfit> list) {
        for (BookReportProfit reportProfit : list) {
            BigDecimal yearTotal = BigDecimal.ZERO;
            BigDecimal currentPeriodHappen = BigDecimal.ZERO;
            BigDecimal previousPeriodBalance = BigDecimal.ZERO;
            if(reportProfit.getIsRule()){
                List<BookReportProfitRule> rules = reportProfitRuleMapper.getList(reportProfit.getId());
                for (BookReportProfitRule rule : rules) {
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
            if(reportProfit.getProfitTitle() != null && reportProfit.getProfitTitle().contains("行次：")){
                String profitTitle = reportProfit.getProfitTitle();
                String title = profitTitle.substring(profitTitle.indexOf("：")+1);
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
                            for (BookReportProfit reportProfit2 : list) {
                                if(reportProfit2.getRowNum() != null && reportProfit2.getRowNum() == row){
                                    yearTotalOne = reportProfit2.getYearTotal();
                                    currentPeriodHappenOne = reportProfit2.getCurrentPeriodHappen();
                                    previousPeriodBalanceOne = reportProfit2.getPreviousPeriodBalance();
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
            reportProfit.setYearTotal(yearTotal);
            reportProfit.setCurrentPeriodHappen(currentPeriodHappen);
            reportProfit.setPreviousPeriodBalance(previousPeriodBalance);
        }
    }


    /**
     * 查询本年累计发生额借方。
     * @return
     */
    public BigDecimal getYearHappenTotal(ReportProfitParams params){
        //本年发生额。
        BigDecimal yearTotal = reportProfitMapper.getYearHappenBalance(params);
        //判断第一期年份与本期年份是否相同,相同则加上科目的本年累计借方，本年累计贷方
        BookPeriod bookPeriodFirst = periodMapper.getPeriodFirst(params.getBookId());
        if(bookPeriodFirst.getYear() == DateUtils.getYear(params.getStartPeriod())){
            //加上年初发生额
            yearTotal = yearTotal.add(reportProfitMapper.getSubjectInitHappen(params));
        }
        return yearTotal;
    }

    /**
     * 查询本期发生额（不包含结转损益）
     * @return
     */
    public BigDecimal getCurrentPeriodHappen(ReportProfitParams params){
        //本期发生额。
        BigDecimal currentPeriodHappen = reportProfitMapper.getHappenBalance(params);
        return currentPeriodHappen;
    }

    /**
     * 上期金额（上期的余额-企业会计准则）
     * @return
     */
    public BigDecimal getPreviousPeriodBalance(ReportProfitParams params){
        //本月期初余额=上期的余额
        Date current = params.getCurrentPeriod();
        params.setCurrentPeriod(DateUtils.getPreviousMonth(current));
        BigDecimal previousPeriodBalance = reportProfitMapper.endBalance(params);
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
    public BigDecimal profitTotalEndBalance(FirstPageParams params) {
        ReportProfitParams paramsVo = new ReportProfitParams();
        paramsVo.setBookId(params.getBookId());
        paramsVo.setCurrentPeriod(params.getYearPeriod());
        paramsVo.setStartPeriod(params.getYearPeriod());
        paramsVo.setEndPeriod(params.getYearPeriod());
        BookReportProfit reportProfit = reportProfitMapper.getProfitByTotal(params.getBookId());
        BigDecimal endBalance = profitEndBalance(reportProfit.getProfitTitle(),paramsVo);
        return endBalance;
    }


    private BigDecimal profitEndBalance(String profitTitle,ReportProfitParams params){
        BigDecimal currentPeriodHappen = BigDecimal.ZERO;
        String title = profitTitle.substring(profitTitle.indexOf("：")+1);
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
                    BookReportProfit reportProfit = reportProfitMapper.getProfit(params.getBookId(),row);
                    if(reportProfit.getProfitTitle() != null && reportProfit.getProfitTitle().contains("行次：")){
                        currentPeriodHappenOne = profitEndBalance(reportProfit.getProfitTitle(),params);
                    }else {
                        List<BookReportProfitRule> rules = reportProfitRuleMapper.getList(reportProfit.getId());
                        for (BookReportProfitRule rule : rules) {
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
