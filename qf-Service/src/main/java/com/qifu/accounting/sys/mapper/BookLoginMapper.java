package com.qifu.accounting.sys.mapper;

import com.qifu.accounting.sys.domain.vo.AccountBookVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *  yangyang
 * @date 2020/9/25 14:51
 */
@Repository
public interface BookLoginMapper {
    List<AccountBookVo> getAccountList(@Param("companyId") long companyId, @Param("bookId")Long bookId);

    AccountBookVo getInfo(Long bookId);
}
