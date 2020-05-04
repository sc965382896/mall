package com.imooc.mall.enums;

import lombok.Getter;

/**
 * 1-在售，2-下架，3-删除
 */
@Getter
public enum ProductStatusEnum {

    ON_SALE(1),

    OFF_SALE(2),

    DELETE(3),
    ;

    private Integer code;

    ProductStatusEnum(Integer code) {
        this.code = code;
    }

}
