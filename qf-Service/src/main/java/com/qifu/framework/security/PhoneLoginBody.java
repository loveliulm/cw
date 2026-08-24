package com.qifu.framework.security;

/**
 * 手机号登录登录对象
 */
public class PhoneLoginBody {
    /**
     * 手机号
     */
    private String phonenumber;

    /**
     * 验证码
     */
    private String phonecode;

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getPhonecode() {
        return phonecode;
    }

    public void setPhonecode(String phonecode) {
        this.phonecode = phonecode;
    }
}
