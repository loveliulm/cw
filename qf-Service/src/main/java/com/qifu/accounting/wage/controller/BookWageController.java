package com.qifu.accounting.wage.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qifu.accounting.common.enums.VoucherModelType;
import com.qifu.accounting.common.enums.VoucherTypeEnum;
import com.qifu.accounting.sys.domain.BookAuxiliary;
import com.qifu.accounting.sys.domain.BookPeriod;
import com.qifu.accounting.sys.service.IBookPeriodService;
import com.qifu.accounting.voucher.domain.BookVoucher;
import com.qifu.accounting.voucher.domain.BookVoucherModel;
import com.qifu.accounting.voucher.domain.BookVoucherRecord;
import com.qifu.accounting.voucher.service.IBookVoucherService;
import com.qifu.accounting.wage.domain.BookWage;
import com.qifu.accounting.wage.mapper.BookWageMapper;
import com.qifu.accounting.wage.service.IBookWageService;
import com.qifu.common.exception.CustomException;
import com.qifu.common.utils.*;
import com.qifu.common.utils.bean.BeanUtils;
import com.qifu.common.utils.poi.ExcelUtil;
import com.qifu.framework.aspectj.lang.annotation.Log;
import com.qifu.framework.aspectj.lang.enums.BusinessType;
import com.qifu.framework.security.LoginUser;
import com.qifu.framework.web.controller.BaseController;
import com.qifu.framework.web.domain.AjaxResult;
import com.qifu.framework.web.page.TableDataInfo;
import com.qifu.project.system.domain.SysUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Value;
import org.apache.ibatis.annotations.Param;
import org.aspectj.weaver.loadtime.Aj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.print.Book;
import java.math.BigDecimal;
import java.security.Key;
import java.util.*;

/**
 *  yangyang
 * 工资
 * @date 2020/7/20 14:34
 */
@Api(tags = "工资")
@Transactional
@AllArgsConstructor
@RestController
@RequestMapping("/accounting/wage" )
public class BookWageController extends BaseController {

    private final IBookWageService bookWageService;

    private final BookWageMapper bookWageMapper;
    @Autowired
    private IBookPeriodService periodService;


    /**
     * 查询工资列表
     * @param bookWage
     * @return
     */
    @ApiOperation("查询工资列表")
    @GetMapping("/list")
    public TableDataInfo list(BookWage bookWage)
    {
        startPage();
        List<BookWage> list = bookWageService.getList(bookWage);
        return getDataTable(list);
    }

    /**
     * 工资合计查询
     * @param bookId
     * @return
     */
    @GetMapping("/getWagesTotalPay")
    public Map<Object,Object> getWagesTotal(Long bookId, Date yearPeriod){
        BookPeriod period = periodService.getInfo(bookId);
        if(yearPeriod == null){
            yearPeriod = period.getYearPeriod();
        }
        BookWage bookWageTotal = bookWageMapper.getTotalBalanceByWageTotal(yearPeriod,bookId);
        Map<Object,Object> result = new HashMap<>();
        result.put("grossPayTotal",bookWageTotal.getGrossPay());
        result.put("netPayTotal",bookWageTotal.getNetPay());
        return result;
    }

    /**
     * 判断计提，发放工资是否生成
     * @param bookId
     * @return
     */
    @GetMapping("/checkWages")
    public Map<Object,Object> checkWages(Long bookId){
        Map<Object,Object> result = bookWageService.checkWages(bookId);
        return result;
    }

    /**
     * 导出工资列表
     */
    @Log(title = "工资" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(BookWage bookWage) {
        List<BookWage> list = bookWageService.getList(bookWage);
        ExcelUtil<BookWage> util = new ExcelUtil<BookWage>(BookWage. class);
        return util.exportExcel(list, "wage" );
    }


    /**
     * 导入模板下载
     * @return
     */
    @GetMapping("/importTemplate")
    public AjaxResult importTemplate()
    {
        ExcelUtil<BookWage> util = new ExcelUtil<BookWage>(BookWage.class);
        return util.importTemplateExcel("用户数据");
    }

    /**
     * 工资导入
     * @param file
     * @param updateSupport
     * @return
     * @throws Exception
     */
    @Log(title = "工资", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception
    {
        ExcelUtil<BookWage> util = new ExcelUtil<BookWage>(BookWage.class);
        List<BookWage> list = util.importExcel(file.getInputStream());
        if (StringUtils.isNull(list) || list.size() == 0){
            throw new CustomException("导入数据不能为空！");
        }
        String message = bookWageService.importList(list);
        return AjaxResult.success(message);
    }

    /**
     * 获取工资详细信息
     */
    @PreAuthorize("@ss.hasPermi('accounting:wage:info')")
    @GetMapping(value = "/{id}" )
    public AjaxResult getInfo(@PathVariable("id" ) Long id) {

        return AjaxResult.success(bookWageService.getWageById(id));
    }

    /**
     * 新增工资
     */
    @PreAuthorize("@ss.hasPermi('accounting:wage:add')")
    @ApiOperation("新增工资")
    @Log(title = "工资" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BookWage bookWage) {
        bookWage.setCreateTime(new Date());
        bookWage.setUpdateTime(new Date());
        boolean b = bookWageService.save(bookWage);
        Asserts.isTrue(b,"添加失败");
        return AjaxResult.success();
    }

    /**
     * 修改工资
     */
    @PreAuthorize("@ss.hasPermi('accounting:wage:edit')")
    @ApiOperation("修改工资")
    @Log(title = "工资" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BookWage bookWage) {
        bookWage.setUpdateTime(new Date());
        boolean b = bookWageService.updateById(bookWage);
        Asserts.isTrue(b,"修改失败");
        return AjaxResult.success();
    }

    /**
     * 删除工资
     */
    @PreAuthorize("@ss.hasPermi('accounting:wage:del')")
    @ApiOperation("删除工资")
    @Log(title = "工资" , businessType = BusinessType.DELETE)
    @DeleteMapping("{ids}")
    public AjaxResult delete(@PathVariable Long [] ids) {
        for (int i = 0; i < ids.length; i++) {
            BookWage wage = new BookWage();
            wage.setId(ids[i]);
            wage.setUpdateTime(new Date());
            wage.setDelFlag(StringDateUtil.Del_FLAG_2);
            boolean b = bookWageService.updateById(wage);
            Asserts.isTrue(b,"删除工资");
        }

        return AjaxResult.success();
    }

    /**
     * 计提工资--本月
     */
    @ApiOperation("计提工资")
    @Log(title = "计提工资" , businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasPermi('accounting:wage:provisionWage')")
    @PostMapping("/provision")
    public Map<?,?> provisionWage(Long bookId) {
        Map<Object, Object> map = checkWages(bookId);
        Boolean b = (Boolean) map.get("provision");
        Asserts.isFalse(b,"计提工资已生成。");
        Map<?,?> result = bookWageService.wageTemplate(bookId, VoucherTypeEnum.计提工资);
        return result;
    }

    /**
     * 发放工资--上月
     */
    @ApiOperation("发放工资")
    @PreAuthorize("@ss.hasPermi('accounting:wage:grantWage')")
    @Log(title = "发放工资" , businessType = BusinessType.UPDATE)
    @PostMapping("/grant")
    public Map<?,?> grantWage(Long bookId) {
        Map<Object, Object> map = checkWages(bookId);
        Boolean b = (Boolean) map.get("grant");
        Asserts.isFalse(b,"发放工资已生成。");
        Map<?,?> result = bookWageService.wageTemplate(bookId, VoucherTypeEnum.发放工资);
        return result;
    }
}
