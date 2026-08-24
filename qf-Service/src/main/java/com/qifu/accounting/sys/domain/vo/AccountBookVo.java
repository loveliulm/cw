package com.qifu.accounting.sys.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.qifu.framework.aspectj.lang.annotation.Excel;
import lombok.Data;

import java.util.Date;

/**
 *  yangyang
 * @date 2020/9/25 14:47
 */
@Data
public class AccountBookVo {

    private Long bookId;

    /** 客户名称 */
    @Excel(name = "客户名称")
    private String customerName;

    /** 当前账期 */
    @Excel(name = "当前账期")
    @JsonFormat(pattern = "yyyy-MM")
    private Date currentPeriod;

    /*会计制度id*/
    private Long accountTemplateId;

    /*会计制度name*/
    private String accountTemplateName;

    /** 纳税性质 */
    @Excel(name = "纳税性质")
    private Integer vatType;

    /*是否是当前账套*/
    private Boolean isCurrentBook;
}
