package com.qifu.accounting.fixedAsset.service;

import com.qifu.accounting.fixedAsset.domain.BookFixedAssetCard;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qifu.accounting.fixedAsset.domain.vo.BookFixedAssetCardVo;
import com.qifu.accounting.sys.domain.BookSubject;

import java.util.List;

/**
 * 固定资产卡片Service接口
 *
 *  YM
 * 07-28
 */
public interface IBookFixedAssetCardService extends IService<BookFixedAssetCard> {

    void updateFixedAssetFatherSubjectId(BookSubject subject, BookSubject fatherSubject);

    List<BookFixedAssetCardVo> getList(BookFixedAssetCard bookFixedAssetCard);

    BookFixedAssetCard getOne(Long bookId);
}
