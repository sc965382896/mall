package com.imooc.mall.service;

import com.imooc.mall.vo.ProductVo;
import com.imooc.mall.vo.ResponseVo;

import java.util.List;

public interface IProductService {

    /**
     * 根据类目id查询商品
     * @param categoryId
     * @param pageNum
     * @param pageSize
     * @return
     */
    ResponseVo<List<ProductVo>> list(Integer categoryId, Integer pageNum, Integer pageSize);
}
