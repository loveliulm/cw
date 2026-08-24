package com.qifu.accounting.sys.domain.vo;

import lombok.Data;

import java.util.Date;

/**
 *  yangyang
 * @date 2020/8/13 14:10
 */
@Data
public class BookConfigVo {
    private Long configId;

    /** 账套id */
    private Long bookId;

    /** 配置类型 */
    private String type;

    /** key */
    private String key;

    /** value */
    private Long subjectId;

    /** 备注 */
    private String remark;

    /** 创建时间 */
    private Date createTime;

    private String subjectBy;
}
