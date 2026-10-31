package com.qifu.accounting.report.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qifu.accounting.common.factory.ReportAssetsFactory;
import com.qifu.accounting.common.factory.ReportProfitFactory;
import com.qifu.accounting.report.domain.vo.FirstPageFinancial;
import com.qifu.accounting.report.domain.vo.FirstPageParams;
import com.qifu.accounting.report.domain.vo.FirstPageTax;
import com.qifu.accounting.report.mapper.FirstPageMapper;
import com.qifu.accounting.report.service.FirstPageService;
import com.qifu.accounting.sys.domain.BookConfig;
import com.qifu.accounting.sys.domain.BookPeriod;
import com.qifu.accounting.sys.mapper.BookPeriodMapper;
import com.qifu.accounting.sys.service.IBookConfigService;
import com.qifu.accounting.voucher.domain.BookVoucher;
import com.qifu.accounting.voucher.service.IBookVoucherService;
import com.qifu.common.utils.DateUtils;
import com.qifu.common.utils.StringDateUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  yangyang
 * @date 2020/9/21 10:35
 */
@Service
public class FirstPageServiceImpl implements FirstPageService {
    @Resource
    private IBookVoucherService voucherService;
    @Resource
    private ReportAssetsFactory reportAssetsFactory;
    @Resource
    private ReportProfitFactory reportProfitFactory;
    @Resource
    private FirstPageMapper firstPageMapper;
    @Resource
    private BookPeriodMapper bookPeriodMapper;
    @Resource
    private IBookConfigService iBookConfigService;

    /**
     * 查询报表数据
     * @param params
     * @return
     */
    public Map reportInfo(FirstPageParams params){
        Map<Object,Object> result = new HashMap<>();
        //资产负债表（资产总计：）
        BigDecimal assetTotalEndBalance = reportAssetsFactory.assetTotalEndBalance(params);
        //利润表（净利润：）
        BigDecimal profitTotalEndBalance = reportProfitFactory.profitTotalEndBalance(params);
        //科目余额表（借方余额总计-期末余额：）
        BigDecimal subjectDebitTotalEndBalance = firstPageMapper.subjectDebitTotalEndBalance(params);
        //凭证汇总表（借方发生额合计：）
        BigDecimal voucherDebitTotalBalance = firstPageMapper.voucherDebitTotalBalance(params);

        result.put("assetTotalEndBalance",assetTotalEndBalance);
        result.put("profitTotalEndBalance",profitTotalEndBalance);
        result.put("subjectDebitTotalEndBalance",subjectDebitTotalEndBalance);
        result.put("voucherDebitTotalBalance",voucherDebitTotalBalance);
        return result;
    }


    /**
     * 查询凭证数据
     * @param params
     * @return
     */
    public List<BookVoucher> voucherInfo(FirstPageParams params){
        LambdaQueryWrapper<BookVoucher> lqw = new LambdaQueryWrapper<BookVoucher>();
        lqw.eq(BookVoucher::getBookId,params.getBookId())
                .eq(BookVoucher::getYearPeriod,params.getYearPeriod())
                .eq(BookVoucher::getDelFlag, StringDateUtil.Del_FLAG_0)
                .orderByDesc(BookVoucher::getCreateTime)
                .last("limit 0,3");
        List<BookVoucher> list = voucherService.list(lqw);
        return list;
    }

    /**
     * 查询科目余额
     * @param params
     * @return
     */
    public List<FirstPageFinancial> subjectInfo(FirstPageParams params){
        BookConfig config = new BookConfig();
        config.setType(StringDateUtil.FINANCIAL);
        config.setBookId(params.getBookId());
        List<BookConfig> configs = iBookConfigService.selectConfigList(config);
        List<FirstPageFinancial> list = firstPageMapper.subjectInfo(params,configs);
        return list;
    }


    public List<FirstPageTax> taxInfo(FirstPageParams params){
        BookPeriod period = new BookPeriod();
        period.setBookId(params.getBookId());
        period.setYear(DateUtils.getYear(params.getYearPeriod()));
        //根据当前年份查询该年份已有账期
        List<BookPeriod> bookPeriods = bookPeriodMapper.getList(period);

        BookConfig config = new BookConfig();
        config.setType(StringDateUtil.TAX_TYPE);
        config.setBookId(params.getBookId());
        List<BookConfig> configs = iBookConfigService.selectConfigList(config);

        List<FirstPageTax> list = new ArrayList<>();
        int [] array = {1,2,3,4,5,6,7,8,9,10,11,12};
        for (int i = 0; i < array.length; i++) {
            FirstPageTax firstPageTax = new FirstPageTax();
            for (BookPeriod bookPeriod : bookPeriods) {
                if(array[i] == bookPeriod.getMonth()){
                    firstPageTax = firstPageMapper.taxInfoByPeriod(bookPeriod,configs);
                }
            }
            list.add(firstPageTax);
        }
        return list;
    }
}
