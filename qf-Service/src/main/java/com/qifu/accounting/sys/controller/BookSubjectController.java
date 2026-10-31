package com.qifu.accounting.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qifu.accounting.sys.domain.BookSubject;
import com.qifu.accounting.sys.domain.vo.BookSubjectVo;
import com.qifu.accounting.common.enums.BalanceDirectionEnum;
import com.qifu.accounting.sys.domain.vo.TrialBalance;
import com.qifu.accounting.sys.service.IBookSubjectService;
import com.qifu.accounting.voucher.service.IBookVoucherService;
import com.qifu.common.constant.Constants;
import com.qifu.common.utils.Asserts;
import com.qifu.common.utils.StringUtils;
import com.qifu.common.utils.poi.ExcelUtil;
import com.qifu.framework.aspectj.lang.annotation.Log;
import com.qifu.framework.aspectj.lang.enums.BusinessType;
import com.qifu.framework.redis.RedisCache;
import com.qifu.framework.web.controller.BaseController;
import com.qifu.framework.web.domain.AjaxResult;
import com.qifu.framework.web.page.TableDataInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.Subject;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 科目Controller
 *
 *  YM
 * 07-03
 */
@Transactional
@Api(tags = "科目")
@AllArgsConstructor
@RestController
@RequestMapping("/accounting/subject" )
public class BookSubjectController extends BaseController {

    private final IBookSubjectService iBookSubjectService;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private IBookVoucherService voucherService;

    /**
     * 获取科目编号
     * @return
     */
    @ApiOperation("获取科目编号")
    @GetMapping("/subjectCode")
    public synchronized String getSubjectCode(Long subjectId){
        String code = iBookSubjectService.getSubjectCode(subjectId);
        return code;
    }

    /**
     * 查询科目列表
     */
    @ApiOperation("查询科目列表")
    @GetMapping("/list")
    public TableDataInfo list(BookSubject bookSubject)
    {
        startPage();
        List<BookSubject> list = iBookSubjectService.getlist(bookSubject);
        return getDataTable(list);
    }

    /**
     * 导出科目列表
     */
    @ApiOperation("导出科目列表")
    @Log(title = "科目" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(BookSubject bookSubject) {
        LambdaQueryWrapper<BookSubject> lqw = new LambdaQueryWrapper<BookSubject>(bookSubject);
        List<BookSubject> list = iBookSubjectService.list(lqw);
        ExcelUtil<BookSubject> util = new ExcelUtil<BookSubject>(BookSubject. class);
        return util.exportExcel(list, "subject" );
    }

    /**
     * 获取科目详细信息
     */
    @ApiOperation("获取科目详细信息")
    @GetMapping(value = "/{subjectId}" )
    public AjaxResult getInfo(@PathVariable("subjectId" ) Long subjectId) {
        return AjaxResult.success(iBookSubjectService.getById(subjectId));
    }

    @ApiOperation("新增科目校验")
    @PostMapping("/add/check")
    public AjaxResult addSubjectCheck(@Validated @RequestBody BookSubject bookSubject){
        //目前只支持4级
        Asserts.isNull(bookSubject.getCode(),"层级溢出");
        Asserts.isFalse(bookSubject.getCode().length() > 10,"层级溢出");
        Map result = iBookSubjectService.addSubjectCheck(bookSubject);
        return AjaxResult.success(result);
    }

    /**
     * 新增科目
     */
    @ApiOperation("新增科目")
    @PreAuthorize("@ss.hasPermi('accounting:subject:add')")
    @Log(title = "科目" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody BookSubject bookSubject) {
        removeSubjectRedisCache(bookSubject.getBookId());
        //目前只支持4级
        Asserts.isNull(bookSubject.getCode(),"层级溢出");
        Asserts.isFalse(bookSubject.getCode().length() > 10,"层级溢出");
        bookSubject.setBalanceDirectionText(BalanceDirectionEnum.ralanceDirection(bookSubject.getBalanceDirection()));
        bookSubject.setCreateTime(new Date());
        bookSubject.setUpdateTime(new Date());
        bookSubject.setDeletable(Boolean.TRUE);
        bookSubject.setEnable(Boolean.TRUE);
        bookSubject.setIsModel(Boolean.FALSE);
        bookSubject.setNewLevel();
        iBookSubjectService.add(bookSubject);
        return AjaxResult.success();
    }

    /**
     * 修改科目
     */
    @ApiOperation("修改科目")
    @PreAuthorize("@ss.hasPermi('accounting:subject:edit')")
    @Log(title = "科目" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody BookSubject bookSubject) {
        BookSubject oldSubject = iBookSubjectService.getById(bookSubject.getSubjectId());
        removeSubjectRedisCache(oldSubject.getBookId());
        Asserts.isNull(oldSubject,"该科目不存在，保存失败！");
        Asserts.isTrue(bookSubject.getIsEdit(),"科目【"+oldSubject.getCode()+":"+oldSubject.getName()+"】存在子级或者发生余额变动，不能对其进行修改！");
        if(voucherService.haveVoucherBySubject(bookSubject.getSubjectId())){
            //科目余额发生变动，更新不能修改字段
            oldSubject.setIsEdit(Boolean.FALSE);
            iBookSubjectService.updateById(oldSubject);
            return AjaxResult.error("科目【"+oldSubject.getCode()+":"+oldSubject.getName()+"】已被凭证使用，不能对其进行修改!");
        }
        judge(oldSubject,bookSubject);
        iBookSubjectService.edit(bookSubject);
        return AjaxResult.success();
    }

    private void judge(BookSubject oldSubject,BookSubject bookSubject){
        if(oldSubject.getBalanceInit().doubleValue() != 0
                || oldSubject.getBalanceInit().doubleValue() != 0
                || oldSubject.getDebit().doubleValue() != 0
                || oldSubject.getCredit().doubleValue() != 0
        ){
            Asserts.isTrue(oldSubject.getIsPerson() == bookSubject.getIsPerson(),"科目["+oldSubject.getFullName()+"]存在余额或发生额，不允许修改");
            Asserts.isTrue(oldSubject.getIsStock() == bookSubject.getIsStock(),"科目["+oldSubject.getFullName()+"]存在余额或发生额，不允许修改");
            Asserts.isTrue(oldSubject.getIsCustomer() == bookSubject.getIsCustomer(),"科目["+oldSubject.getFullName()+"]存在余额或发生额，不允许修改");
            Asserts.isTrue(oldSubject.getIsProject() == bookSubject.getIsProject(),"科目["+oldSubject.getFullName()+"]存在余额或发生额，不允许修改");
            Asserts.isTrue(oldSubject.getIsDepartment() == bookSubject.getIsDepartment(),"科目["+oldSubject.getFullName()+"]存在余额或发生额，不允许修改");
            Asserts.isTrue(oldSubject.getIsSupplier() == bookSubject.getIsSupplier(),"科目["+oldSubject.getFullName()+"]存在余额或发生额，不允许修改");
            Asserts.isTrue(oldSubject.getIsCashFlow() == bookSubject.getIsCashFlow(),"科目["+oldSubject.getFullName()+"]存在余额或发生额，不允许修改");
            Asserts.isTrue(oldSubject.getIsQuantity() == bookSubject.getIsQuantity(),"科目["+oldSubject.getFullName()+"]存在余额或发生额，不允许修改");
        }
    }

    /**
     * 修改初始化金额
     */
    @ApiOperation("修改科目期初金额")
    @Log(title = "科目" , businessType = BusinessType.UPDATE)
    @PutMapping("/balance")
    public AjaxResult editBalance(BookSubject bookSubject) {
        iBookSubjectService.editBalance(bookSubject);
        return AjaxResult.success();
    }

    /**
     * 删除科目
     */
    @ApiOperation("删除科目")
    @PreAuthorize("@ss.hasPermi('accounting:subject:del')")
    @Log(title = "科目" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{subjectId}" )
    public AjaxResult remove(@PathVariable("subjectId" ) Long subjectId) {
        BookSubject oldSubject = iBookSubjectService.getById(subjectId);
        removeSubjectRedisCache(oldSubject.getBookId());
        Asserts.isNull(oldSubject,"该科目不存在，不能删除！");
        if(oldSubject.getBalanceInit().doubleValue() != 0){
            return AjaxResult.error("科目【"+oldSubject.getCode()+":"+oldSubject.getName()+"】期初余额未清空，不能对其进行删除");
        }
        if(oldSubject.getBalance().doubleValue() != 0){
            return AjaxResult.error("科目【"+oldSubject.getCode()+":"+oldSubject.getName()+"】金额发生变动，不能对其进行删除");
        }
        if(!oldSubject.getDeletable()){
            return AjaxResult.error("该科目不可删除");
        }
        if(voucherService.haveVoucherBySubject(subjectId)){
            //科目余额发生变动，更新不能修改字段
            oldSubject.setIsEdit(Boolean.FALSE);
            iBookSubjectService.updateById(oldSubject);
            return AjaxResult.error("科目【"+oldSubject.getCode()+":"+oldSubject.getName()+"】已被凭证使用，不能对其进行删除!");
        }
        iBookSubjectService.delete(oldSubject);

        return AjaxResult.success();
    }

    private void removeSubjectRedisCache(Long bookId){
        String key = Constants.BOOK_SUBJECT + bookId;
        redisCache.deleteObject(key);
    }

    /**
     * 查询科目（最子级科目）：1.录凭证，2.固定资产，3.结转规则，4.工资模板
     */
    @GetMapping("/searchLeaf")
    public TableDataInfo listForVoucher(Long bookId){
        List<BookSubjectVo> list = iBookSubjectService.searchLeaf(bookId);
        return  getDataTable(list);
    }


    /**
     * 查询科目-所有：1.结转规则取数科目，2.账簿筛选条件及其他模块条件
     */
    @GetMapping("/searchOriginSubject")
    public TableDataInfo listForOriginSubjectList(Long bookId){
        List<BookSubjectVo> list = iBookSubjectService.searchOriginSubject(bookId);
        return  getDataTable(list);
    }


    /**
     * 获取科目余额
     * @param subjectId
     * @return
     */
    @GetMapping("/balance/{subjectId}")
    public AjaxResult getSubjectBalance(@PathVariable("subjectId" ) Long subjectId){
        BookSubject subject = iBookSubjectService.getById(subjectId);
        Map<String,Object> result = new HashMap<>();
        BigDecimal balance = new BigDecimal(0);
        if(subject != null && subject.getBalance() != null){
            balance = subject.getBalance();
        }
        result.put("balance",balance);
        return AjaxResult.success(result);
    }


    /**
     * 试算平衡
     * @param bookId
     * @return
     */
    @GetMapping("/trialBalance")
    public AjaxResult getTrialBalance(Long bookId){
        TrialBalance trialBalance = iBookSubjectService.getTrialBalance(bookId);
        return AjaxResult.success(trialBalance);
    }


    /**
     * 校验科目余额是否可以修改（1.当前账期不是第一期 2.账户有没有过期）
     */
    @GetMapping("/subjectBalanceIsEdit")
    public Map subjectBalanceIsEdit(Long bookId){
        Map<String,Object> result = iBookSubjectService.subjectBalanceIsEdit(bookId);
        return result;
    }
}



