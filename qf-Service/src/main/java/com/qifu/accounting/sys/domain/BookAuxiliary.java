package com.qifu.accounting.sys.domain;

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
import java.math.BigDecimal;
import java.util.Date;

/**
 * 辅组核算对象 book_auxiliary
 *
 *  YM
 * 07-09
 */
@Data
@EqualsAndHashCode
@NoArgsConstructor
@Accessors(chain = true)
@TableName("book_auxiliary")
public class BookAuxiliary implements Serializable {

private static final long serialVersionUID=1L;


    /** 辅组核算id */
    @TableId(value = "auxiliary_id", type = IdType.AUTO)
    private Long auxiliaryId;

    /** 账套id */
    @Excel(name = "账套id")
    private Long bookId;

    /** 编号 */
    @Excel(name = "编号")
    private String code;

    /** 名称 */
    @NotBlank(message = "名称不能为空")
    @Size(min = 0, max = 30, message = "名称长度不能超过30个字符")
    @Excel(name = "名称")
    private String name;

    /** 联系电话 */
    @Excel(name = "联系电话")
    private String phoneNumber;

    /** 地址 */
    @Excel(name = "地址")
    private String address;

    /** 备注 */
    @Excel(name = "备注")
    private String remark;

    /** 辅组核算类型id */
    @Excel(name = "辅组核算类型id")
    private Long auxiliaryTypeId;

    /** 删除标志：'0'：正常 '2'：已删除 */
    private String delFlag;

    /*创建时间*/
    private Date createTime;

    /*创建人id*/
    private Long createUserId;

}
