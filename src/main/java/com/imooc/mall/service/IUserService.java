package com.imooc.mall.service;

import com.imooc.mall.pojo.User;
import com.imooc.mall.vo.ResponseVo;

public interface IUserService {

    /**
     * 注册
     * @param user
     * @return
     */
    public ResponseVo<User> register(User user);

    /**
     * 登录
     * @param username
     * @param password
     */
    public ResponseVo<User> login(String username, String password);
}
