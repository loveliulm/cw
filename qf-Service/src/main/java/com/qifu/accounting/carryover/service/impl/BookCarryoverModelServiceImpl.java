package com.qifu.accounting.carryover.service.impl;

import com.qifu.accounting.carryover.domain.BookCarryover;
import com.qifu.accounting.carryover.domain.BookCarryoverRuleModel;
import com.qifu.accounting.carryover.mapper.BookCarryoverMapper;
import com.qifu.accounting.carryover.mapper.BookCarryoverRuleMapper;
import com.qifu.accounting.carryover.mapper.BookCarryoverRuleModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qifu.accounting.carryover.mapper.BookCarryoverModelMapper;
import com.qifu.accounting.carryover.domain.BookCarryoverModel;
import com.qifu.accounting.carryover.service.IBookCarryoverModelService;

import java.util.Date;
import java.util.List;

/**
 * 结转模板Service业务层处理
 *
 *  YM
 * 07-23
 */
@Service
public class BookCarryoverModelServiceImpl extends ServiceImpl<BookCarryoverModelMapper, BookCarryoverModel> implements IBookCarryoverModelService {

    @Autowired
    private BookCarryoverMapper carryoverMapper;

    @Autowired
    private BookCarryoverRuleMapper carryoverRuleMapper;

    @Autowired
    private BookCarryoverModelMapper carryoverModelMapper;

    @Autowired
    private BookCarryoverRuleModelMapper carryoverRuleModelMapper;

    public void insertInitCarryOver(Long bookId, Long accountTemplateId){
        List<BookCarryoverModel> carryoverModelList = carryoverModelMapper.getListByTemplate(accountTemplateId);
        carryoverModelList.forEach(bookCarryoverModel -> {
            BookCarryover carryover = new BookCarryover();
            carryover.setBookId(bookId)
                    .setCode(bookCarryoverModel.getCode())
                    .setText(bookCarryoverModel.getText())
                    .setMoney(bookCarryoverModel.getMoney())
                    .setShowRule(bookCarryoverModel.getShowRule())
                    .setStatus(bookCarryoverModel.getStatus())
                    .setProfitLossOpen(bookCarryoverModel.getProfitLossOpen())
                    .setCreateTime(new Date())
                    .setUpdateTime(new Date());
            carryoverMapper.insert(carryover);
            List<BookCarryoverRuleModel> carryoverRuleModels = carryoverRuleModelMapper.selectListByModelId(bookCarryoverModel.getCarryoverModelId());
            if(!carryoverRuleModels.isEmpty()){
                carryoverRuleMapper.insertInitCarryOverRule(carryoverRuleModels,carryover);
            }
        });
    }
}
