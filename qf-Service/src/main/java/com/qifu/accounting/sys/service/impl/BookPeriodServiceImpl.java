package com.qifu.accounting.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qifu.accounting.accountBook.domain.AuxiliaryDetailAccount;
import com.qifu.accounting.common.enums.PeriodStatusEnum;
import com.qifu.accounting.sys.domain.BookSubject;
import com.qifu.accounting.sys.domain.vo.SeasonVo;
import com.qifu.common.utils.Asserts;
import com.qifu.common.utils.DateUtils;
import com.qifu.common.utils.StringDateUtil;
import com.qifu.common.utils.StringUtils;
import com.qifu.project.customer.domain.TAccountBook;
import com.qifu.project.customer.mapper.TAccountBookMapper;
import com.sun.org.apache.xpath.internal.operations.Bool;
import net.bytebuddy.asm.Advice;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qifu.accounting.sys.mapper.BookPeriodMapper;
import com.qifu.accounting.sys.domain.BookPeriod;
import com.qifu.accounting.sys.service.IBookPeriodService;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 07-17
 * 账期Service业务层处理
 *
 *  YM
 */
@Service
public class BookPeriodServiceImpl extends ServiceImpl<BookPeriodMapper, BookPeriod> implements IBookPeriodService {
    @Autowired
    private BookPeriodMapper periodMapper;

    @Autowired
    private TAccountBookMapper accountBookMapper;


    public List<BookPeriod> getList(BookPeriod bookPeriod){
        return periodMapper.getList(bookPeriod);
    }

    public List<SeasonVo> quarterList(BookPeriod bookPeriod){
        List<BookPeriod> list = getList(bookPeriod);
        Set<String> set = new HashSet<>();
        String currentPeriod = null;
        for (BookPeriod period : list) {
            int season = DateUtils.getSeason(period.getMonth());
            String quar = period.getYear() + "-" + season;
            set.add(quar);
            if(period.getIsCurrentPeriod()){
                currentPeriod = quar;
            }
        }
        List<SeasonVo> quarters = new ArrayList<>();
        for (String str : set) {
            SeasonVo seasonVo = new SeasonVo();
            seasonVo.setSeason(str);
            if(str.equals(currentPeriod)){
                seasonVo.setIsCurrentPeriod(Boolean.TRUE);
            }else {
                seasonVo.setIsCurrentPeriod(Boolean.FALSE);
            }
            quarters.add(seasonVo);
        }
        return quarters;
    }

    /**
     * 获取当前账期
     * @param bookId
     * @return
     */
    public BookPeriod getInfo(Long bookId){
        LambdaQueryWrapper<BookPeriod> lqw = new LambdaQueryWrapper<BookPeriod>();
        lqw.eq(BookPeriod::getBookId,bookId)
                .eq(BookPeriod::getIsCurrentPeriod,Boolean.TRUE)
                .last("limit 1");
        BookPeriod bookPeriod = periodMapper.selectOne(lqw);
        return bookPeriod;
    }

    //获取当前账期
    public Date yearPeriod(Long bookId){
        LambdaQueryWrapper<BookPeriod> lqw = new LambdaQueryWrapper<BookPeriod>();
        lqw.eq(BookPeriod::getBookId,bookId)
                .eq(BookPeriod::getIsCurrentPeriod,Boolean.TRUE)
                .last("limit 1");
        BookPeriod bookPeriod = periodMapper.selectOne(lqw);
        return bookPeriod.getYearPeriod();
    }


    //获取最大账期
    public Date maxPeriod(Long bookId){
        LambdaQueryWrapper<BookPeriod> lqw = new LambdaQueryWrapper<BookPeriod>();
        lqw.eq(BookPeriod::getBookId,bookId)
                .orderByDesc(BookPeriod::getYearPeriod)
                .last("limit 1");
        BookPeriod bookPeriod = periodMapper.selectOne(lqw);
        return bookPeriod.getYearPeriod();
    }

    /**
     * 查询账套是否已经结过账 false:未结过，true:已结过
     * 结过了不允许修改金额
     * @param bookId
     * @return
     */
    public Boolean isCarryover(Long bookId){

        Date currentPeriod = yearPeriod(bookId);
        Date previousDate = DateUtils.getPreviousMonth(currentPeriod);
        //查询前一个月账期，1.如果存在，结过账。否则未结过
        BookPeriod previousPeriod = periodMapper.selectByPeriod(bookId,previousDate);
        if(previousPeriod != null){
            return true;
        }
        return false;
    }




}
