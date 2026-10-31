package com.qifu.accounting.sys.service;


import com.qifu.accounting.sys.domain.vo.AccountBookVo;

import java.util.List;

/**
 *  yangyang
 * @date 2020/9/25 14:45
 */
public interface IBookLoginService {
    List<AccountBookVo> getAccountList(Long bookId);

    AccountBookVo getInfo(Long bookId);
}
