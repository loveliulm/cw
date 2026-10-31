package com.qifu.accounting.fixedAsset.mapper;

import com.qifu.accounting.fixedAsset.domain.BookFixedAssetCard;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qifu.accounting.fixedAsset.domain.BookFixedAssetCategory;
import com.qifu.accounting.fixedAsset.domain.vo.BookFixedAssetCardVo;
import com.qifu.accounting.fixedAsset.domain.vo.DepreciationSubjectVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 固定资产卡片Mapper接口
 *
 *  YM
 * 07-28
 */
@Repository
public interface BookFixedAssetCardMapper extends BaseMapper<BookFixedAssetCard> {

    void updateFixedAssetFatherSubjectId(@Param("subjectId") Long subjectId,@Param("fatherSubjectId") Long fatherSubjectId,@Param("bookId") Long bookId);

    void updateDepreciationFatherSubjectId(@Param("subjectId") Long subjectId,@Param("fatherSubjectId") Long fatherSubjectId,@Param("bookId") Long bookId);

    void updateDepreciationFeeFatherSubjectId(@Param("subjectId") Long subjectId,@Param("fatherSubjectId") Long fatherSubjectId,@Param("bookId") Long bookId);

    List<BookFixedAssetCardVo> getList(BookFixedAssetCard bookFixedAssetCard);

    BigDecimal getBalanceDepreciation(@Param("bookId") Long bookId, @Param("yearPeriod") Date yearPeriod);

    List<DepreciationSubjectVo> selectDepreciationSubject(@Param("bookId") Long bookId, @Param("yearPeriod") Date yearPeriod);
}
