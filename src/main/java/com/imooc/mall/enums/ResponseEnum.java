package com.imooc.mall.enums;

import lombok.Getter;

@Getter
public enum ResponseEnum {

    ERROR(-1, "服务端错误"),

    SUCCEESS(0, "成功"),

    PASSWORD_ERROR(1, "密码错误"),

    USER_EXIST(2, "用户已存在"),

    PARAM_ERROR(3, "参数错误"),

    USERNAME_EXIST(4, "用户名已存在"),

    EMAIL_EXIST(5, "邮箱已存在"),

    SHIPPING_ERROR(6, "地址改动错误"),

    SHIPPING_NOT_EXIST(7, "地址不存在"),

    NEED_LOGIN(10, "用户未登录，请先登录"),

    USERNAME_OR_PASSWORD_ERROR(11, "用户名或密码错误"),

    PRODUCT_OFF_SLAE_OR_DELETE(12, "该商品已下架或删除"),

    PRODUCT_NOT_EXIST(13, "商品不存在"),

    PRODUCT_STOCK_ERROR(14, "库存不正确"),

    CART_PRODUCT_NOT_EXIST(15, "购物车中无此商品"),

    CART_SELECTED_IS_EMPTY(16, "请选择商品后下单"),

    ;

    Integer code;

    String desc;

    ResponseEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
