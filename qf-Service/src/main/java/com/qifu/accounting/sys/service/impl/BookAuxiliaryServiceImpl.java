package com.qifu.accounting.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qifu.accounting.voucher.domain.BookVoucherModelRecord;
import com.qifu.accounting.voucher.domain.BookVoucherRecord;
import com.qifu.accounting.sys.domain.vo.BookAuxiliaryVo;
import com.qifu.accounting.voucher.mapper.BookVoucherModelRecordMapper;
import com.qifu.accounting.voucher.mapper.BookVoucherRecordMapper;
import com.qifu.common.utils.Asserts;
import com.qifu.common.utils.StringDateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qifu.accounting.sys.mapper.BookAuxiliaryMapper;
import com.qifu.accounting.sys.domain.BookAuxiliary;
import com.qifu.accounting.sys.service.IBookAuxiliaryService;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 辅组核算Service业务层处理
 *
 *
 * 07-09
 */
@Service
public class BookAuxiliaryServiceImpl extends ServiceImpl<BookAuxiliaryMapper, BookAuxiliary> implements IBookAuxiliaryService {

    @Autowired
    private BookAuxiliaryMapper auxiliaryMapper;

    @Autowired
    private BookVoucherRecordMapper voucherRecordMapper;

    @Autowired
    private BookVoucherModelRecordMapper voucherModelRecordMapper;

    public BookAuxiliary getOne(Long bookId, Long auxiliaryTypeId){
        LambdaQueryWrapper<BookAuxiliary> lqw = new LambdaQueryWrapper<BookAuxiliary>();
        lqw.eq(BookAuxiliary::getAuxiliaryTypeId,auxiliaryTypeId)
                .eq(BookAuxiliary::getBookId,bookId)
                .eq(BookAuxiliary::getDelFlag, StringDateUtil.Del_FLAG_0)
                .orderByDesc(BookAuxiliary::getCreateTime)
                .last("limit 1");
        return auxiliaryMapper.selectOne(lqw);
    }

    /**
     * 根据凭证明细id查询辅助核算集合
     * @param voucherRecordId
     * @return
     */
    @Transactional
    public List<BookAuxiliaryVo> getListByVoucherRecord(Long voucherRecordId){
        BookVoucherRecord record = voucherRecordMapper.selectById(voucherRecordId);
        if(record == null){
            return null;
        }
        List<BookAuxiliaryVo> list = new ArrayList<>();
        Long supplierId = record.getSupplierId();
        Long customerId = record.getCustomerId();
        Long projectId = record.getProjectId();
        Long departmentId = record.getDepartmentId();
        Long personId = record.getPersonId();
        Long stockId = record.getStockId();
        if(supplierId != null && supplierId != 0){
            list.add(auxiliaryMapper.getAuxiliary(supplierId));
        }
        if(customerId != null && customerId != 0){
            list.add(auxiliaryMapper.getAuxiliary(customerId));
        }
        if(projectId != null && projectId != 0){
            list.add(auxiliaryMapper.getAuxiliary(projectId));
        }
        if(departmentId != null && departmentId != 0){
            list.add(auxiliaryMapper.getAuxiliary(departmentId));
        }
        if(personId != null && personId != 0){
            list.add(auxiliaryMapper.getAuxiliary(personId));
        }
        if(stockId != null && stockId != 0){
            list.add(auxiliaryMapper.getAuxiliary(stockId));
        }
        return list;
    }


    /**
     * 根据凭证模板明细id查询辅助核算集合
     * @param voucherModelRecordId
     * @return
     */
    @Transactional
    public List<BookAuxiliaryVo> getListByVoucherModelRecord(Long voucherModelRecordId){
        BookVoucherModelRecord record = voucherModelRecordMapper.selectById(voucherModelRecordId);
        if(record == null){
            return null;
        }
        List<BookAuxiliaryVo> list = new ArrayList<>();
        Long supplierId = record.getSupplierId();
        Long customerId = record.getCustomerId();
        Long projectId = record.getProjectId();
        Long departmentId = record.getDepartmentId();
        Long personId = record.getPersonId();
        Long stockId = record.getStockId();
        if(supplierId != null && supplierId != 0){
            list.add(auxiliaryMapper.getAuxiliary(supplierId));
        }
        if(customerId != null && customerId != 0){
            list.add(auxiliaryMapper.getAuxiliary(customerId));
        }
        if(projectId != null && projectId != 0){
            list.add(auxiliaryMapper.getAuxiliary(projectId));
        }
        if(departmentId != null && departmentId != 0){
            list.add(auxiliaryMapper.getAuxiliary(departmentId));
        }
        if(personId != null && personId != 0){
            list.add(auxiliaryMapper.getAuxiliary(personId));
        }
        if(stockId != null && stockId != 0){
            list.add(auxiliaryMapper.getAuxiliary(stockId));
        }
        return list;
    }


    public void checkAuxiliaryName(BookAuxiliary bookAuxiliary){
        Asserts.isNull(bookAuxiliary.getName(),"辅助核算名称不能为空！");
        Asserts.isNull(bookAuxiliary.getAuxiliaryTypeId(),"辅助核算类型编号不能为空！");
        LambdaQueryWrapper<BookAuxiliary> lqw = new LambdaQueryWrapper<BookAuxiliary>();
        if(bookAuxiliary.getAuxiliaryId() != null){
            lqw.ne(BookAuxiliary::getAuxiliaryId,bookAuxiliary.getAuxiliaryId());
        }
        lqw.eq(BookAuxiliary::getAuxiliaryTypeId,bookAuxiliary.getAuxiliaryTypeId())
                .eq(BookAuxiliary::getBookId,bookAuxiliary.getBookId())
                .eq(BookAuxiliary::getDelFlag, StringDateUtil.Del_FLAG_0)
                .eq(BookAuxiliary::getName,bookAuxiliary.getName())
                .last("limit 1");
        BookAuxiliary oldBookAuxiliary = auxiliaryMapper.selectOne(lqw);
        Asserts.notNull(oldBookAuxiliary,"该辅助核算名称已存在！");
    }
}
