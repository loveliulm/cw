package com.qifu.accounting.report.mapper;

import com.qifu.accounting.report.domain.BookReportProfitModel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 利润模板Mapper接口
 *
 *  YM
 * 09-11
 */
@Repository
public interface BookReportProfitModelMapper extends BaseMapper<BookReportProfitModel> {

    List<BookReportProfitModel> getListByTemplate(Long templateId);
}
