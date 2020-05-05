package com.imooc.mall.service;

import com.github.pagehelper.PageInfo;
import com.imooc.mall.form.ShippingForm;
import com.imooc.mall.pojo.Shipping;
import com.imooc.mall.vo.ResponseVo;

import java.util.Map;

public interface IShippingService {

    ResponseVo<Map<String, Integer>> add(Integer uId, ShippingForm form);

    ResponseVo delete(Integer uId, Integer shippingId);

    ResponseVo update(Integer uId, Integer shippingId, ShippingForm form);

    ResponseVo<PageInfo<Shipping>> list(Integer uId, Integer pageNum, Integer pageSize);
}
