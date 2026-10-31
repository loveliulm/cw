package com.qifu.accounting.report.mapper;

import com.qifu.accounting.report.domain.BookReportAssetsRule;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qifu.accounting.report.domain.vo.ReportAssetVo;
import com.qifu.accounting.report.domain.vo.ReportAssetsParams;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 资产负债规则Mapper接口
 *
 *  YM
 * 09-03
 */
@Repository
public interface BookReportAssetsRuleMapper extends BaseMapper<BookReportAssetsRule> {

    void initInsetReportAssetsRule(@Param("bookId") Long bookId,@Param("templateId")Long templateId);

    List<BookReportAssetsRule> getList(ReportAssetsParams params);

    void updateByRowNum(ReportAssetVo reportAssetVo);
}
