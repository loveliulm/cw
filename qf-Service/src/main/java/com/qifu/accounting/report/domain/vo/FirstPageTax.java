package com.qifu.accounting.report.domain.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 *  yangyang
 * @date 2020/9/23 14:06
 */
@Data
public class FirstPageTax {
    /*增值税*/
    private BigDecimal taxVat = BigDecimal.ZERO;

    /*城市维护建设税*/
    private BigDecimal taxBuilding = BigDecimal.ZERO;

    /*教育附加税*/
    private BigDecimal taxEducation = BigDecimal.ZERO;

    /*企业所得税*/
    private BigDecimal taxCorporate = BigDecimal.ZERO;

    /*个人所得税*/
    private BigDecimal taxPersonal = BigDecimal.ZERO;


}
