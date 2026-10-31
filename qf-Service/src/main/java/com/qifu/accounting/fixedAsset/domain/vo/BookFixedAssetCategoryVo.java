package com.qifu.accounting.fixedAsset.domain.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.qifu.framework.aspectj.lang.annotation.Excel;
import lombok.Data;

import java.util.Date;

/**
 *  yangyang
 * @date 2020/7/27 14:14
 */
@Data
public class BookFixedAssetCategoryVo {
    /** 固定资产类别id */
    @TableId(value = "category_id", type = IdType.AUTO)
    private Long categoryId;

    /** 账套id */
    @Excel(name = "账套id")
    private Long bookId;

    /** 编码 */
    @Excel(name = "编码")
    private String code;

    /** 折旧方法 */
    @Excel(name = "折旧方法")
    private Integer depreciationMethod;

    /** 类别名称 */
    @Excel(name = "类别名称")
    private String name;

    /** 预计使用年限 */
    @Excel(name = "预计使用年限")
    private Long years;

    /** 预计残值率 */
    @Excel(name = "预计残值率")
    private String residualRate;

    /** 固定资产科目code */
    @Excel(name = "固定资产科目code")
    private String fixedAssetSubjectCode;

    @Excel(name = "固定资产科目名称")
    private String fixedAssetSubjectName;

    /** 折旧科目code */
    @Excel(name = "折旧科目code")
    private String depreciationSubjectCode;

    @Excel(name = "折旧科目名称")
    private String depreciationSubjectName;

    /** 备注 */
    @Excel(name = "备注")
    private String remark;

    /** 创建时间 */
    private Date createTime;

}
