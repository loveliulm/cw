package com.qifu.accounting.sys.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.qifu.accounting.common.enums.PeriodStatusEnum;
import com.qifu.common.utils.DateUtils;
import com.qifu.common.utils.SecurityUtils;
import io.swagger.models.auth.In;
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
 * 账期对象 book_period
 *
 *  YM
 * 07-17
 */
@Data
@EqualsAndHashCode
@Accessors(chain = true)
@TableName("book_period")
public class BookPeriod implements Serializable {

private static final long serialVersionUID=1L;

    /** 账套id */
    private Long bookId;

    private Integer year;

    private Integer month;

    /** 账期 */
    @JsonFormat(pattern = "yyyy-MM")
    private Date yearPeriod;

    /** 是否时当前账期1：是0：历史账期 */
    @Excel(name = "是否时当前账期1：是0：历史账期")
    private Boolean isCurrentPeriod;

    /** 0:正常 2:删除 */
    private String delFlag;

    /** 创建时间 */
    private Date createTime;

    /** 创建人id */
    @Excel(name = "创建人id")
    private Long createUserId;

    private PeriodStatusEnum status;

    public BookPeriod(){}

    public BookPeriod(Long bookId,Date yearPeriod,Boolean isCurrentPeriod,String delFlag){
        this.bookId = bookId;
        this.yearPeriod = yearPeriod;
        this.isCurrentPeriod = isCurrentPeriod;
        this.delFlag = delFlag;
        this.createTime = new Date();
        this.createUserId = SecurityUtils.userId();
        this.year = DateUtils.getYear(yearPeriod);
        this.month = DateUtils.getMonth(yearPeriod);
    }
}
