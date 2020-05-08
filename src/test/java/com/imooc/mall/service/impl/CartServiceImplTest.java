package com.imooc.mall.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.imooc.mall.MallApplicationTests;
import com.imooc.mall.enums.ResponseEnum;
import com.imooc.mall.form.CartAddForm;
import com.imooc.mall.form.CartUpdateForm;
import com.imooc.mall.service.ICartService;
import com.imooc.mall.vo.CartVo;
import com.imooc.mall.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class CartServiceImplTest extends MallApplicationTests {

    @Autowired
    private ICartService cartService;

    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private Integer productId = 26;

    private Integer uid = 6;

    @Before
    public void add() {
        CartAddForm form = new CartAddForm();
        form.setProductId(productId);
        form.setSelected(true);
        ResponseVo<CartVo> responseVo = cartService.add(uid, form);
        log.info("cartAddResult={}", gson.toJson(responseVo));
        Assert.assertEquals(ResponseEnum.SUCCEESS.getCode(), responseVo.getStatus());
    }

    @Test
    public void list() {
        ResponseVo<CartVo> responseVo = cartService.list(uid);
        log.info("cartListResult={}", gson.toJson(responseVo));
        Assert.assertEquals(ResponseEnum.SUCCEESS.getCode(), responseVo.getStatus());
    }

    @Test
    public void update() {
        CartUpdateForm form  = new CartUpdateForm();
        form.setQuantity(10);
        form.setSelected(false);
        ResponseVo<CartVo> responseVo = cartService.update(uid, productId, form);
        log.info("cartUpdateResult={}", gson.toJson(responseVo));
        Assert.assertEquals(ResponseEnum.SUCCEESS.getCode(), responseVo.getStatus());
    }

    @Test
    public void selectAll() {
        ResponseVo<CartVo> responseVo = cartService.selectAll(uid);
        log.info("cartSelectAllResult={}", gson.toJson(responseVo));
        Assert.assertEquals(ResponseEnum.SUCCEESS.getCode(), responseVo.getStatus());
    }

    @Test
    public void unSelectedAll() {
        ResponseVo<CartVo> responseVo = cartService.unSelectedAll(uid);
        log.info("cartUnselectedAllResult={}", gson.toJson(responseVo));
        Assert.assertEquals(ResponseEnum.SUCCEESS.getCode(), responseVo.getStatus());
    }

    @Test
    public void sum() {
        ResponseVo<Integer> responseVo = cartService.sum(uid);
        log.info("cartSumResult={}", gson.toJson(responseVo));
        Assert.assertEquals(ResponseEnum.SUCCEESS.getCode(), responseVo.getStatus());
    }

    @After
    public void delete() {
        ResponseVo<CartVo> responseVo = cartService.delete(uid, productId);
        log.info("cartDeleteResult={}", gson.toJson(responseVo));
        Assert.assertEquals(ResponseEnum.SUCCEESS.getCode(), responseVo.getStatus());
    }
}