package com.qifu.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;

/**
 * 发送手机验证码工具类
 */
public class SmsUtil {

    // aliyun:
    //  accessKeyId: LTAI5tAVJDNiXwZDQGZAzfoo
    //  accessKeySecret: U1FbdbrtHJKjk8VU9icRlMzAJap26e
    //  sms:
    //    signature: 企万慧
    //    template-code:
    //      verification: SMS_270215208
    //      register: SMS_270195200
    //      login: SMS_270190207
    //      resetpwd: SMS_270275202

    public static void sendSmsCode(String mobile, String code, String templateCode) {
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou",
                "LTAI5tAVJDNiXwZDQGZAzfoo",
                "U1FbdbrtHJKjk8VU9icRlMzAJap26e");
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", mobile);
        request.putQueryParameter("SignName", "企万慧");
        request.putQueryParameter("TemplateCode", templateCode);
        JSONObject param = new JSONObject();
        param.put("code", code);
        request.putQueryParameter("TemplateParam", param.toJSONString());
        try {
            CommonResponse response = client.getCommonResponse(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
