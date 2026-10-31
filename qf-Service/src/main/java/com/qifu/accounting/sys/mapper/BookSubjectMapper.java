package com.qifu.accounting.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.qifu.accounting.sys.domain.BookSubject;
import com.qifu.accounting.sys.domain.vo.BookSubjectVo;
import com.qifu.accounting.sys.domain.vo.TrialBalance;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * 科目Mapper接口
 *
 *  YM
 * 07-03
 */
@Repository
public interface BookSubjectMapper extends BaseMapper<BookSubject> {

    /**
     * 初始化科目
     */
    void insertInitSubject(@Param("bookId")Long bookId,@Param("templateId")Long templateId);

    List<BookSubject> getlist(BookSubject bookSubject);

    /**
     * 根据bookId查询templateId
     */
    Long templateIdByBookId(@Param("bookId")Long bookId);

    /**
     * 查询所有科目
     */
    List<BookSubjectVo> searchLeaf(@Param("bookId")Long bookId);

    List<BookSubjectVo> searchOriginSubject(Long bookId);

    //查询本年利润科目
    BookSubject getYearProfitSubject(Long bookId);

    //结转更新科目余额
    void updateSubjectsBalance(@Param("bookId")Long bookId,@Param("yearPeriod") Date previousDate);

    //测算平衡
    TrialBalance getTrialBalance(Long bookId);

    //结转更新科目余额(临时表)
    void updateSubjectsBalanceByTemporary(@Param("bookId")Long bookId, @Param("yearPeriod")Date nextDate);
}
