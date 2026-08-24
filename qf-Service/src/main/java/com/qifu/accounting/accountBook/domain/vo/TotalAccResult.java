package com.qifu.accounting.accountBook.domain.vo;


import com.baomidou.mybatisplus.annotation.TableName;
import com.qifu.accounting.accountBook.domain.TotalAccount;
import com.qifu.framework.aspectj.lang.annotation.Excel;
import com.qifu.framework.aspectj.lang.annotation.Excels;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 总账结果返回
 */
@Getter
@Setter
public class TotalAccResult {
    private Long subjectId;

    /**
     * 科目编码
     */
    @Excel(name = "科目编码")
    private String subjectCode;

    /**
     * 科目名称
     */
    @Excel(name = "科目名称")
    private String subjectName;

    /**
     * 科目内容
     */
    @Excels({
            @Excel(name = "账期", targetAttr = "yearPeriod", type = Excel.Type.EXPORT),
            @Excel(name = "摘要", targetAttr = "summary", type = Excel.Type.EXPORT),
            @Excel(name = "借方", targetAttr = "debit", type = Excel.Type.EXPORT),
            @Excel(name = "贷方", targetAttr = "credit", type = Excel.Type.EXPORT),
            @Excel(name = "方向", targetAttr = "balanceDirectionText", type = Excel.Type.EXPORT),
            @Excel(name = "余额", targetAttr = "balance", type = Excel.Type.EXPORT),
    })
    private List<TotalAccount> details;
}
