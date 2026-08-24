package com.qifu.accounting.sys.mapper;

import com.qifu.accounting.accountBook.domain.vo.AuxiliaryAccParamsVo;
import com.qifu.accounting.sys.domain.BookSubjectAuxiliary;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 科目辅组核算关联Mapper接口
 *
 *  YM
 * 08-31
 */
@Repository
public interface BookSubjectAuxiliaryMapper extends BaseMapper<BookSubjectAuxiliary> {

    List<BookSubjectAuxiliary> getList(BookSubjectAuxiliary bookSubjectAuxiliary);

    void updateBySubjectId(Long subjectId);

    void updateOneSubjectId(@Param("subjectId")Long subjectId,@Param("fatherSubjectId") Long fatherSubjectId);

    BookSubjectAuxiliary selectByTotalBalance(AuxiliaryAccParamsVo params);
}
