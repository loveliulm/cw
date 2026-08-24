package com.qifu.accounting.sys.service;

import com.qifu.accounting.sys.domain.BookConfig;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qifu.accounting.sys.domain.vo.BookConfigVo;

import java.util.List;

/**
 * 代账基础配置Service接口
 *
 *
 * 08-13
 */
public interface IBookConfigService extends IService<BookConfig> {

    void initInsertConfig(Long bookId, Long accountTemplateId);

    List<BookConfig> selectConfigList(BookConfig bookConfig);

    String selectConfigByKey(String Key,Long bookId);

    List<BookConfigVo> selectConfigByBookId(BookConfig bookConfig);
}
