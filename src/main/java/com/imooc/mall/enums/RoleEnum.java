package com.imooc.mall.enums;

import lombok.Getter;

@Getter
public enum RoleEnum {
    ADMIN(0),

    CUSTOMER(1);

    Integer code;
    private RoleEnum(Integer code) {
        this.code = code;
    }
}
