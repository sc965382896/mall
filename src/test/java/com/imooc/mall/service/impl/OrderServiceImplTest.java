package com.imooc.mall.service.impl;

import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.imooc.mall.MallApplicationTests;
import com.imooc.mall.enums.ResponseEnum;
import com.imooc.mall.form.CartAddForm;
import com.imooc.mall.service.ICartService;
import com.imooc.mall.service.IOrderService;
import com.imooc.mall.vo.CartVo;
import com.imooc.mall.vo.OrderVo;
import com.imooc.mall.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
public class OrderServiceImplTest extends MallApplicationTests {

    @Autowired
    private IOrderService orderService;

    @Autowired
    private ICartService cartService;

    private final Integer uid = 6;

    private final Integer shippingId = 6;

    private final Integer productId = 26;

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Before
    public void add() {
        CartAddForm form = new CartAddForm();
        form.setProductId(productId);
        form.setSelected(true);
        ResponseVo<CartVo> responseVo = cartService.add(uid, form);
        Assert.assertEquals(ResponseEnum.SUCCEESS.getCode(), responseVo.getStatus());
    }

    @Test
    public void create() {
        ResponseVo<OrderVo> responseVo = orderService.create(uid, shippingId);
        log.info("result={}", responseVo);
        Assert.assertEquals(ResponseEnum.SUCCEESS.getCode(), responseVo.getStatus());
    }

    public ResponseVo<OrderVo> createOrderVo() {
        ResponseVo<OrderVo> responseVo = orderService.create(uid, shippingId);
        log.info("result={}", responseVo);
        Assert.assertEquals(ResponseEnum.SUCCEESS.getCode(), responseVo.getStatus());
        return responseVo;
    }

    @Test
    public void list() {
        ResponseVo<PageInfo<OrderVo>> responseVo = orderService.list(uid, 1, 10);
        log.info("result={}", responseVo);
        Assert.assertEquals(ResponseEnum.SUCCEESS.getCode(), responseVo.getStatus());
    }

    @Test
    public void detail() {
        ResponseVo<OrderVo> orderVoResponseVo = createOrderVo();
        ResponseVo<OrderVo> responseVo = orderService.detail(uid, orderVoResponseVo.getData().getOrderNo());
        log.info("result={}", responseVo);
        Assert.assertEquals(ResponseEnum.SUCCEESS.getCode(), responseVo.getStatus());
    }

    @Test
    public void cancel() {
        ResponseVo<OrderVo> orderVoResponseVo = createOrderVo();
        ResponseVo responseVo = orderService.cancel(uid, orderVoResponseVo.getData().getOrderNo());
        log.info("result={}", responseVo);
        Assert.assertEquals(ResponseEnum.SUCCEESS.getCode(), responseVo.getStatus());
    }
}