package com.qifu.accounting.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.qifu.accounting.mapper.BookSubjectModelMapper;

import com.qifu.accounting.sys.domain.BookSubjectModel;
import com.qifu.accounting.sys.service.IBookSubjectModelService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 科目模板Service业务层处理
 *
 *  YM
 * 07-03
 */
@Service
public class BookSubjectModelServiceImpl extends ServiceImpl<BookSubjectModelMapper, BookSubjectModel> implements IBookSubjectModelService {
    @Resource
    private BookSubjectModelMapper subjectModelMapper;

}
