package com.imooc.mall.service.impl;

 import com.imooc.mall.MallApplicationTests;
 import com.imooc.mall.enums.ResponseEnum;
 import com.imooc.mall.enums.RoleEnum;
import com.imooc.mall.pojo.User;
import com.imooc.mall.vo.ResponseVo;
 import org.junit.Assert;
 import org.junit.Before;
 import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class UserServiceImplTest extends MallApplicationTests {

    public static final String USERNAME = "lucy";

    public static final String PASSWORD = "12345678";

    public static final String EMAIL = "lucy12345678@163.com";

    @Autowired
    // 可以用接口声明，因为实现类唯一。
    private UserServiceImpl iUserService;

    @Before
    public void register() {
        User user = new User(USERNAME, PASSWORD, EMAIL, RoleEnum.CUSTOMER.getCode());
        ResponseVo<User> register = iUserService.register(user);
        Assert.assertEquals(ResponseEnum.SUCCEESS.getCode(), register.getStatus());
    }

    @Test
    public void login() {
        ResponseVo<User> success = iUserService.login(USERNAME, PASSWORD);
        Assert.assertEquals(ResponseEnum.SUCCEESS.getCode(), success.getStatus());
    }
}