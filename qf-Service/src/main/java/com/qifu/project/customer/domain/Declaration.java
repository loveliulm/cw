package com.qifu.project.customer.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.qifu.framework.aspectj.lang.annotation.Excel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 报税对象 t_declaration
 * 
 *  YM
 * 09-08
 */
@Data
@EqualsAndHashCode
@NoArgsConstructor
@Accessors(chain = true)
@TableName("t_declaration")
public class Declaration implements Serializable {

private static final long serialVersionUID=1L;


    /** id */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 客户id */
    @Excel(name = "客户id")
    private Long customerId;

    /** 增值税 */
    @Excel(name = "增值税")
    private BigDecimal taxVatChange;

    /** 城市维护建设费 */
    @Excel(name = "城市维护建设费")
    private BigDecimal taxBuilding;

    /** 教育附加税 */
    @Excel(name = "教育附加税")
    private BigDecimal taxEducation;

    /** 企业所得税 */
    @Excel(name = "企业所得税")
    private BigDecimal taxCorporate;

    /** 个人所得税 */
    @Excel(name = "个人所得税")
    private BigDecimal taxPersonal;

    /** 印花税 */
    @Excel(name = "印花税")
    private BigDecimal taxStamp;

    /** 其他 */
    @Excel(name = "其他")
    private BigDecimal taxOther;

    /** 合计 */
    @Excel(name = "合计")
    private BigDecimal total;

    /** 年 */
    @Excel(name = "年")
    private Integer year;

    /** 月 */
    @Excel(name = "月")
    private Integer month;

    /** 创建时间 */
    private Date createTime;

    /** 修改时间 */
    private Date updateTime;

    /** 创建人id */
    private Long createBy;

    /** 修改人id */
    private Long updateBy;

    /** 部门 */
    private Long deptId;

    /**删除标志（0代表存在 2代表删除）*/
    private String isDel;
}
