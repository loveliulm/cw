package com.qifu.accounting.fixedAsset.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qifu.accounting.fixedAsset.domain.BookFixedAssetCategory;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 固定资产类别Mapper接口
 *
 *  YM
 * 07-27
 */
@Repository
public interface BookFixedAssetCategoryMapper extends BaseMapper<BookFixedAssetCategory> {
    void initInsertFixedAssertCategory(@Param("bookId")Long bookId, @Param("templateId")Long templateId);

    List<BookFixedAssetCategory> getList(BookFixedAssetCategory bookFixedAssetCategory);

    void updateAssetFatherSubjectCode(@Param("subjectCode")String subjectCode,@Param("fatherSubjectCode")String fatherSubjectCode,@Param("bookId") Long bookId);
    void updateDepreciationFatherSubjectCode(@Param("subjectCode")String subjectCode,@Param("fatherSubjectCode")String fatherSubjectCode,@Param("bookId") Long bookId);
}
