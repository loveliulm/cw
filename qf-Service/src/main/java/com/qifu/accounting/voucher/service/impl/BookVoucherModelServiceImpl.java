package com.qifu.accounting.voucher.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qifu.accounting.common.enums.BalanceDirectionEnum;
import com.qifu.accounting.common.enums.FiledCodeEnum;
import com.qifu.accounting.common.enums.VoucherModelType;
import com.qifu.accounting.sys.domain.BookSubject;
import com.qifu.accounting.sys.domain.vo.BookAuxiliaryVo;
import com.qifu.accounting.sys.mapper.BookAuxiliaryMapper;
import com.qifu.accounting.sys.service.IBookAuxiliaryService;
import com.qifu.accounting.voucher.domain.*;
import com.qifu.accounting.voucher.domain.vo.BookVoucherModelRecordVo;
import com.qifu.accounting.voucher.domain.vo.BookVoucherModelWageRecordVo;
import com.qifu.accounting.voucher.domain.vo.BookVoucherModelWageVo;
import com.qifu.accounting.voucher.mapper.BookVoucherModelPubMapper;
import com.qifu.accounting.voucher.mapper.BookVoucherModelRecordMapper;
import com.qifu.accounting.voucher.mapper.BookVoucherModelRecordPubMapper;
import com.qifu.common.utils.Asserts;
import com.qifu.common.utils.SecurityUtils;
import com.qifu.common.utils.StringDateUtil;
import com.qifu.common.utils.StringUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qifu.accounting.voucher.mapper.BookVoucherModelMapper;
import com.qifu.accounting.voucher.service.IBookVoucherModelService;

import javax.annotation.Resource;
import java.awt.print.Book;
import java.util.Date;
import java.util.List;

/**
 * 凭证模板Service业务层处理
 *
 *  YM
 * 07-16
 */
@Service
public class BookVoucherModelServiceImpl extends ServiceImpl<BookVoucherModelMapper, BookVoucherModel> implements IBookVoucherModelService {

    @Resource
    private BookVoucherModelMapper modelMapper;

    @Resource
    private BookVoucherModelRecordMapper modelRecordMapper;

    @Resource
    private IBookAuxiliaryService auxiliaryService;

    @Resource
    private BookVoucherModelPubMapper modelPubMapper;

    @Resource
    private BookVoucherModelRecordPubMapper modelRecordPubMapper;

    /**
     * 初始化工资凭证模板
     * @param bookId
     * @param templateId
     */
    public void InitWageVoucherModel(Long bookId,Long templateId){
        List<BookVoucherModelPub> pubs = modelPubMapper.selectListByTemplateId(templateId);
        pubs.forEach(bookVoucherModelPub -> {
            BookVoucherModel model = new BookVoucherModel();
            model.setName(bookVoucherModelPub.getName())
                    .setRemark(bookVoucherModelPub.getRemark())
                    .setSection(bookVoucherModelPub.getSection())
                    .setCreateUserId(SecurityUtils.userId())
                    .setType(bookVoucherModelPub.getType())
                    .setCreateTime(new Date())
                    .setBookId(bookId)
                    .setUpdateTime(new Date());
            modelMapper.insert(model);
            List<BookVoucherModelRecordPub> recordPubs = modelRecordPubMapper.selectListByPubId(bookVoucherModelPub.getId());
            modelRecordMapper.InitInsertList(recordPubs,model.getModelId(),bookId);
        });
    }

    /**
     * 新增
     * @param bookVoucherModel
     */
    public void add(BookVoucherModel bookVoucherModel){
        diffName(bookVoucherModel);
        bookVoucherModel.setCreateTime(new Date());
        bookVoucherModel.setUpdateTime(new Date());
        bookVoucherModel.setCreateUserId(SecurityUtils.userId());
        modelMapper.insert(bookVoucherModel);

        List<BookVoucherModelRecord> list = bookVoucherModel.getDetails();
        list.forEach(bookVoucherModelRecord -> {
            if(bookVoucherModel.getIsAmount() == null || !bookVoucherModel.getIsAmount()){
                bookVoucherModelRecord.setCredit(null);
                bookVoucherModelRecord.setDebit(null);
            }
            bookVoucherModelRecord.setCreateTime(new Date());
            bookVoucherModelRecord.setUpdateTime(new Date());
            bookVoucherModelRecord.setModelId(bookVoucherModel.getModelId());
            bookVoucherModelRecord.setBalanceDirectionText(BalanceDirectionEnum.ralanceDirection(bookVoucherModelRecord.getBalanceDirection()));
            modelRecordMapper.insert(bookVoucherModelRecord);
        });
    }


    private void diffName(BookVoucherModel bookVoucherModel){
        Asserts.isNull(bookVoucherModel.getBookId(),"bookId不能为空");
        Asserts.isNull(bookVoucherModel.getName(),"模板名称不能为空");
        LambdaQueryWrapper<BookVoucherModel> lqw = new LambdaQueryWrapper<BookVoucherModel>();
        if (bookVoucherModel.getBookId() != null){
            lqw.eq(BookVoucherModel::getBookId ,bookVoucherModel.getBookId());
        }
        if (bookVoucherModel.getModelId() != null){
            lqw.ne(BookVoucherModel::getModelId ,bookVoucherModel.getModelId());
        }
        if (StringUtils.isNotBlank(bookVoucherModel.getName())){
            lqw.eq(BookVoucherModel::getName ,bookVoucherModel.getName());
        }
        lqw.eq(BookVoucherModel::getDelFlag, StringDateUtil.Del_FLAG_0);
        lqw.eq(BookVoucherModel::getType, VoucherModelType.普通凭证模板);
        lqw.last("limit 1");
        BookVoucherModel model = modelMapper.selectOne(lqw);
        Asserts.notNull(model,"模板名称已存在！");
    }

    /**
     * 删除
     * @param modelId
     */
    public void delete(Long modelId){
        BookVoucherModel model = new BookVoucherModel();
        model.setModelId(modelId);
        model.setDelFlag(StringDateUtil.Del_FLAG_2);
        model.setUpdateTime(new Date());
        modelMapper.updateById(model);
    }

    /**
     * 修改
     * @param bookVoucherModel
     */
    public void edit(BookVoucherModel bookVoucherModel){
        diffName(bookVoucherModel);
        bookVoucherModel.setUpdateTime(new Date());
        modelMapper.updateById(bookVoucherModel);
    }

    /**
     * 获取详情
     * @param modelId
     * @return
     */
    public BookVoucherModel getInfo(Long modelId){
        BookVoucherModel model = modelMapper.selectById(modelId);
        List<BookVoucherModelRecordVo> detail = modelRecordMapper.selectListByModelId(modelId);
        model.setDetail(detail);
        detail.forEach(bookVoucherModelRecordVo -> {
            List<BookAuxiliaryVo> auxiliaryVos = auxiliaryService.getListByVoucherModelRecord(bookVoucherModelRecordVo.getModelRecordId());
            bookVoucherModelRecordVo.setAuxiliarys(auxiliaryVos);
        });
        return model;
    }

    /*----------------------------------------工资模板-------------------------------------------*/

    /**
     * 工资模板-获取详情
     * @param modelId
     * @return
     */
    public BookVoucherModelWageVo getInfoByWage(Long modelId){
        BookVoucherModelWageVo model = modelMapper.selectModelWageVpById(modelId);
        List<BookVoucherModelWageRecordVo> detail = modelRecordMapper.selectWageRecordListByModelId(modelId);
        model.setDetail(detail);
        detail.forEach(bookVoucherModelRecordVo -> {
            List<BookAuxiliaryVo> auxiliaryVos = auxiliaryService.getListByVoucherModelRecord(bookVoucherModelRecordVo.getModelRecordId());
            bookVoucherModelRecordVo.setAuxiliarys(auxiliaryVos);
        });
        return model;
    }

    /**
     * 工资模板-修改
     * @param bookVoucherModel
     */
    public void editByWage(BookVoucherModel bookVoucherModel){
        bookVoucherModel.setUpdateTime(new Date());
        modelMapper.updateById(bookVoucherModel);

        //删除老的模板详情数据
        modelRecordMapper.deleteOldVoucherModelWage(bookVoucherModel.getModelId());

        //新增新的模板详情数据
        List<BookVoucherModelRecord> list = bookVoucherModel.getDetails();
        list.forEach(bookVoucherModelRecord -> {
            bookVoucherModelRecord.setCreateTime(new Date());
            bookVoucherModelRecord.setUpdateTime(new Date());
            bookVoucherModelRecord.setModelId(bookVoucherModel.getModelId());
            bookVoucherModelRecord.setBalanceDirectionText(BalanceDirectionEnum.ralanceDirection(bookVoucherModelRecord.getBalanceDirection()));
            bookVoucherModelRecord.setFiledText(FiledCodeEnum.getFiledText(bookVoucherModelRecord.getFiledCode()));
            modelRecordMapper.insert(bookVoucherModelRecord);
        });
    }
}
