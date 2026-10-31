package com.qifu.accounting.fixedAsset.domain.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.qifu.framework.aspectj.lang.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 *  yangyang
 * @date 2020/7/28 15:01
 */
@Data
public class BookFixedAssetCardVo {
    /** 卡片id */
    @TableId(value = "card_id", type = IdType.AUTO)
    private Long cardId;

    /** 卡片编码 */
    @Excel(name = "卡片编码")
    private String cardCode;

    /** 资产名称 */
    @Excel(name = "资产名称")
    private String name;

    /** 类别 */
    @Excel(name = "类别")
    private String categoryName;

    /** 账期 */
    @Excel(name = "账期" , width = 30, dateFormat = "yyyy-MM")
    @JsonFormat(pattern = "yyyy-MM")
    private Date yearPeriod;

    /** 开始使用日期 */
    @Excel(name = "开始使用日期" , width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    /** 清理期间 */
    @Excel(name = "清理期间" , width = 30, dateFormat = "yyyy-MM")
    @JsonFormat(pattern = "yyyy-MM")
    private Date cleanPeriod;

    /** 折旧方法 */
    @Excel(name = "折旧方法")
    private Integer depreciationMethod;

    /** 预计使用期限 */
    @Excel(name = "预计使用期限")
    private Long term;

    /** 原值 */
    @Excel(name = "原值")
    private BigDecimal originalValue;

    /** 残值率 */
    @Excel(name = "残值率")
    private BigDecimal residualRate;

    /** 预计残值率 */
    @Excel(name = "预计残值率")
    private BigDecimal residualRatePre;

    /** 减值准备 */
    @Excel(name = "减值准备")
    private BigDecimal impairmentPrepare;

    /** 已折旧期间 */
    @Excel(name = "已折旧期间")
    private Long alreadyDepreciationDate;

    /** 期初累计折旧 */
    @Excel(name = "期初累计折旧")
    private BigDecimal startDepreciation;

    /** 期初净值 */
    @Excel(name = "期初净值")
    private BigDecimal startNetValue;

    /** 月折旧 */
    @Excel(name = "月折旧")
    private BigDecimal monthDepreciation;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 0：正常1：清理 */
    private Boolean status;
}
