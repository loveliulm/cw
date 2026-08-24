package com.qifu.accounting.accountBook.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.qifu.accounting.common.enums.BalanceDirectionEnum;
import com.qifu.framework.aspectj.lang.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 *  yangyang
 * @date 2020/7/20 15:39
 */
@Data
public class SubjectVo {

    /*科目id*/
    private Long subjectId;

    /*科目编码*/
    private String subjectCode;

    /*科目编码*/
    private String subjectName;


    private String subjectNameBy;

}
