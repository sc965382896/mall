package com.imooc.mall.controller;

import com.imooc.mall.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {

    @PostMapping("/register")
    public ResponseVo register(@RequestParam String username) {
        log.info("username={}", username);
        return ResponseVo.success("注册成功");
    }
}
