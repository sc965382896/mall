package com.imooc.mall.controller;

import com.imooc.mall.enums.ResponseEnum;
import com.imooc.mall.form.UserLoginForm;
import com.imooc.mall.form.UserRegisterForm;
import com.imooc.mall.pojo.User;
import com.imooc.mall.service.IUserService;
import com.imooc.mall.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Objects;

import static com.imooc.mall.enums.MallConst.CURRENT_USER;

@RestController
@Slf4j
public class UserController {

    @Autowired IUserService iUserService;

    @PostMapping("/user/register")
    public ResponseVo<User> register(@Valid @RequestBody UserRegisterForm userRegisterForm, BindingResult bindingResult) {

        // 判断是否有数据为空，使得数据提交错误。
        if (bindingResult.hasErrors()) {
            log.error("注册提交参数有误，{} {}", Objects.requireNonNull(bindingResult.getFieldError()).getField(), bindingResult.getFieldError().getDefaultMessage());
            return ResponseVo.error(ResponseEnum.ERROR, bindingResult);
        }

        // 参数无误则调用注册服务，完成数据写入。
        User user = new User();
        BeanUtils.copyProperties(userRegisterForm, user);
        return iUserService.register(user);
    }

    @PostMapping("/user/login")
    public ResponseVo<User> login(@Valid @RequestBody UserLoginForm userLoginForm,
                                  BindingResult bindingResult,
                                  HttpSession session) {
        // 判断是否有数据为空，使得数据提交错误。
        if (bindingResult.hasErrors()) {
            log.error("登录提交参数有误，{} {}", Objects.requireNonNull(bindingResult.getFieldError()).getField(), bindingResult.getFieldError().getDefaultMessage());
            return ResponseVo.error(ResponseEnum.ERROR, bindingResult);
        }
        ResponseVo<User> userResponseVo = iUserService.login(userLoginForm.getUsername(), userLoginForm.getPassword());

        // 设置Session
        session.setAttribute(CURRENT_USER, userResponseVo.getData());

        return userResponseVo;
    }

    @GetMapping("/user")
    public ResponseVo<User> UserInfo(HttpSession session) {
        log.info("/user session={}", session.getId());
        User user = (User)session.getAttribute(CURRENT_USER);
        return ResponseVo.success(user);
    }

    //TODO 判断登录状态，拦截器
    @PostMapping("/user/logout")
    public ResponseVo<User> logout(HttpSession session) {
        log.info("/user/logout session={}", session.getId());
        session.removeAttribute(CURRENT_USER);
        return ResponseVo.successWithMsg("退出成功");
    }
}
