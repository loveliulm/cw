package com.qifu.accounting.sys.controller;

import com.qifu.accounting.sys.domain.BookConfig;
import com.qifu.accounting.sys.domain.vo.AccountBookVo;
import com.qifu.accounting.sys.domain.vo.BookConfigVo;
import com.qifu.accounting.sys.service.IBookLoginService;
import com.qifu.common.utils.StringDateUtil;
import com.qifu.framework.web.controller.BaseController;
import com.qifu.framework.web.domain.AjaxResult;
import com.qifu.framework.web.page.TableDataInfo;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 代账系统登录
 *  yangyang
 * @date 2020/9/25 14:41
 */
@AllArgsConstructor
@RestController
@RequestMapping("/accounting/login" )
public class BookLoginController extends BaseController {

    public final IBookLoginService bookLoginService;

    /**
     * 代账系统登录-获取所有已开账套
     * @param bookId
     * @return
     */
    @GetMapping("/book/list")
    public TableDataInfo list(Long bookId){
        List<AccountBookVo> list = bookLoginService.getAccountList(bookId);
        return getDataTable(list);
    }

    /**
     * 代账系统登录-获取当前账套信息
     * @param bookId
     * @return
     */
    @GetMapping("/book/info")
    public AjaxResult AccountInfo(Long bookId){
        //获取当前账套信息
        AccountBookVo book = bookLoginService.getInfo(bookId);
        return AjaxResult.success(book);
    }

}
