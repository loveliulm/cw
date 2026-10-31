package com.qifu.accounting.sys.service.impl;

import com.qifu.accounting.sys.domain.vo.BookConfigVo;
import com.qifu.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qifu.accounting.sys.mapper.BookConfigMapper;
import com.qifu.accounting.sys.domain.BookConfig;
import com.qifu.accounting.sys.service.IBookConfigService;

import java.util.List;

/**
 * 代账基础配置Service业务层处理
 *
 *  YM
 * 08-13
 */
@Service
public class BookConfigServiceImpl extends ServiceImpl<BookConfigMapper, BookConfig> implements IBookConfigService {

    @Autowired
    private BookConfigMapper bookConfigMapper;

    /**
     * 初始化固定资产科目
     * @param bookId
     * @param templateId
     */
    public void initInsertConfig(Long bookId, Long templateId){
        bookConfigMapper.initInsertConfig(bookId, templateId);
    }

    /**
     * 根据条件查询列表
     * @return
     */
    public List<BookConfig> selectConfigList(BookConfig bookConfig){
        List<BookConfig> configsList = bookConfigMapper.selectBookConfigList(bookConfig);
        return configsList;
    }

    /**
     * 根据条件查询列表
     * @param bookConfig
     * @return
     */
    public List<BookConfigVo> selectConfigByBookId(BookConfig bookConfig){
        List<BookConfigVo> configsList = bookConfigMapper.selectConfigByBookId(bookConfig);
        return configsList;
    }

    /**
     * 根据键名查询参数配置信息
     * @param key
     * @return
     */
    public String selectConfigByKey(String key,Long bookId){
        BookConfig config = new BookConfig();
        config.setKey(key);
        config.setBookId(bookId);
        BookConfig retConfig = bookConfigMapper.selectBookConfig(config);
        if (StringUtils.isNotNull(retConfig)){
            return retConfig.getValue();
        }
        return StringUtils.EMPTY;
    }


}
