package com.qifu.accounting.voucher.mapper;

import com.qifu.accounting.voucher.domain.BookVoucherModelPub;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * 凭证模板公用Mapper接口
 *
 *  YM
 * 08-07
 */
public interface BookVoucherModelPubMapper extends BaseMapper<BookVoucherModelPub> {

    List<BookVoucherModelPub> selectListByTemplateId(Long templateId);
}
