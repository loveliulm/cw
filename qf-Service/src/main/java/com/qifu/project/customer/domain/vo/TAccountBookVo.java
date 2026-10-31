package com.qifu.project.customer.domain.vo;

import com.qifu.framework.aspectj.lang.annotation.Excel;
import com.qifu.project.customer.domain.TAccountBook;
import lombok.Data;

/**
 *  yangyang
 * @date 2020/6/30 11:32
 */
@Data
public class TAccountBookVo extends TAccountBook {
    /** 会计主管 */
    @Excel(name = "会计主管")
    private String managerAccounting;

    /** 记账会计 */
    @Excel(name = "记账会计")
    private String bookkeepingAccounting;

    /** 会计助理 */
    @Excel(name = "会计助理")
    private String assistantAccounting;
}
