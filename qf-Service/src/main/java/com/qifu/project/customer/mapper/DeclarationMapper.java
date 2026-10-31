package com.qifu.project.customer.mapper;

import com.qifu.project.customer.domain.Declaration;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qifu.project.customer.domain.vo.ReportData;

/**
 * 报税Mapper接口
 *
 *  YM
 * 09-08
 */
public interface DeclarationMapper extends BaseMapper<Declaration> {

    /**
     * 获取客户一年的每月是否报税
     * @param declaration
     * @return
     */
    ReportData getTaxByYear(Declaration declaration);

}
