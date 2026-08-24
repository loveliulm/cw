package com.qifu.accounting.report.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qifu.accounting.report.domain.BookReportCashModel;

/**
 * 现金流量模板Mapper接口
 *
 *  YM
 * 09-11
 */
@Repository
public interface BookReportCashModelMapper extends BaseMapper<BookReportCashModel> {

    List<BookReportCashModel> getListByTemplate(Long templateId);
}
