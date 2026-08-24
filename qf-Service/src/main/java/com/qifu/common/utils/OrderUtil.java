package com.qifu.common.utils;

public class OrderUtil {

    /**未请求*/
    public static final Integer STATUS_0 = 0;
    /**已开具*/
    public static final Integer STATUS_1 = 1;
    /**已红冲*/
    public static final Integer STATUS_2 = 2;
    /**已作废*/
    public static final Integer STATUS_3 = 3;
    /**失败(开具/红冲/作废)*/
    public static final Integer STATUS_11 = 11;

    //是否打印清单 1 打印清单 0 不打印清单
    /**打印清单*/
    public static final Integer IS_PRINT = 1;
    /**不打印清单*/
    public static final Integer NO_PRINT = 0;

    //是否自动拆分？（1、拆分；0、不拆分）
    /**拆分*/
    public static final Integer AUTOMATIC_SPLIT = 1;
    /**不拆分*/
    public static final Integer NO_SPLIT = 0;

    //发票种类（01、专用发票(纸质)；02、普通发票（纸质）；12、普通发票（电子）
    /**专用发票(纸质)*/
    public static final String SPECICAL_INVOICE = "01";
    /**普通发票（纸质）*/
    public static final String COMMON_INVOICE = "02";
    /**普通发票（电子）*/
    public static final String ELECTRONIC_INVOICE = "12";

    //待开票交易信息中的金额是否含税？（1、含税；0、不含税）
    /**含税*/
    public static final Integer TAX_INCLUSIVE = 1;
    /**不含税*/
    public static final Integer TAX_EXCLUSIVE = 0;

    //征税方式
    /**普通征税*/
    public static final Integer CHARGE_TAX_WAY_PT = 0;
    /**减按征税*/
    public static final Integer CHARGE_TAX_WAY_JA = 1;
    /**差额征税*/
    public static final Integer CHARGE_TAX_WAY_CE = 2;

    //发票状态
    /**待开具*/
    public static final String INVOICE_STATUS_DN = "0";
    /**发票开具*/
    public static final String INVOICE_STATUS_KJ = "11";
    /**发票红冲*/
    public static final String INVOICE_STATUS_HC = "12";
    /**发票作废*/
    public static final String INVOICE_STATUS_ZF = "14";
}
