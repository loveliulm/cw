package com.qifu.accounting.fixedAsset.service;

import com.qifu.accounting.fixedAsset.domain.BookFixedAssetCategory;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 固定资产类别Service接口
 *
 *  YM
 * 07-27
 */
public interface IBookFixedAssetCategoryService extends IService<BookFixedAssetCategory> {
    void initInsertFixedAssertCategory(Long bookId,Long templateId);

    List<BookFixedAssetCategory> getList(BookFixedAssetCategory bookFixedAssetCategroy);

    BookFixedAssetCategory getOne(Long bookId);

}
