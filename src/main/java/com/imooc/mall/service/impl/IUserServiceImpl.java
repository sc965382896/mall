package com.imooc.mall.service.impl;

import com.imooc.mall.dao.UserMapper;
import com.imooc.mall.pojo.User;
import com.imooc.mall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

@Service
public class IUserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;
    /**
     * 注册
     * @param user
     */
    @Override
    public void register(User user) {
        // username不能重复
        int countByUsername = userMapper.CountByUsername(user.getUsername());
        if (countByUsername > 0) {
            throw new RuntimeException("该用户已注册");
        }
        // email不能重复
        int countByEmail = userMapper.CountByEmail(user.getEmail());
        if (countByEmail > 0) {
            throw new RuntimeException("该用户已注册");
        }

        // 调用spring自带的MD5加密
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes(StandardCharsets.UTF_8)));

        // 写入数据库
        int resultCount = userMapper.insertSelective(user);
        if (resultCount == 0) {
            throw new RuntimeException("注册失败");
        }
    }
}
