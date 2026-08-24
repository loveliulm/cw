package com.qifu.accounting.sys.mapper;

import com.qifu.accounting.sys.domain.BookConfig;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qifu.accounting.sys.domain.vo.BookConfigVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 代账基础配置Mapper接口
 *
 *  YM
 * 08-13
 */
@Repository
public interface BookConfigMapper extends BaseMapper<BookConfig> {

    void initInsertConfig(@Param("bookId")Long bookId,@Param("templateId") Long templateId);

    BookConfig selectBookConfig(BookConfig config);

    List<BookConfig> selectBookConfigList(BookConfig config);

    List<BookConfigVo> selectConfigByBookId(BookConfig bookConfig);
}
