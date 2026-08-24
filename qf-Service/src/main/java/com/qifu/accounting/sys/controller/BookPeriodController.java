package com.qifu.accounting.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import java.util.List;
import java.util.Arrays;
import java.util.Set;

import com.qifu.accounting.sys.domain.vo.SeasonVo;
import com.qifu.accounting.voucher.domain.BookVoucherModel;
import com.qifu.common.utils.StringDateUtil;
import com.qifu.common.utils.StringUtils;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.qifu.framework.aspectj.lang.annotation.Log;
import com.qifu.framework.aspectj.lang.enums.BusinessType;
import com.qifu.accounting.sys.domain.BookPeriod;
import com.qifu.accounting.sys.service.IBookPeriodService;
import com.qifu.framework.web.controller.BaseController;
import com.qifu.framework.web.domain.AjaxResult;
import com.qifu.common.utils.poi.ExcelUtil;
import com.qifu.framework.web.page.TableDataInfo;

/**
 * 账期Controller
 *
 *  YM
 * 07-17
 */
@AllArgsConstructor
@RestController
@RequestMapping("/accounting/period" )
public class BookPeriodController extends BaseController {

    private final IBookPeriodService iBookPeriodService;

    /**
     * 查询账期列表
     */
    @GetMapping("/list")
    public TableDataInfo list(BookPeriod bookPeriod)
    {
        startPage();
        List<BookPeriod> list = iBookPeriodService.getList(bookPeriod);
        return getDataTable(list);
    }


    /**
     * 查询季度列表
     */
    @GetMapping("/quarterList")
    public TableDataInfo quarterList(BookPeriod bookPeriod)
    {
        List<SeasonVo> list = iBookPeriodService.quarterList(bookPeriod);
        return getDataTable(list);
    }

    /**
     * 查询当前账期
     */
    @GetMapping("/getCurrentPeriod")
    public AjaxResult info(BookPeriod bookPeriod)
    {
        startPage();
        BookPeriod bookPeriod1 = iBookPeriodService.getInfo(bookPeriod.getBookId());
        return AjaxResult.success(bookPeriod1);
    }

    /**
     * 新增当前账期
     */
    @PostMapping
    public AjaxResult add(@RequestBody BookPeriod bookPeriod)
    {
        iBookPeriodService.save(bookPeriod);
        return AjaxResult.success();
    }

    /**
     * 查询账套是否已经结过账 false:未结过，true:已结过
     * 结过了不允许修改金额
     * @param bookId
     * @return
     */
    @GetMapping("/isCarryover")
    public Boolean isCarryover(Long bookId){
        return iBookPeriodService.isCarryover(bookId);
    }

}
