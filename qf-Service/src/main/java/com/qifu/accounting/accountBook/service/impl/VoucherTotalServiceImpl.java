package com.qifu.accounting.accountBook.service.impl;

import com.qifu.accounting.accountBook.domain.VoucherTotal;
import com.qifu.accounting.accountBook.domain.vo.ParamsVo;
import com.qifu.accounting.accountBook.domain.vo.SubjectVo;
import com.qifu.accounting.accountBook.mapper.VoucherTotalMapper;
import com.qifu.accounting.accountBook.service.VoucherTotalService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 *  yangyang
 * @date 2020/7/31 10:14
 */
@Service
public class VoucherTotalServiceImpl implements VoucherTotalService {

    @Resource
    private VoucherTotalMapper voucherTotalMapper;

    public List<VoucherTotal> getList(ParamsVo paramsVo){
        List<VoucherTotal> list = voucherTotalMapper.getSubjectList(paramsVo);
        if(list.isEmpty()){
            return new ArrayList<>();
        }
        BigDecimal creTotal = BigDecimal.ZERO;
        BigDecimal debTotal = BigDecimal.ZERO;
        for (VoucherTotal voucherTotal : list) {
            VoucherTotal voucher = voucherTotalMapper.voucherBalance(paramsVo,voucherTotal.getSubjectId());
            voucherTotal.setCredit(voucher.getCredit());
            voucherTotal.setDebit(voucher.getDebit());
            creTotal = creTotal.add(voucher.getCredit());
            debTotal = debTotal.add(voucher.getDebit());
        }
        VoucherTotal voucherTotal = new VoucherTotal();
        voucherTotal.setCredit(creTotal);
        voucherTotal.setDebit(debTotal);
        voucherTotal.setSubjectName("合计");
        list.add(voucherTotal);
        return list;
    }
}
