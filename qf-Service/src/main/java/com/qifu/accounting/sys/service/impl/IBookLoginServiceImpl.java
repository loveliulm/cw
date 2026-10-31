package com.qifu.accounting.sys.service.impl;

import com.qifu.accounting.sys.domain.vo.AccountBookVo;
import com.qifu.accounting.sys.mapper.BookLoginMapper;
import com.qifu.accounting.sys.service.IBookLoginService;
import com.qifu.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *  yangyang
 * @date 2020/9/25 14:45
 */
@Service
public class IBookLoginServiceImpl implements IBookLoginService {
    @Autowired
    private BookLoginMapper bookLoginMapper;

    /**
     * 代账系统登录-获取所有已开账套
     * @param bookId
     * @return
     */
    public List<AccountBookVo> getAccountList(Long bookId){
        long companyId = SecurityUtils.companyId();
        return bookLoginMapper.getAccountList(companyId,bookId);
    }

    /**
     * 代账系统登录-获取当前账套信息
     * @param bookId
     * @return
     */
    public AccountBookVo getInfo(Long bookId) {
        return bookLoginMapper.getInfo(bookId);
    }
}
