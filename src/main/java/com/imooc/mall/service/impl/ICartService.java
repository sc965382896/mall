package com.imooc.mall.service.impl;

import com.imooc.mall.form.CartAddForm;
import com.imooc.mall.vo.CartVo;
import com.imooc.mall.vo.ResponseVo;

public interface ICartService {

    ResponseVo<CartVo> add(Integer uId, CartAddForm form);
}
