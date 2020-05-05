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
public class ICartServiceImplTest extends MallApplicationTests {

    @Autowired
    private ICartService iCartService;

    private Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private Integer productId = 26;
    private Integer uId = 26;

    @Before
    public void add() {
        CartAddForm cartAddForm = new CartAddForm();
        cartAddForm.setProductId(productId);
        cartAddForm.setSelected(true);
        ResponseVo<CartVo> responseVo = iCartService.add(uId, cartAddForm);
        log.info("list={}", gson.toJson(responseVo));
        log.info("list={}", gson.toJson(responseVo));

    }

    @Test
    public void list() {
        ResponseVo<CartVo> responseVo = iCartService.list(uId);
        log.info("list={}", gson.toJson(responseVo));
        log.info("list={}", gson.toJson(responseVo));
    }

    @Test
    public void update() {
        CartUpdateForm cartUpdateForm  = new CartUpdateForm();
        cartUpdateForm.setQuantity(10);
        cartUpdateForm.setSelected(false);
        ResponseVo<CartVo> responseVo = iCartService.update(uId, productId, cartUpdateForm);
        log.info("list={}", gson.toJson(responseVo));
    }

    @Test
    public void selectAll() {
        ResponseVo<CartVo> responseVo = iCartService.selectAll(uId);
        log.info("list={}", gson.toJson(responseVo));
        log.info("list={}", gson.toJson(responseVo));
    }

    @Test
    public void unSelectedAll() {
        ResponseVo<CartVo> responseVo = iCartService.unSelectedAll(uId);
        log.info("list={}", gson.toJson(responseVo));
        Assert.assertEquals(ResponseEnum.SUCCEESS.getCode(), responseVo.getStatus());
    }

    @Test
    public void sum() {
        ResponseVo<Integer> responseVo = iCartService.sum(uId);
        log.info("list={}", gson.toJson(responseVo));
        Assert.assertEquals(ResponseEnum.SUCCEESS.getCode(), responseVo.getStatus());
    }

    @After
    public void delete() {
        ResponseVo<CartVo> responseVo = iCartService.delete(uId, productId);
        log.info("list={}", gson.toJson(responseVo));
        Assert.assertEquals(ResponseEnum.SUCCEESS.getCode(), responseVo.getStatus());
    }
}