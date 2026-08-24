package com.qifu.framework.security;

import io.swagger.annotations.ApiModelProperty;

/**
 * 用户注册对象
 */
public class RegisterBody {
    /**
     * 公司名称
     */
    @ApiModelProperty("公司名称")
    private String deptname;

    /**
     * 用户名
     */
    @ApiModelProperty("用户名")
    private String username;

    /**
     * 用户密码
     */
    @ApiModelProperty("用户密码")
    private String password;

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

    public String getDeptname() {
        return deptname;
    }

    public void setDeptname(String deptname) {
        this.deptname = deptname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

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
