package com.imooc.mall.dao;

import com.imooc.mall.pojo.Shipping;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface ShippingMapper {
    int deleteByPrimaryKey(Integer id);

    // 根据uId和shippingId两个条件删除。
    int deleteByIdAndUid(@Param("uid") Integer uid,
                         @Param("shippingId") Integer shippingId);

    int insert(Shipping record);

    int insertSelective(Shipping record);

    Shipping selectByPrimaryKey(Integer id);

    List<Shipping> selectByUid(Integer uid);

    int updateByPrimaryKeySelective(Shipping record);

    int updateByPrimaryKey(Shipping record);

    Shipping selectByIdAndUid(@Param("uid") Integer uid,
                              @Param("shippingId") Integer shippingId);

    List<Shipping> selectByIdSet(@Param("shippingIdSet") Set<Integer> shippingIdSet);
}