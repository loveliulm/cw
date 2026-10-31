package com.qifu.accounting.voucher.mapper;

import com.qifu.accounting.common.enums.VoucherTypeEnum;
import com.qifu.accounting.voucher.domain.BookVoucherModel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qifu.accounting.voucher.domain.vo.BookVoucherModelWageVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 凭证模板Mapper接口
 *
 *  YM
 * 07-16
 */
@Repository
public interface BookVoucherModelMapper extends BaseMapper<BookVoucherModel> {

    BookVoucherModelWageVo selectModelWageVpById(Long modelId);

    BookVoucherModel getVoucherModelByWage(@Param("bookId")Long bookId, @Param("typeEnum") VoucherTypeEnum typeEnum);
}
