package com.qifu.accounting.fixedAsset.domain.vo;

import com.qifu.accounting.common.enums.BalanceDirectionEnum;
import com.qifu.framework.aspectj.lang.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;

/**
 *  yangyang
 * @date 2020/8/13 15:53
 */
@Data
public class DepreciationSubjectVo {
    /** 摘要 */
    private String summary;

    /** 借方金额 */
    private BigDecimal debit;

    /** 贷方金额 */
    private BigDecimal credit;

    private Long subjectId;

    private Long auxiliaryId;

    /** 余额方向：关键字 */
    private BalanceDirectionEnum balanceDirection;

    /** 余额方向：借贷 */
    private BalanceDirectionEnum balanceDirectionText;
}
