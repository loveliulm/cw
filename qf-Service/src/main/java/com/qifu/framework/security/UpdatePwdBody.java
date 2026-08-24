package com.qifu.framework.security;

import io.swagger.annotations.ApiModelProperty;

/**
 * 修改密码对象
 */
public class UpdatePwdBody {

    /**
     * 用户手机号
     */
    @ApiModelProperty("用户手机号")
    private String phonenumber;

    /**
     * 验证码
     */
    @ApiModelProperty("验证码")
    private String phonecode;

    /**
     * 用户密码
     */
    @ApiModelProperty("用户密码")
    private String password;

    /**
     * 重复用户密码
     */
    @ApiModelProperty("重复用户密码")
    private String repeatpassword;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRepeatpassword() {
        return repeatpassword;
    }

    public void setRepeatpassword(String repeatpassword) {
        this.repeatpassword = repeatpassword;
    }
}
