package com.imooc.mall.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserRegisterForm {

    // NotBlank: 字符串不能只有空格,
    // NotEmpty：集合不能为空，
    // NotNull：对象不能是null。

    @NotBlank()
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String email;
}
