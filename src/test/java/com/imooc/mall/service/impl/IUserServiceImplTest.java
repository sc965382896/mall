package com.imooc.mall.service.impl;

 import com.imooc.mall.MallApplicationTests;
import com.imooc.mall.enums.RoleEnum;
import com.imooc.mall.pojo.User;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class IUserServiceImplTest extends MallApplicationTests {

    @Autowired
    // 可以用接口声明，因为实现类唯一。
    private IUserServiceImpl iUserService;

    @Test
    public void register() {
        User user = new User("sc965382896", "admin1521", "sc965382896@163.com", RoleEnum.CUSTOMER.getCode());
        iUserService.register(user);
    }
}