package com.qifu.accounting.sys.service.impl;

import com.qifu.accounting.common.enums.PeriodStatusEnum;
import com.qifu.accounting.sys.domain.BookPeriod;
import com.qifu.accounting.sys.domain.BookSubject;
import com.qifu.accounting.sys.domain.BookSubjectPeriodTemporary;
import com.qifu.accounting.sys.mapper.BookSubjectPeriodTemporaryMapper;
import com.qifu.accounting.sys.service.IBookPeriodService;
import com.qifu.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qifu.accounting.sys.mapper.BookSubjectPeriodMapper;
import com.qifu.accounting.sys.domain.BookSubjectPeriod;
import com.qifu.accounting.sys.service.IBookSubjectPeriodService;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 科目账期期末余额Service业务层处理
 *
 *
 * 08-17
 */
@Service
public class BookSubjectPeriodServiceImpl extends ServiceImpl<BookSubjectPeriodMapper, BookSubjectPeriod> implements IBookSubjectPeriodService {
    @Autowired
    private BookSubjectPeriodMapper subjectPeriodMapper;
    @Autowired
    private IBookPeriodService periodService;
    @Autowired
    private BookSubjectPeriodTemporaryMapper subjectPeriodTemporaryMapper;

    public void insertListByPeriod(Long bookId, Date period){
        Date previousPeriod = DateUtils.getPreviousMonth(period);
        subjectPeriodMapper.insertListByPeriod(bookId, period,previousPeriod);
    }

    /**
     * 根据科目id，相差金额，账期，更改当前账期以后账期的金额。
     * @param bookId
     * @param subjectId
     * @param diffMoney
     */
    public void updateBalanceBySubjectId(Long bookId,Long subjectId, BigDecimal diffMoney){
        Date yearPeriod = periodService.yearPeriod(bookId);
        //修改本期 --修改期末
        subjectPeriodMapper.updateBalanceByPeriod(subjectId,diffMoney,yearPeriod);
        //修改后期 --修改期初，期末
        subjectPeriodMapper.updateBalanceByAfterPeriod(subjectId,diffMoney,yearPeriod);
        //修改临时表 金额
        subjectPeriodTemporaryMapper.updateBalanceByAfterPeriod(subjectId,diffMoney,yearPeriod);
    }

    /**
     * 反结账后新增科目同步到科目账期表
     * @param bookSubject
     */
    public void insert(BookSubject bookSubject,BookSubject fatherSubject,Boolean isSon){
        BookPeriod currentPeriod = periodService.getInfo(bookSubject.getBookId());
        if(PeriodStatusEnum.反结账.name().equals(currentPeriod.getStatus().name())) {
            List<BookSubjectPeriod> periodList = subjectPeriodMapper.selectListPeriodByBookId(currentPeriod.getYearPeriod(),fatherSubject.getSubjectId());
            BookSubjectPeriod subjectPeriod = new BookSubjectPeriod();
            subjectPeriod.setCreateTime(new Date())
                    .setSubjectId(bookSubject.getSubjectId());

            //临时表
            BookSubjectPeriodTemporary subjectPeriodTemporary = new BookSubjectPeriodTemporary();
            subjectPeriodTemporary.setCreateTime(new Date());
            subjectPeriodTemporary.setUpdateTime(new Date());
            subjectPeriodTemporary.setSubjectId(bookSubject.getSubjectId());
            for (int i = 0; i < periodList.size(); i++) {
                subjectPeriod.setYearPeriod(periodList.get(i).getYearPeriod());
                //判断父级是否已经存在子级，如果存在金额初始化数据为0，如果不存在则继承父亲
                if(isSon){
                    subjectPeriod.setPeriodInitBalance(BigDecimal.ZERO);
                    subjectPeriod.setPeriodEndBalance(BigDecimal.ZERO);
                }else {
                    BookSubjectPeriod fatherSubjectPeriod = subjectPeriodMapper.selectByPeriod(fatherSubject.getSubjectId(),periodList.get(i).getYearPeriod());
                    subjectPeriod.setPeriodInitBalance(fatherSubjectPeriod.getPeriodInitBalance());
                    subjectPeriod.setPeriodEndBalance(fatherSubjectPeriod.getPeriodEndBalance());
                }
                subjectPeriodMapper.insert(subjectPeriod);
            }

            //科目账期余额临时表修改
            Date maxPeriod = periodService.maxPeriod(fatherSubject.getBookId());
            subjectPeriodTemporary.setYearPeriod(maxPeriod);
            if(isSon){
                subjectPeriodTemporary.setPeriodBalance(BigDecimal.ZERO);
            }else{
                BookSubjectPeriodTemporary father = subjectPeriodTemporaryMapper.selectByPeriod(fatherSubject.getSubjectId(),maxPeriod);
                subjectPeriodTemporary.setPeriodBalance(father.getPeriodBalance());
            }
            subjectPeriodTemporaryMapper.insert(subjectPeriodTemporary);
        }
    }

}
