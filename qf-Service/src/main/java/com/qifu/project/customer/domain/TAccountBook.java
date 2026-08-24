package com.qifu.project.customer.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.qifu.project.common.enums.ServerStatusEnum;
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
import java.util.Date;
import com.qifu.framework.web.domain.BaseEntity;

/**
 * 账套对象 t_account_book
 *
 *  YM
 * 06-30
 */
@Data
@EqualsAndHashCode
@NoArgsConstructor
//@Accessors(chain = true)
@TableName("t_account_book")
public class TAccountBook implements Serializable {

private static final long serialVersionUID=1L;


    /** 账套id */
    @TableId(value = "book_id", type = IdType.AUTO)
    private Long bookId;

    /** 客户id */
    @Excel(name = "客户id")
    private Long customerId;

    /** 客户名称 */
    @Excel(name = "客户名称")
    private String customerName;

    /*账套个数：0：未建账套，大于0：已建账套*/
    private Integer bookCount;

    /** 纳税性质 */
    @Excel(name = "纳税性质")
    private Integer vatType;

    /*会计主管id*/
    private Long managerAccountingUserId;

    /*记账会计id*/
    private Long bookkeepingAccountingUserId;

    /*会计助理id*/
    private Long assistantAccountingUserId;

    /** 状态  (0:表示正常 1：表示是客户的服务类型取消掉了代理记账)*/
    @Excel(name = "状态")
    private Integer status;

    /** 凭证数 */
    @Excel(name = "凭证数")
    private Integer voucherCount;

    /** 建账时间 */
    @Excel(name = "建账时间" , width = 30, dateFormat = "yyyy-MM")
    @JsonFormat(pattern = "yyyy-MM")
    private Date createPeriod;

    /** 当前账期 */
    @Excel(name = "当前账期")
    @JsonFormat(pattern = "yyyy-MM")
    private Date currentPeriod;

    /** 服务状态 */
    @Excel(name = "服务状态")
    private ServerStatusEnum serverStatus;

    /** 创建时间 */
    private Date createTime;

    /** 更新时间 */
    private Date updateTime;

    /** 更新时间 */
    private Long deptId;

    private String isDel;

    /*会计制度id*/
    private Long accountTemplateId;

    public TAccountBook(TCustomer customer){
        this.customerId = customer.getId();
        this.customerName = customer.getCustomerName();
        this.updateTime = new Date();
        this.assistantAccountingUserId = customer.getAssistantAccountingUserId();
        this.bookkeepingAccountingUserId = customer.getBookkeepingAccountingUserId();
        this.managerAccountingUserId = customer.getManagerAccountingUserId();
        this.vatType = customer.getVatType();
        this.deptId = customer.getDeptId();
    }
}
