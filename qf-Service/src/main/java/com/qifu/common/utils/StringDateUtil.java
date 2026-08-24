package com.qifu.common.utils;

public class StringDateUtil {

    //删除标志（0代表存在 2代表删除）
    public static final String Del_FLAG_0 = "0";
    public static final String Del_FLAG_2 = "2";

    //状态标志 0正常 1停用
    public static final String STATUS_0 = "0";
    public static final String STATUS_1 = "1";

    //凭证审核状态
    public static final Integer VOUCHER_STATUS_0 = 0;
    public static final Integer VOUCHER_STATUS_1 = 1;

    //'0':顶级号 '1':公司 '2':部门
    public static final String  IS_COMPANY_0 = "0";
    public static final String  IS_COMPANY_1 = "1";
    public static final String  IS_COMPANY_2 = "2";

    //'0':代表默认角色，'1':代表自定义角色
    public static final String  IS_DEFAULT_0 = "0";
    public static final String  IS_DEFAULT_1 = "1";

    //
    public static final Integer ACCOUNTING_STATUS_0 = 0;
    public static final Integer ACCOUNTING_STATUS_1 = 1;

    //角色
    public static final String COMPANYADMIN = "companyAdmin";//公司管理员
    public static final String ACCOUNTMANGER = "accountManager";//主管会计
    public static final String ACCOUNTING = "accounting";//记账会计
    public static final String DRAWER = "drawer";//开票员
    public static final String TAXACCOUNTING = "taxAccounting";//报税会计
    public static final String ACCOUNTCLERK = "accountClerk";//会计助理
    public static final String CASHIER = "legwork";//工商外勤


    /*账套*/
    //type 1：balanceInit-期初金额,2:debit-本年累计借方,3:credit-本年累计贷方
    public static final Integer TYPE_1 = 1;
    public static final Integer TYPE_2 = 2;
    public static final Integer TYPE_3 = 3;

    //配置的key值
    public static final String FIXED_ASSET_TYPE = "固定资产配置";

    public static final String TAX_TYPE = "首页折线图";

    public static final String FINANCIAL = "首页财务指标";

    //工商服务任务状态
    public static final Integer TASK_TYPE_0 = 0;//未开始
    public static final Integer TASK_TYPE_1 = 1;//服务中
    public static final Integer TASK_TYPE_2 = 2;//已完成

    //客户电子发票开通状态
    /**开通*/
    public static final Integer OPEN_INVOICE_TYPE = 1;
    /**未开通*/
    public static final Integer CLOSE_INVOICE_TYPE = 0;

    //购买方类型
    /**个人*/
    public static final Integer BUYER_CUSTOMER_TYPE_0 = 0;
    /**公司*/
    public static final Integer BUYER_CUSTOMER_TYPE_1 = 1;

    public static final Integer COMMON_STATUS_0 = 0;
    public static final Integer COMMON_STATUS_1 = 1;
    public static final Integer COMMON_STATUS_2 = 2;
}
