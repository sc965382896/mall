package com.imooc.mall.service.impl;

import com.imooc.mall.MallApplicationTests;
import com.imooc.mall.enums.ResponseEnum;
import com.imooc.mall.service.ICategoryService;
import com.imooc.mall.vo.CategoryVo;
import com.imooc.mall.vo.ResponseVo;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public class CategoryServiceImplTest extends MallApplicationTests {

    @Autowired
    private ICategoryService categoryService;

    @Test
    public void selectAll() {
        ResponseVo<List<CategoryVo>> responseVo = categoryService.selectAll();
        Assert.assertEquals(ResponseEnum.SUCCEESS.getCode(), responseVo.getStatus());
    }


}