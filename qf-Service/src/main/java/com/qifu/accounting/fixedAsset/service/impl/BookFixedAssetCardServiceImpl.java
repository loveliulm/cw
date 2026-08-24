package com.qifu.accounting.fixedAsset.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qifu.accounting.fixedAsset.domain.BookFixedAssetCategory;
import com.qifu.accounting.fixedAsset.domain.vo.BookFixedAssetCardVo;
import com.qifu.accounting.fixedAsset.mapper.BookFixedAssetCategoryMapper;
import com.qifu.accounting.sys.domain.BookSubject;
import com.qifu.common.utils.StringDateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qifu.accounting.fixedAsset.mapper.BookFixedAssetCardMapper;
import com.qifu.accounting.fixedAsset.domain.BookFixedAssetCard;
import com.qifu.accounting.fixedAsset.service.IBookFixedAssetCardService;

import java.util.List;

/**
 * 固定资产卡片Service业务层处理
 *
 *  yangyang
 * 07-28
 */
@Service
public class BookFixedAssetCardServiceImpl extends ServiceImpl<BookFixedAssetCardMapper, BookFixedAssetCard> implements IBookFixedAssetCardService {

    @Autowired
    private BookFixedAssetCardMapper fixedAssetCardMapper;

    @Autowired
    private BookFixedAssetCategoryMapper fixedAssetCategoryMapper;

    /**
     * 批量修改：父级固定资产涉及得科目改为当前子级科目。
     * @param subject
     * @param fatherSubject
     */
    public void updateFixedAssetFatherSubjectId(BookSubject subject, BookSubject fatherSubject){
        //卡片得科目id
        fixedAssetCardMapper.updateFixedAssetFatherSubjectId(subject.getSubjectId(),fatherSubject.getSubjectId(),subject.getBookId());
        fixedAssetCardMapper.updateDepreciationFatherSubjectId(subject.getSubjectId(),fatherSubject.getSubjectId(),subject.getBookId());
        fixedAssetCardMapper.updateDepreciationFeeFatherSubjectId(subject.getSubjectId(),fatherSubject.getSubjectId(),subject.getBookId());

        //固定资产类别涉及得科目code
        fixedAssetCategoryMapper.updateAssetFatherSubjectCode(subject.getCode(),fatherSubject.getCode(),subject.getBookId());
        fixedAssetCategoryMapper.updateDepreciationFatherSubjectCode(subject.getCode(),fatherSubject.getCode(),subject.getBookId());

    }

    /**
     * 查询列表
     * @param bookFixedAssetCard
     * @return
     */
    public List<BookFixedAssetCardVo> getList(BookFixedAssetCard bookFixedAssetCard){
        return fixedAssetCardMapper.getList(bookFixedAssetCard);
    }

    public BookFixedAssetCard getOne(Long bookId){
        LambdaQueryWrapper<BookFixedAssetCard> lqw = new LambdaQueryWrapper<BookFixedAssetCard>();
        lqw.eq(BookFixedAssetCard::getBookId,bookId)
                .eq(BookFixedAssetCard::getDelFlag, StringDateUtil.Del_FLAG_0)
                .orderByDesc(BookFixedAssetCard::getCreateTime)
                .last("limit 1");
        BookFixedAssetCard card = fixedAssetCardMapper.selectOne(lqw);
        return card;
    }
}
