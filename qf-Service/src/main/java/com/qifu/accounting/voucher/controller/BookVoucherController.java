package com.qifu.accounting.voucher.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.qifu.accounting.common.domain.Page;
import com.qifu.accounting.sys.domain.BookPeriod;
import com.qifu.accounting.sys.service.IBookPeriodService;
import com.qifu.accounting.voucher.domain.BookVoucherRecord;
import com.qifu.accounting.voucher.domain.vo.BookVoucherParams;
import com.qifu.accounting.voucher.domain.vo.BookVoucherVo;
import com.qifu.accounting.common.enums.VoucherWordEnum;
import com.qifu.accounting.voucher.mapper.BookVoucherRecordMapper;
import com.qifu.common.utils.Asserts;
import com.qifu.common.utils.SecurityUtils;
import com.qifu.common.utils.StringDateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
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
import com.qifu.accounting.voucher.domain.BookVoucher;
import com.qifu.accounting.voucher.service.IBookVoucherService;
import com.qifu.framework.web.controller.BaseController;
import com.qifu.framework.web.domain.AjaxResult;
import com.qifu.framework.web.page.TableDataInfo;

/**
 * 凭证Controller
 *
 *
 *
 */
@Transactional
@Api(tags = "凭证")
@AllArgsConstructor
@RestController
@RequestMapping("/accounting/voucher" )
public class BookVoucherController extends BaseController {

    private final IBookVoucherService iBookVoucherService;

    private final IBookPeriodService iBookPeriodService;

    private final BookVoucherRecordMapper voucherRecordMapper;

    @ApiOperation("获取凭证数")
    @GetMapping("/getCode")
    public Long getCode(BookVoucher bookVoucher){
        if(bookVoucher.getVoucherWord().isEmpty()){
            bookVoucher.setVoucherWord(VoucherWordEnum.记.name());
        }
        if(bookVoucher.getYearPeriod() == null){
            BookPeriod bookPeriod = iBookPeriodService.getInfo(bookVoucher.getBookId());
            bookVoucher.setYearPeriod(bookPeriod.getYearPeriod());
        }
        return iBookVoucherService.getCode(bookVoucher);
    }

    /**
     * 查询凭证列表
     */
    @ApiOperation("查询凭证列表")
    @GetMapping("/list")
    public TableDataInfo list(BookVoucherParams bookVoucher, Page page)
    {
        //自定义分页。加快查询速度
        bookVoucher.resetAuxiliary();
        List<BookVoucherVo> list = iBookVoucherService.getList(bookVoucher,page);
        int total = iBookVoucherService.getListTotal(bookVoucher);
        return getDataTableByPage(list,total);
    }

    /**
     * 获取凭证详细信息
     */
    @ApiOperation("获取凭证详细信息")
    @GetMapping(value = "/{voucherId}" )
    @PreAuthorize("@ss.hasPermi('accounting:voucher:info')")
    public AjaxResult getInfo(@PathVariable("voucherId" ) Long voucherId) {
        BookVoucher bookVoucher = iBookVoucherService.getInfo(voucherId);
        return AjaxResult.success(bookVoucher);
    }

    /**
     * 新增凭证
     */
    @ApiOperation("新增凭证")
    @Log(title = "凭证" , businessType = BusinessType.INSERT)
    @PostMapping
    @PreAuthorize("@ss.hasPermi('accounting:voucher:add')")
    public AjaxResult add(@Validated @RequestBody BookVoucher bookVoucher) {
        iBookVoucherService.add(bookVoucher);
        Map<String,Long> result = new HashMap<>();
        result.put("voucherId",bookVoucher.getVoucherId());
        return AjaxResult.success(result);
    }

    /**
     * 修改凭证
     */
    @PreAuthorize("@ss.hasPermi('accounting:voucher:edit')")
    @ApiOperation("修改凭证")
    @Log(title = "凭证" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody BookVoucher bookVoucher) {
        conditionEdit(bookVoucher);
        iBookVoucherService.edit(bookVoucher);
        return AjaxResult.success();
    }

    /**
     * 删除凭证
     */
    @PreAuthorize("@ss.hasPermi('accounting:voucher:del')")
    @ApiOperation("删除凭证")
    @Log(title = "凭证" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{voucherIds}" )
    public AjaxResult remove(@PathVariable Long[] voucherIds) {
        Asserts.isNull(voucherIds,"凭证id不能为空");
        iBookVoucherService.delete(voucherIds);
        return AjaxResult.success();
    }

    /**
     * 审核
     */
    @PreAuthorize("@ss.hasPermi('accounting:voucher:review')")
    @ApiOperation("审核凭证")
    @Log(title = "凭证" , businessType = BusinessType.DELETE)
    @PutMapping("/review/{voucherIds}" )
    public AjaxResult review(@PathVariable Long[] voucherIds) {
        for (int i = 0; i < voucherIds.length; i++) {
            BookVoucher bookVoucher = iBookVoucherService.getById(voucherIds[i]);
            condition(bookVoucher);
            bookVoucher.setStatus(StringDateUtil.VOUCHER_STATUS_1);
            bookVoucher.setUpdateTime(new Date());
            iBookVoucherService.review(bookVoucher);
        }
        return AjaxResult.success();
    }

    /**
     * 反审核
     */
    @PreAuthorize("@ss.hasPermi('accounting:voucher:review')")
    @ApiOperation("反审核凭证")
    @Log(title = "凭证" , businessType = BusinessType.DELETE)
    @PutMapping("/reviewBack/{voucherIds}" )
    public AjaxResult reviewBack(@PathVariable Long[] voucherIds) {
        for (int i = 0; i < voucherIds.length; i++) {
            BookVoucher bookVoucher = iBookVoucherService.getById(voucherIds[i]);
            condition(bookVoucher);
            bookVoucher.setStatus(StringDateUtil.VOUCHER_STATUS_0);
            bookVoucher.setUpdateTime(new Date());
            iBookVoucherService.review(bookVoucher);
        }
        return AjaxResult.success();
    }

    private void condition(BookVoucher bookVoucher){
        Asserts.isNull(bookVoucher,"请选择凭证！");
        Asserts.isNull(bookVoucher.getVoucherId(),"请选择凭证！");
        Asserts.isTrue(bookVoucher.getDelFlag().equals(StringDateUtil.Del_FLAG_0),"凭证不存在，或已删除，操作失败！");
    }

    private void conditionEdit(BookVoucher bookVoucher){
        Asserts.isNull(bookVoucher,"请选择凭证！");
        Asserts.isNull(bookVoucher.getVoucherId(),"请选择凭证！");
        BookVoucher bookVoucher1 = iBookVoucherService.getById(bookVoucher.getVoucherId());
        Asserts.isNull(bookVoucher1,"该凭证不存在！");
        Asserts.isFalse(bookVoucher1.getStatus() == 1,"该凭证已审核不能进行操作！");
        Asserts.isTrue(bookVoucher1.getDelFlag().equals(StringDateUtil.Del_FLAG_0),"该凭证不存在或已被删除！");
    }

    /**
     * 复制凭证
     */
    @ApiOperation("复制凭证")
    @PreAuthorize("@ss.hasPermi('accounting:voucher:copy')")
    @Log(title = "凭证" , businessType = BusinessType.INSERT)
    @PostMapping("/copy/{voucherIds}")
    public AjaxResult copy(@PathVariable Long[] voucherIds) {
        for (int i = 0; i < voucherIds.length; i++) {
            BookVoucher bookVoucher = iBookVoucherService.getById(voucherIds[i]);
            List<BookVoucherRecord> details = voucherRecordMapper.selectListByVoucherId(voucherIds[i]);
            bookVoucher.setDetails(details);
            condition(bookVoucher);
            bookVoucher.setVoucherId(null);
            bookVoucher.setCreateTime(new Date());
            bookVoucher.setUpdateTime(new Date());
            bookVoucher.setCreateUserId(SecurityUtils.userId());
            bookVoucher.setVoucherNo(iBookVoucherService.getCode(bookVoucher));
            bookVoucher.setVoucherCode();
            bookVoucher.setDelFlag(StringDateUtil.Del_FLAG_0);
            iBookVoucherService.add(bookVoucher);
        }
        return AjaxResult.success();
    }
}
