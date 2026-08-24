package com.qifu.project.customer.domain;

import com.qifu.project.common.enums.CollectionStatusEnum;
import com.qifu.project.common.enums.PayMentEnum;
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
 * 收款明细对象 t_collection_record
 *
 *  YM
 * 06-28
 */
@Data
@EqualsAndHashCode
@NoArgsConstructor
@Accessors(chain = true)
@TableName("t_collection_record")
public class TCollectionRecord implements Serializable {

private static final long serialVersionUID=1L;


    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 收款总表id */
    @Excel(name = "收款总表id")
    private Long collectionId;

    /** 当前收款金额 */
    @Excel(name = "当前收款金额")
    private BigDecimal collectionAmount;

    /** 当前收款日期 */
    @Excel(name = "当前收款日期" , width = 30, dateFormat = "yyyy-MM-dd")
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date collectionDate;

    /** 开始时间 */
    @Excel(name = "开始时间" , width = 30, dateFormat = "yyyy-MM-dd")
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startDate;

    /** 结束时间 */
    @Excel(name = "结束时间" , width = 30, dateFormat = "yyyy-MM-dd")
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endDate;

    /** 付款方式 '银行卡','微信','支付宝','现金'*/
    @Excel(name = "付款方式")
    private PayMentEnum payment;

    /** 是否收款 '未收款','已收款'*/
    @Excel(name = "是否收款")
    private CollectionStatusEnum status;

    private Date createTime;

    private Date updateTime;

    /** 创建人id */
    @Excel(name = "创建人id")
    private Long createId;

    /** 1：代账服务收款2：工商服务收款 */
    @Excel(name = "1：代账服务收款2：工商服务收款")
    private Long serviceClassId;

    /**备注*/
    private String remark;
}
