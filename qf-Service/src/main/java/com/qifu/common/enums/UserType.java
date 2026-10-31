package com.qifu.common.enums;

import lombok.Getter;

@Getter
public enum UserType {
    DZ_COMPANY(1,"代账公司"),
    COMPANY_CLERK(2,"公司职员");

    private int code;
    private String value;

    UserType(int code, String value){
        this.code = code;
        this.value = value;
    }
}
