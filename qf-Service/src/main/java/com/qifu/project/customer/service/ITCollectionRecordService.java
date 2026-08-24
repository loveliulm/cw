package com.qifu.project.customer.service;

import com.qifu.framework.web.domain.AjaxResult;
import com.qifu.project.customer.domain.TCollectionRecord;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 收款明细Service接口
 *
 *  YM
 * 06-28
 */
public interface ITCollectionRecordService extends IService<TCollectionRecord> {

    AjaxResult deleteById(Long id);

}
