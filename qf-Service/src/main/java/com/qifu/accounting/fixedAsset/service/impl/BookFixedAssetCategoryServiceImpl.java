package com.qifu.accounting.fixedAsset.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qifu.accounting.fixedAsset.domain.BookFixedAssetCategory;
import com.qifu.accounting.sys.mapper.BookSubjectMapper;
import com.qifu.accounting.sys.mapper.BookSubjectTypeMapper;
import com.qifu.common.utils.StringDateUtil;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qifu.accounting.fixedAsset.mapper.BookFixedAssetCategoryMapper;
import com.qifu.accounting.fixedAsset.service.IBookFixedAssetCategoryService;

import javax.annotation.Resource;
import java.util.List;

/**
 * 固定资产类别Service业务层处理
 *
 *  YM
 * 07-27
 */
@Service
public class BookFixedAssetCategoryServiceImpl extends ServiceImpl<BookFixedAssetCategoryMapper, BookFixedAssetCategory> implements IBookFixedAssetCategoryService {

    @Resource
    private BookFixedAssetCategoryMapper fixedAssetCategoryMapper;

    @Resource
    private BookSubjectMapper subjectMapper;

    @Resource
    private BookSubjectTypeMapper typeMapper;

    /**
     * 初始化固定资产类别
     * @param bookId
     */
    public void initInsertFixedAssertCategory(Long bookId,Long templateId){
        fixedAssetCategoryMapper.initInsertFixedAssertCategory(bookId,templateId);
    }

    /**
     * 查询固定资产
     * @param bookFixedAssetCategory
     * @return
     */
    public List<BookFixedAssetCategory> getList(BookFixedAssetCategory bookFixedAssetCategory){
        return fixedAssetCategoryMapper.getList(bookFixedAssetCategory);
    }

    /**
     * 查询最后一位code
     * @param bookId
     * @return
     */
    public BookFixedAssetCategory getOne(Long bookId){
        LambdaQueryWrapper<BookFixedAssetCategory> lqw = new LambdaQueryWrapper<BookFixedAssetCategory>();
        lqw.eq(BookFixedAssetCategory::getBookId,bookId)
                .eq(BookFixedAssetCategory::getDelFlag, StringDateUtil.Del_FLAG_0)
                .orderByDesc(BookFixedAssetCategory::getCreateTime)
                .last("limit 1");
        return fixedAssetCategoryMapper.selectOne(lqw);
    }

}
