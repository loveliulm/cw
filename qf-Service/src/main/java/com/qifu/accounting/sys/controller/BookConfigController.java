package com.qifu.accounting.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import java.util.List;

import com.qifu.accounting.sys.domain.vo.BookConfigVo;
import com.qifu.common.utils.StringDateUtil;
import com.qifu.framework.web.domain.AjaxResult;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.qifu.accounting.sys.domain.BookConfig;
import com.qifu.accounting.sys.service.IBookConfigService;
import com.qifu.framework.web.controller.BaseController;
import com.qifu.framework.web.page.TableDataInfo;

/**
 * 代账基础配置Controller
 *
 *  YM
 * 08-13
 */
@AllArgsConstructor
@RestController
@RequestMapping("/accounting/config" )
public class BookConfigController extends BaseController {

    private final IBookConfigService iBookConfigService;

    /**
     * 查询固定资产默认科目
     */
    @GetMapping("/list")
    public TableDataInfo list(BookConfig bookConfig){
        bookConfig.setType(StringDateUtil.FIXED_ASSET_TYPE);
        List<BookConfigVo> list = iBookConfigService.selectConfigByBookId(bookConfig);
        return getDataTable(list);
    }

    /**
     * 跟新配置-科目
     */
    @PutMapping
    public AjaxResult update(BookConfigVo configVo){
        BookConfig config = new BookConfig();
        config.setConfigId(configVo.getConfigId());
        config.setValue(configVo.getSubjectId().toString());
        iBookConfigService.updateById(config);
        return AjaxResult.success();
    }


    /**
     * 测试-初始化代账基础配置。
     */
    @PostMapping("/init")
    public AjaxResult init(Long bookId, Long templateId){
        iBookConfigService.initInsertConfig(bookId,templateId);
        return AjaxResult.success();
    }


}
