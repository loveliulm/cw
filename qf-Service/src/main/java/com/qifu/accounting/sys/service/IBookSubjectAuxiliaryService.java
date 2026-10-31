package com.qifu.accounting.sys.service;

import com.qifu.accounting.sys.domain.BookSubjectAuxiliary;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qifu.accounting.sys.domain.vo.BookSubjectAuxiliaryVo;

import java.util.List;

/**
 * 科目辅组核算关联Service接口
 *
 *  YM
 * 08-31
 */
public interface IBookSubjectAuxiliaryService extends IService<BookSubjectAuxiliary> {
    List<BookSubjectAuxiliary> getList(BookSubjectAuxiliary bookSubjectAuxiliary);

    void add(BookSubjectAuxiliaryVo subjectAuxiliaryVo) throws Exception;

    void edit(BookSubjectAuxiliaryVo subjectAuxiliaryVo) throws Exception;

    void judge(Long subjectId);

    void updateOneSubjectId(Long subjectId,Long fatherSubjectId);
}
