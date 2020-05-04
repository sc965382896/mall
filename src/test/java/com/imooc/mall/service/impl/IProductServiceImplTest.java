package com.imooc.mall.service.impl;

import com.github.pagehelper.PageInfo;
import com.imooc.mall.MallApplicationTests;
import com.imooc.mall.enums.ResponseEnum;
import com.imooc.mall.vo.ProductDetailVo;
import com.imooc.mall.vo.ProductVo;
import com.imooc.mall.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class IProductServiceImplTest extends MallApplicationTests {

    @Autowired
    private IProductServiceImpl productService;

    @Test
    public void list() {
        ResponseVo<PageInfo<ProductVo>> responseVo = productService.list(100012, 1, 1);
        Assert.assertEquals(ResponseEnum.SUCCEESS.getCode(), responseVo.getStatus());
    }

    @Test
    public void detail() {
        ResponseVo<ProductDetailVo> responseVo = productService.detail(100012);
        Assert.assertEquals(ResponseEnum.SUCCEESS.getCode(), responseVo.getStatus());
    }
}