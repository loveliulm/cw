package com.qifu.accounting.carryover.service;

import com.qifu.accounting.carryover.domain.BookCarryoverModel;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 结转模板Service接口
 *
 *  YM
 * 07-23
 */
public interface IBookCarryoverModelService extends IService<BookCarryoverModel> {

    void insertInitCarryOver(Long bookId, Long accountTemplateId);
}
