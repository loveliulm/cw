package com.qifu.project.customer.service;

import com.qifu.project.customer.domain.Declaration;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qifu.project.customer.domain.vo.ReportData;

/**
 * 报税Service接口
 *
 *
 *
 */
public interface IDeclarationService extends IService<Declaration> {

    /**
     * 获取客户一年的每月是否报税
     * @param declaration
     * @return
     */
    ReportData getTaxByYear(Declaration declaration);

}
