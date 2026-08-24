package com.qifu.project.customer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qifu.common.utils.SecurityUtils;
import com.qifu.common.utils.StringDateUtil;
import com.qifu.project.commerce.domain.vo.StatisticalData;
import com.qifu.project.common.enums.ServerStatusEnum;
import com.qifu.project.customer.domain.vo.AccountBookVoTwo;
import com.qifu.project.customer.domain.vo.BookAccountTemplateVo;
import com.qifu.project.customer.domain.vo.TAccountBookVo;
import com.qifu.project.system.domain.SysDept;
import com.qifu.project.system.service.ISysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qifu.project.customer.mapper.TAccountBookMapper;
import com.qifu.project.customer.domain.TAccountBook;
import com.qifu.project.customer.service.ITAccountBookService;

import java.util.List;
import java.util.Map;

/**
 * 账套Service业务层处理
 *
 *  YM
 * 06-30
 */
@Service
public class TAccountBookServiceImpl extends ServiceImpl<TAccountBookMapper, TAccountBook> implements ITAccountBookService {
    @Autowired
    private TAccountBookMapper bookMapper;
    @Autowired
    private ISysDeptService sysDeptService;

    public List<TAccountBookVo> selectListBook(TAccountBookVo bookVo){
        return bookMapper.selectListBook(bookVo);
    }

    public List<BookAccountTemplateVo> getAccountTemplate(){
        return bookMapper.getAccountTemplate();
    }

    @Override
    public StatisticalData getIndexData(Map<String, Object> map) {
        return bookMapper.getIndexData(map);
    }

    @Override
    public boolean checkBookNumber(Long deptId) {
        LambdaQueryWrapper<TAccountBook> lambdaQueryWrapper = new LambdaQueryWrapper<TAccountBook>();
        lambdaQueryWrapper.eq(TAccountBook::getDeptId,deptId);
        lambdaQueryWrapper.eq(TAccountBook::getIsDel, StringDateUtil.Del_FLAG_0);
        lambdaQueryWrapper.eq(TAccountBook::getStatus,StringDateUtil.STATUS_0);
        lambdaQueryWrapper.gt(TAccountBook::getBookCount,0);

        List<TAccountBook> list = bookMapper.selectList(lambdaQueryWrapper);
        long companyId = SecurityUtils.companyId();
        SysDept dept = sysDeptService.selectDeptById(companyId);
        if (dept.getAccountBookNum() > list.size()) {
            return true;
        }
        return false;
    }

    @Override
    public AccountBookVoTwo selectAccountBookStatusByDeptId(Long deptId) {
        return bookMapper.selectAccountBookStatusByDeptId(deptId);
    }

    @Override
    public boolean checkServiceStatus(Long id) {
        TAccountBook accountBook = bookMapper.selectById(id);
        if (accountBook.getServerStatus() == ServerStatusEnum.停止服务){
            return false;
        }
        return true;
    }
}
