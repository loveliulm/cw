package com.qifu.accounting.fixedAsset.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import com.qifu.framework.aspectj.lang.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.function.BiConsumer;

import com.qifu.framework.web.domain.BaseEntity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 固定资产卡片对象 book_fixed_asset_card
 *
 *  yangyang
 * 07-28
 */
@Data
@EqualsAndHashCode
@NoArgsConstructor
@Accessors(chain = true)
@TableName("book_fixed_asset_card")
public class BookFixedAssetCard implements Serializable {

private static final long serialVersionUID=1L;


    /** 卡片id */
    @TableId(value = "card_id", type = IdType.AUTO)
    private Long cardId;

    /** 账套id */
    private Long bookId;

    /** 卡片编码 */
    @Excel(name = "卡片编码")
    private String cardCode;

    /** 资产名称 */
    @Excel(name = "资产名称")
    @NotBlank(message = "资产名称不能为空")
    @Size(min = 0, max = 30, message = "资产名称长度不能超过30个字符")
    private String name;

    /** 类别id */
    @Excel(name = "类别id")
    private Long categoryId;

    /** 部门id*/
    private Long departmentId;

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

    /** 固定资产科目id */
    @Excel(name = "固定资产科目id")
    private Long fixedAssetSubjectId;

    /** 累计折旧科目id */
    @Excel(name = "累计折旧科目id")
    private Long depreciationSubjectId;


    /** 折旧费用科目id */
    @Excel(name = "折旧费用科目id")
    private Long depreciationFeeSubjectId;

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
    private Date createTime;

    /** 修改时间 */
    private Date updateTime;

    /** 删除字段0：正常2：删除 */
    private String delFlag;

    /** 备注 */
    @Excel(name = "备注")
    private String remark;

    /** 0：正常1：清理 */
    private Boolean status;

    /** 固定资产科目辅助核算id */
    private Long FixedSubjectAuxiliaryId;

    /** 累计折旧科目辅助核算id */
    private Long depreciationSubjectAuxiliaryId;

    /** 累计折旧科目辅助核算id */
    private Long depreciationFeeSubjectAuxiliaryId;

    @TableField(exist = false)
    private String fixedAssetSubjectNameBy;

    @TableField(exist = false)
    private String depreciationSubjectNameBy;

    @TableField(exist = false)
    private String depreciationFeeSubjectNameBy;

}
