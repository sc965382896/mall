package com.imooc.mall.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.imooc.mall.enums.ResponseEnum;
import lombok.Data;
import org.springframework.validation.BindingResult;

import java.util.Objects;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Data
public class ResponseVo<T> {

    private Integer status;

    private String msg;

    private T data;

    private ResponseVo(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    private ResponseVo(Integer status, T data) {
        this.status = status;
        this.data = data;
    }

    // 成功
    public static <T> ResponseVo<T> successWithMsg(String msg) {
        return new ResponseVo<>(ResponseEnum.SUCCEESS.getCode(), msg);
    }

    // 简化成功入参
    public static <T> ResponseVo<T> success() {
        return new ResponseVo<>(ResponseEnum.SUCCEESS.getCode(), ResponseEnum.SUCCEESS.getDesc());
    }

    // 返回成功的数据data
    public static <T> ResponseVo<T> success(T data) {
        return new ResponseVo<>(ResponseEnum.SUCCEESS.getCode(), data);
    }

    // 错误返回
    public static <T> ResponseVo<T> error(ResponseEnum responseEnum) {
        return new ResponseVo<>(responseEnum.getCode(), responseEnum.getDesc());
    }

    // 表单错误
    public static <T> ResponseVo<T> error(ResponseEnum responseEnum, BindingResult bindingResult) {
        return new ResponseVo<>(responseEnum.getCode(), Objects.requireNonNull(bindingResult.getFieldError()).getField() + " " + bindingResult.getFieldError().getDefaultMessage());
    }

    // 表单错误
    public static <T> ResponseVo<T> error(ResponseEnum responseEnum, String msg) {
        return new ResponseVo<>(responseEnum.getCode(), msg);
    }

}
