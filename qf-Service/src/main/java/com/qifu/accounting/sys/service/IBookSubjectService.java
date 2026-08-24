package com.qifu.accounting.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;

import com.qifu.accounting.sys.domain.BookSubject;
import com.qifu.accounting.sys.domain.vo.BookSubjectVo;
import com.qifu.accounting.common.enums.BalanceDirectionEnum;
import com.qifu.accounting.common.enums.BalancePurposeEnum;
import com.qifu.accounting.sys.domain.vo.TrialBalance;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 科目Service接口
 *
 *  YM
 * 07-03
 */
public interface IBookSubjectService extends IService<BookSubject> {

    /**
     * 初始化科目
     */
    void insertInitSubject(@Param("bookId")Long bookId, @Param("templateId")Long templateId);

    List<BookSubject> getlist(BookSubject bookSubject);

    String getSubjectCode(Long subjectId);

    void edit(BookSubject bookSubject);

    void add(BookSubject bookSubject);

    Map addSubjectCheck(BookSubject bookSubject);

    void editBalance(BookSubject bookSubject);

    void delete(BookSubject subject);

    List<BookSubjectVo> searchLeaf(Long bookId);


    List<BookSubjectVo> searchOriginSubject(Long bookId);

    TrialBalance getTrialBalance(Long bookId);

    Map<String,Object> subjectBalanceIsEdit(Long bookId);
}
