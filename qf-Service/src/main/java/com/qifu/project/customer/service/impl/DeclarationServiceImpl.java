package com.qifu.project.customer.service.impl;

import com.qifu.project.customer.domain.vo.ReportData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qifu.project.customer.mapper.DeclarationMapper;
import com.qifu.project.customer.domain.Declaration;
import com.qifu.project.customer.service.IDeclarationService;

/**
 * 报税Service业务层处理
 *
 *  YM
 * 09-08
 */
@Service
public class DeclarationServiceImpl extends ServiceImpl<DeclarationMapper, Declaration> implements IDeclarationService {

    @Autowired
    private DeclarationMapper declarationMapper;

    @Override
    public ReportData getTaxByYear(Declaration declaration) {
        return declarationMapper.getTaxByYear(declaration);
    }
}
