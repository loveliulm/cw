package com.qifu.accounting.common.factory;

import com.qifu.accounting.accountBook.mapper.BookBalanceMapper;
import com.qifu.accounting.common.enums.BalanceDirectionEnum;
import com.qifu.accounting.common.enums.PeriodStatusEnum;
import com.qifu.accounting.common.enums.ReportDateRuleEnum;
import com.qifu.accounting.report.domain.BookReportAssets;
import com.qifu.accounting.report.domain.BookReportAssetsRule;
import com.qifu.accounting.report.domain.vo.FirstPageParams;
import com.qifu.accounting.report.domain.vo.ReportAssetsParams;
import com.qifu.accounting.report.mapper.BookReportAssetsMapper;
import com.qifu.accounting.report.mapper.BookReportAssetsRuleMapper;
import com.qifu.accounting.sys.domain.BookPeriod;
import com.qifu.accounting.sys.domain.BookSubject;
import com.qifu.accounting.sys.mapper.BookPeriodMapper;
import com.qifu.accounting.sys.mapper.BookSubjectMapper;
import com.qifu.common.utils.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 *  yangyang
 * @date 2020/9/14 13:41
 */
@Component
public class ReportAssetsFactory {

    @Resource
    private BookReportAssetsRuleMapper reportAssetsRuleMapper;
    @Resource
    private BookReportAssetsMapper reportAssetsMapper;
    @Resource
    private BookPeriodMapper periodMapper;


    /**
     * 重置
     * @param paramsVo
     * @param list
     */
    public void resetReportAssetsList(ReportAssetsParams paramsVo, List<BookReportAssets> list){
        for (BookReportAssets bookReportAssets : list) {
            resetReportAssets(paramsVo,bookReportAssets,list);
        }
    }


    /**
     * 重置
     * @param paramsVo
     * @param bookReportAssets
     */
    private void resetReportAssets(ReportAssetsParams paramsVo, BookReportAssets bookReportAssets, List<BookReportAssets> list){
        StringBuffer rowStr = new StringBuffer("行次：");
        //资产有科目
        if(bookReportAssets.getIsAssetsRule()){
            paramsVo.setRowNum(bookReportAssets.getAssetsRowNum());
            resetReportAssetsBalance(paramsVo,bookReportAssets,1);
        }
        //负债有科目
        if(bookReportAssets.getIsDebtRule()){
            paramsVo.setRowNum(bookReportAssets.getDebtRowNum());
            resetReportAssetsBalance(paramsVo,bookReportAssets,-1);
        }
        //资产有行次
        if(bookReportAssets.getAssetsTitle() != null && bookReportAssets.getAssetsTitle().contains(rowStr)){
            resetTotal(bookReportAssets,list,1);
        }

        //负债有行次
        if(bookReportAssets.getDebtTitle() != null && bookReportAssets.getDebtTitle().contains(rowStr)){
            resetTotal(bookReportAssets,list,-1);
        }
    }

    /**
     * 统计合计的金额
     * @param bookReportAssets
     * @param list
     * @param status 1：资产，-1：负债
     */
    private void resetTotal(BookReportAssets bookReportAssets,List<BookReportAssets> list,int status){
        String assetsTitle = "";
        if(status == 1){
            assetsTitle = bookReportAssets.getAssetsTitle();
        }
        if(status == -1){
            assetsTitle = bookReportAssets.getDebtTitle();
        }
        String title = assetsTitle.substring(assetsTitle.indexOf("：")+1);
        BigDecimal periodBalance = BigDecimal.ZERO;
        BigDecimal yearBalance = BigDecimal.ZERO;
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
                    BigDecimal endBalance = BigDecimal.ZERO;
                    BigDecimal yearInitBalance = BigDecimal.ZERO;
                    for (BookReportAssets reportAssets : list) {
                        if(reportAssets.getAssetsRowNum() != null && reportAssets.getAssetsRowNum() == row){
                            endBalance = reportAssets.getAssetsEndBalance();
                            yearInitBalance = reportAssets.getAssetsYearInitBalance();
                        }
                        if(reportAssets.getDebtRowNum() != null && reportAssets.getDebtRowNum() == row){
                            endBalance = reportAssets.getDebtEndBalance();
                            yearInitBalance = reportAssets.getDebtYearInitBalance();
                        }
                    }
                    if(dateRule == '+'){
                        periodBalance = periodBalance.add(endBalance);
                        yearBalance = yearBalance.add(yearInitBalance);
                    }
                    if(dateRule == '-'){
                        periodBalance = periodBalance.subtract(endBalance);
                        yearBalance = yearBalance.subtract(yearInitBalance);
                    }
                    if(i != array.length-1){
                        dateRule = s.charAt(s.length()-1);
                    }

                }
            }

        }
        if(status == 1){
            bookReportAssets.setAssetsEndBalance(periodBalance);
            bookReportAssets.setAssetsYearInitBalance(yearBalance);
        }
        if(status == -1){
            bookReportAssets.setDebtEndBalance(periodBalance);
            bookReportAssets.setDebtYearInitBalance(yearBalance);
        }
    }

    /**
     * 重置：月末余额，年初余额
     * @param status 1：资产，-1：负债
     */
    public void resetReportAssetsBalance(ReportAssetsParams paramsVo, BookReportAssets bookReportAssets, int status){
        BigDecimal yearBalance = BigDecimal.ZERO;
        BigDecimal periodBalance = BigDecimal.ZERO;
        List<BookReportAssetsRule> list = reportAssetsRuleMapper.getList(paramsVo);
        for (BookReportAssetsRule bookReportAssetsRule : list) {
            //科目期初余额
            paramsVo.setSubjectCode(bookReportAssetsRule.getSubjectCode());
            BigDecimal yearInitBalance = yearInitBalance(paramsVo,bookReportAssetsRule.getDataRule());
            BigDecimal endBalance = endBalance(paramsVo,bookReportAssetsRule.getDataRule());
            if(bookReportAssetsRule.getSign() == '+'){
                yearBalance = yearBalance.add(yearInitBalance);
                periodBalance = periodBalance.add(endBalance);
            }
            if(bookReportAssetsRule.getSign() == '-'){
                yearBalance = yearBalance.subtract(yearInitBalance);
                periodBalance = periodBalance.subtract(endBalance);
            }
        }
        //资产
        if(status == 1){
            bookReportAssets.setAssetsYearInitBalance(yearBalance);
            bookReportAssets.setAssetsEndBalance(periodBalance);
        }
        //负债
        if(status == -1){
            bookReportAssets.setDebtYearInitBalance(yearBalance);
            bookReportAssets.setDebtEndBalance(periodBalance);
        }
    }

    /**
     * 查询本期月末余额
     * @param paramsVo
     * @param dataRule
     * @return
     */
    public BigDecimal endBalance(ReportAssetsParams paramsVo,ReportDateRuleEnum dataRule){
        BookPeriod Period = periodMapper.selectByPeriod(paramsVo.getBookId(),paramsVo.getCurrentPeriod());
        BookSubject subject = reportAssetsMapper.getSubject(paramsVo);
        BigDecimal endBalance;
        if(Period != null &&
                (Period.getStatus().toString().equals(PeriodStatusEnum.已结账.name())
                ||Period.getStatus().toString().equals(PeriodStatusEnum.反结账.name()))
        ){
            endBalance = reportAssetsMapper.endBalance(paramsVo);
        }else {
            endBalance = subject.getBalance();
        }
        endBalance = balance(endBalance,dataRule,subject);
        return endBalance;
    }


    /**
     * 查询年初余额
     * @param paramsVo
     * @param dataRule
     * @return
     */
    public BigDecimal yearInitBalance(ReportAssetsParams paramsVo, ReportDateRuleEnum dataRule){
        //去年十二月账期
        BookPeriod Period = periodMapper.selectByPeriod(paramsVo.getBookId(),paramsVo.getLastYearPeriod());
        BookSubject subject = reportAssetsMapper.getSubject(paramsVo);
        BigDecimal yearInitBalance;
        if(Period != null){
            yearInitBalance = reportAssetsMapper.yearInitBalance(paramsVo);
        }else {
            yearInitBalance =  subject.getYearBalance();
        }
        yearInitBalance = balance(yearInitBalance,dataRule,subject);
        return yearInitBalance;
    }


    /**
     * 根据取数规则返回金额
     * @param balance
     * @param dataRule
     * @param subject
     * @return
     */
    public BigDecimal balance(BigDecimal balance, ReportDateRuleEnum dataRule,BookSubject subject){
        BigDecimal creditBalance = BigDecimal.ZERO;
        BigDecimal debitBalance = BigDecimal.ZERO;
        if(balance.doubleValue() < 0){
            if(subject.getBalanceDirection().name().equals(BalanceDirectionEnum.DEBIT.name())){
                creditBalance = balance.multiply(new BigDecimal(-1));
            }
            if(subject.getBalanceDirection().name().equals(BalanceDirectionEnum.CREDIT.name())){
                debitBalance = balance.multiply(new BigDecimal(-1));
            }
        }
        if(balance.doubleValue() > 0){
            if(subject.getBalanceDirection().name().equals(BalanceDirectionEnum.DEBIT.name())){
                debitBalance = balance;
            }
            if(subject.getBalanceDirection().name().equals(BalanceDirectionEnum.CREDIT.name())){
                creditBalance = balance;
            }
        }
        if(dataRule.name().equals(ReportDateRuleEnum.余额.name())){
            return balance;
        }
        if(dataRule.name().equals(ReportDateRuleEnum.借方余额.name())){
            return debitBalance;
        }
        if(dataRule.name().equals(ReportDateRuleEnum.贷方余额.name())){
            return creditBalance;
        }
        return balance;
    }

    //首页报表-测算资产金额。
    public BigDecimal assetTotalEndBalance(FirstPageParams params) {
        ReportAssetsParams paramsVo = new ReportAssetsParams();
        paramsVo.setIsClassify(Boolean.TRUE);
        paramsVo.setBookId(params.getBookId());
        paramsVo.setCurrentPeriod(params.getYearPeriod());
        //查询资产合计：
        BookReportAssets bookReportAssets = reportAssetsMapper.getReportAssetsTotal(params.getBookId());
        BigDecimal balance  = assetTotalEndBalance(bookReportAssets.getAssetsTitle(),paramsVo);
        return balance;
    }

    private BigDecimal assetTotalEndBalance(String assetsTitle,ReportAssetsParams paramsVo){
        String title = assetsTitle.substring(assetsTitle.indexOf("：")+1);
        BigDecimal periodBalance = BigDecimal.ZERO;
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
                    BigDecimal endBalance;
                    paramsVo.setRowNum(row);
                    BookReportAssets bookReportAssets = reportAssetsMapper.getReportAssets(paramsVo);
                    if(bookReportAssets.getAssetsTitle() != null && bookReportAssets.getAssetsTitle().contains("行次：")){
                        endBalance = assetTotalEndBalance(bookReportAssets.getAssetsTitle(),paramsVo);
                    }else{
                        resetReportAssetsBalance(paramsVo, bookReportAssets, 1);
                        endBalance = bookReportAssets.getAssetsEndBalance();
                    }
                    if(dateRule == '+'){
                        periodBalance = periodBalance.add(endBalance);
                    }
                    if(dateRule == '-'){
                        periodBalance = periodBalance.subtract(endBalance);
                    }
                    if(i != array.length-1){
                        dateRule = s.charAt(s.length()-1);
                    }

                }
            }

        }
        return periodBalance;
    }
}
