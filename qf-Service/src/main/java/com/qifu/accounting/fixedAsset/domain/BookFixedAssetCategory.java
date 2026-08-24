package com.qifu.accounting.fixedAsset.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import com.qifu.framework.aspectj.lang.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 * 固定资产类别对象 book_fixed_asset_categroy
 *
 *  YM
 * 07-27
 */
@Data
@EqualsAndHashCode
@NoArgsConstructor
@Accessors(chain = true)
@TableName("book_fixed_asset_category")
public class BookFixedAssetCategory implements Serializable {

private static final long serialVersionUID=1L;


    /** 固定资产类别id */
    @TableId(value = "category_id", type = IdType.AUTO)
    private Long categoryId;

    /** 账套id */
    @Excel(name = "账套id")
    private Long bookId;

    /** 编码 */
    @Excel(name = "编码")
    private String code;

    /** 类别名称 */
    @Excel(name = "类别名称")
    @NotBlank(message = "类别名称不能为空")
    @Size(min = 0, max = 30, message = "类别名称长度不能超过30个字符")
    private String name;

    /** 预计使用年限 */
    @Excel(name = "预计使用年限")
    private Long years;


    private String delFlag;

    /** 备注 */
    @Excel(name = "备注")
    private String remark;

    /** 创建时间 */
    private Date createTime;

    /** 修改时间 */
    private Date updateTime;
}
