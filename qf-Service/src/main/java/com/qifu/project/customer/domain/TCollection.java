package com.qifu.project.customer.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.qifu.project.common.enums.IsWarningEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import com.qifu.framework.aspectj.lang.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 收款对象 t_collection
 *
 *  YM
 * 06-28
 */
@Data
@EqualsAndHashCode
@NoArgsConstructor
@Accessors(chain = true)
@TableName("t_collection")
public class TCollection implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 收款id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long deptId;

    /**
     * 合同id
     */
    @Excel(name = "合同id", type = Excel.Type.IMPORT)
    private Long contractId;

    /**
     * 合同编号
     */
    @Excel(name = "合同编号")
    @TableField(exist = false)
    private String contractNo;

    /**
     * 客户名称
     */
    @Excel(name = "客户名称")
    @TableField(exist = false)
    private String customerName;

    /**
     * 合同金额
     */
    @Excel(name = "合同金额")
    private BigDecimal total;

    /**
     * 累计已收金额
     */
    @Excel(name = "累计已收金额")
    private BigDecimal amount;

    /**
     * 欠款金额
     */
    @Excel(name = "欠款金额")
    private BigDecimal debt;

    /**
     * 是否完结，0：否 1：是
     */
    @Excel(name = "是否完结", readConverterExp = "0=否,1=是")
    private Integer isEnd;

    /**
     * 短信发送状态0：未发送1：已发送
     */
//    @Excel(name = "短信发送状态0：未发送1：已发送")
    private Integer isNotice;

    /**
     * 预警状态 '正常','即将到期','已经欠费'
     */
    @Excel(name = "预警状态")
    private IsWarningEnum isWarning;

    /** 1个月内属于即将到期 '已到期','即将到期','未到期'*/
//    @Excel(name = "1个月内属于即将到期")
//    private CollectionStateEnum state;

    /**
     * 最后收款日期
     */
    @Excel(name = "最后收款日期", width = 30, dateFormat = "yyyy-MM-dd")
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastPayDate;

    /**
     * $column.columnComment
     */
    private Date createTime;

    /**
     * 1：代账服务收款2：工商服务收款
     */
    @Excel(name = "收款类型", readConverterExp = "1=代账服务收款,2=工商服务收款")
    private Long serviceClassId;

    private String remark;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String isDel;
}
