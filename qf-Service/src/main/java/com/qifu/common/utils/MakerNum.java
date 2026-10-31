package com.qifu.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 创建编号规则工具类
 *
 */
public class MakerNum {

    public static String makerNumRule(String prefix, String maxNumNo){
        String numNo = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String uid_pfix = prefix + format.format(new Date());
        if (maxNumNo !=null){
            String uid_end = maxNumNo.substring(prefix.length() + 8, prefix.length() + 12);
            int endNum = Integer.parseInt(uid_end);
            int tmpNum = 10000 + endNum + 1;
            //去除首位1
            numNo = uid_pfix + subStr("" + tmpNum, 1);
        } else {
            numNo = uid_pfix + "0001";
        }

        return numNo;
    }

    public static String subStr(String str, int start){
        if (str == null || str.equals("") || str.length() == 0){
            return "";
        }
        if (start < str.length()) {
            return str.substring(start);
        } else {
            return "";
        }
    }


    /**
     * 4位：0001，0002，0003，
     * @param code
     * @return
     */
    public static String makerNumRuleLength4(String code){
        String numNo = null;
        if (code !=null){
            int number = Integer.parseInt(code);
            int tmpNum = 10000 + number + 1;
            //去除首位1
            numNo = subStr("" + tmpNum, 1);
        } else {
            numNo = "0001";
        }
        return numNo;
    }

    /**
     * 3位：001，002，003，
     * @param code
     * @return
     */
    public static String makerNumRuleLength3(String code){
        String numNo = null;
        if (code !=null){
            int number = Integer.parseInt(code);
            int tmpNum = 1000 + number + 1;
            //去除首位1
            numNo = subStr("" + tmpNum, 1);
        } else {
            numNo = "001";
        }
        return numNo;
    }
}
