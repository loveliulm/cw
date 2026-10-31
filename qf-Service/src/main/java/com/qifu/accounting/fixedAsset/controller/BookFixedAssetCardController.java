package com.qifu.accounting.fixedAsset.controller;

import java.util.Date;
import java.util.List;

import com.qifu.accounting.common.factory.FixedAssetFactory;
import com.qifu.accounting.fixedAsset.domain.vo.BookFixedAssetCardVo;
import com.qifu.accounting.sys.domain.BookSubject;
import com.qifu.accounting.sys.service.IBookPeriodService;
import com.qifu.accounting.sys.service.IBookSubjectService;
import com.qifu.common.utils.MakerNum;
import com.qifu.common.utils.StringDateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.aspectj.weaver.loadtime.Aj;
import org.springframework.security.access.prepost.PreAuthorize;
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
import com.qifu.accounting.fixedAsset.domain.BookFixedAssetCard;
import com.qifu.accounting.fixedAsset.service.IBookFixedAssetCardService;
import com.qifu.framework.web.controller.BaseController;
import com.qifu.framework.web.domain.AjaxResult;
import com.qifu.common.utils.poi.ExcelUtil;
import com.qifu.framework.web.page.TableDataInfo;

/**
 * 固定资产卡片Controller
 *
 *  YM
 * 07-28
 */
@Api(tags = "固定资产")
@AllArgsConstructor
@RestController
@RequestMapping("/accounting/fixedAssetCard" )
public class BookFixedAssetCardController extends BaseController {

    private final IBookFixedAssetCardService iBookFixedAssetCardService;

    private final FixedAssetFactory fixedAssetFactory;

    private final IBookSubjectService iBookSubjectService;

    private final IBookPeriodService bookPeriodService;

    /**
     * 获取编码
     * @param bookId
     * @return
     */
    @GetMapping("/getCode")
    public String getCode(Long bookId){
        BookFixedAssetCard card = iBookFixedAssetCardService.getOne(bookId);
        if(card == null){
            return MakerNum.makerNumRuleLength4(null);
        }else {
            return MakerNum.makerNumRuleLength4(card.getCardCode());
        }
    }

    /**
     * 查询固定资产卡片列表
     */
    @PreAuthorize("@ss.hasPermi('accounting:fixedAssetCard:list')")
    @ApiOperation("查询固定资产卡片列表")
    @GetMapping("/list")
    public TableDataInfo getList(BookFixedAssetCard bookFixedAssetCard)
    {
        startPage();
        List<BookFixedAssetCardVo> list = iBookFixedAssetCardService.getList(bookFixedAssetCard);
        return getDataTable(list);
    }

    /**
     * 导出固定资产卡片列表
     */
    //@PreAuthorize("@ss.hasPermi('accounting:fixedAssetCard:export')" )
    @Log(title = "固定资产卡片" , businessType = BusinessType.EXPORT)
    @GetMapping("/export" )
    public AjaxResult export(BookFixedAssetCard bookFixedAssetCard) {
        List<BookFixedAssetCardVo> list = iBookFixedAssetCardService.getList(bookFixedAssetCard);
        ExcelUtil<BookFixedAssetCardVo> util = new ExcelUtil<BookFixedAssetCardVo>(BookFixedAssetCardVo.class);
        return util.exportExcel(list, "fixedAssetCard" );
    }

    /**
     * 获取固定资产卡片详细信息
     */
    //@PreAuthorize("@ss.hasPermi('accounting:fixedAssetCard:query')" )
    @GetMapping(value = "/{cardId}" )
    public AjaxResult getInfo(@PathVariable("cardId" ) Long cardId) {
        BookFixedAssetCard card = iBookFixedAssetCardService.getById(cardId);
        card.setFixedAssetSubjectNameBy(resetSubjectNameBy(card.getFixedAssetSubjectId()));
        card.setDepreciationSubjectNameBy(resetSubjectNameBy(card.getDepreciationSubjectId()));
        card.setDepreciationFeeSubjectNameBy(resetSubjectNameBy(card.getDepreciationFeeSubjectId()));
        return AjaxResult.success(card);
    }

    private String resetSubjectNameBy(Long subjectId){
        String str = "";
        BookSubject subject = iBookSubjectService.getById(subjectId);
        str = subject.getCode()+' '+subject.getFullName();
        return str;
    }

    /**
     * 新增固定资产卡片
     */
    @ApiOperation("新增固定资产卡片")
    @PreAuthorize("@ss.hasPermi('accounting:fixedAssetCard:add')" )
    @Log(title = "固定资产卡片" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody BookFixedAssetCard bookFixedAssetCard) {
        bookFixedAssetCard.setCreateTime(new Date());
        bookFixedAssetCard.setUpdateTime(new Date());
        return toAjax(iBookFixedAssetCardService.save(bookFixedAssetCard) ? 1 : 0);
    }

    /**
     * 修改固定资产卡片
     */
    @ApiOperation("修改固定资产卡片")
    @PreAuthorize("@ss.hasPermi('accounting:fixedAssetCard:edit')" )
    @Log(title = "固定资产卡片" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody BookFixedAssetCard bookFixedAssetCard) {
        bookFixedAssetCard.setUpdateTime(new Date());
        return toAjax(iBookFixedAssetCardService.updateById(bookFixedAssetCard) ? 1 : 0);
    }

    /**
     * 删除固定资产卡片
     */
    @PreAuthorize("@ss.hasPermi('accounting:fixedAssetCard:del')" )
    @Log(title = "固定资产卡片" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{cardIds}" )
    public AjaxResult remove(@PathVariable Long[] cardIds) {
        for (int i = 0; i < cardIds.length; i++) {
            BookFixedAssetCard card = new BookFixedAssetCard();
            card.setCardId(cardIds[i]);
            card.setDelFlag(StringDateUtil.Del_FLAG_2);
            card.setUpdateTime(new Date());
            iBookFixedAssetCardService.updateById(card);
        }
        return AjaxResult.success();
    }

    /**
     * 固定资产卡片-清理
     */
    //@PreAuthorize("@ss.hasPermi('accounting:fixedAssetCard:remove')" )
    @Log(title = "固定资产卡片" , businessType = BusinessType.DELETE)
    @PutMapping("/clean" )
    public AjaxResult clean(Long cardId,boolean status,Date yearPeriod) {
        BookFixedAssetCard card = new BookFixedAssetCard();
        card.setCardId(cardId);
        card.setStatus(status);
        card.setCleanPeriod(yearPeriod);
        card.setUpdateTime(new Date());
        iBookFixedAssetCardService.updateById(card);
        return AjaxResult.success();
    }

    /**
     * 测算计提折旧金额
     * @param bookId
     * @param yearPeriod
     * @return
     */
    @GetMapping("/getBalanceDepreciation")
    public AjaxResult getBalanceDepreciation(Long bookId, Date yearPeriod){
        return AjaxResult.success(fixedAssetFactory.getBalanceDepreciation(bookId, yearPeriod));
    }

    /**
     * 计提折旧结转
     * @param bookId
     * @param yearPeriod
     * @param carryoverId
     * @return
     */
    @PostMapping("/insertDepreciationVoucher")
    public AjaxResult insertDepreciationVoucher(Long bookId, Date yearPeriod,Long carryoverId){
       //fixedAssetFactory.insertDepreciationVoucher(bookId, yearPeriod,carryoverId);
        return AjaxResult.success();
    }
}
