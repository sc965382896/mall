package com.imooc.mall.service;

import com.imooc.mall.form.CartAddForm;
import com.imooc.mall.form.CartUpdateForm;
import com.imooc.mall.vo.CartVo;
import com.imooc.mall.vo.ResponseVo;

public interface ICartService {

    ResponseVo<CartVo> list(Integer uId);

    ResponseVo<CartVo> add(Integer uId, CartAddForm form);

    ResponseVo<CartVo> update(Integer uId, Integer productId, CartUpdateForm cartUpdateForm);

    ResponseVo<CartVo> delete(Integer uId, Integer productId);

    ResponseVo<CartVo> selectAll(Integer uId);

    ResponseVo<CartVo> unSelectedAll(Integer uId);

    ResponseVo<Integer> sum(Integer uId);
}
