package com.qifu.accounting.common.factory;

import com.qifu.accounting.common.enums.VoucherTypeEnum;
import com.qifu.accounting.fixedAsset.domain.vo.DepreciationSubjectVo;
import com.qifu.accounting.fixedAsset.mapper.BookFixedAssetCardMapper;
import com.qifu.accounting.sys.domain.BookAuxiliary;
import com.qifu.accounting.sys.mapper.BookAuxiliaryMapper;
import com.qifu.accounting.voucher.domain.BookVoucher;
import com.qifu.accounting.voucher.domain.BookVoucherRecord;
import com.qifu.accounting.voucher.domain.vo.BookVoucherModelRecordVo;
import com.qifu.accounting.voucher.service.IBookVoucherService;
import com.qifu.common.utils.Asserts;
import com.qifu.common.utils.DateUtils;
import com.qifu.common.utils.bean.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 固定资产工厂
 *  yangyang
 * @date 2020/8/11 11:48
 */
@Component
public class FixedAssetFactory {
    @Autowired
    private BookFixedAssetCardMapper fixedAssetCardMapper;
    @Autowired
    private IBookVoucherService bookVoucherService;
    @Autowired
    private BookAuxiliaryMapper  bookAuxiliaryMapper;

    /**
     * 测算计提折旧金额
     * @param bookId 账套id
     * @param yearPeriod 当前账期
     * @return
     */
    public BigDecimal getBalanceDepreciation(Long bookId, Date yearPeriod){
        return fixedAssetCardMapper.getBalanceDepreciation(bookId, yearPeriod);
    }

    /**
     * 计提折旧结转-生成凭证
     * @param bookId
     * @param yearPeriod
     */
    public BookVoucher insertDepreciationVoucher(Long bookId, Date yearPeriod,Long carryoverId,BookVoucher voucher){
        List<DepreciationSubjectVo> list = fixedAssetCardMapper.selectDepreciationSubject(bookId, yearPeriod);
        BigDecimal total = getBalanceDepreciation(bookId, yearPeriod);
        Asserts.isFalse(total.doubleValue() == 0,"计提折旧测算金额为0，不能生成凭证");
        voucher.setBookId(bookId)
                .setYearPeriod(yearPeriod)
                .setCreditTotal(total)
                .setDebitTotal(total)
                .setVoucherWord("记")
                .setVoucherType(VoucherTypeEnum.结转凭证)
                .setTypeClassId(carryoverId)
                .setVoucherDate(DateUtils.getLastDayByYearPeriod(yearPeriod));
        List<BookVoucherRecord> details = new ArrayList<>();
        for (DepreciationSubjectVo depreciationSubjectVo : list) {
            BookVoucherRecord voucherRecord = new BookVoucherRecord();
            BeanUtils.copyBeanProp(voucherRecord,depreciationSubjectVo);
            resetAuxiliary(depreciationSubjectVo, voucherRecord);
            details.add(voucherRecord);
        }
        voucher.setDetails(details);
        bookVoucherService.add(voucher);
        return voucher;
    }

    private void resetAuxiliary(DepreciationSubjectVo depreciationSubjectVo, BookVoucherRecord voucherRecord){
        if(depreciationSubjectVo.getAuxiliaryId() != null && depreciationSubjectVo.getAuxiliaryId() != 0){
            BookAuxiliary auxiliary = bookAuxiliaryMapper.selectById(depreciationSubjectVo.getAuxiliaryId());
            switch (auxiliary.getAuxiliaryTypeId().intValue()){
                case 1:
                    voucherRecord.setSupplierId(auxiliary.getAuxiliaryTypeId());
                    break;
                case 2:
                    voucherRecord.setCustomerId(auxiliary.getAuxiliaryTypeId());
                    break;
                case 3:
                    voucherRecord.setProjectId(auxiliary.getAuxiliaryTypeId());
                    break;
                case 4:
                    voucherRecord.setDepartmentId(auxiliary.getAuxiliaryTypeId());
                    break;
                case 5:
                    voucherRecord.setPersonId(auxiliary.getAuxiliaryTypeId());
                    break;
                case 6:
                    voucherRecord.setStockId(auxiliary.getAuxiliaryTypeId());
                    break;
            }
        }
    }
}
