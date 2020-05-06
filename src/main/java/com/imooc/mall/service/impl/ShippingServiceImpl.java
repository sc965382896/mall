package com.imooc.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imooc.mall.dao.ShippingMapper;
import com.imooc.mall.form.ShippingForm;
import com.imooc.mall.pojo.Shipping;
import com.imooc.mall.service.IShippingService;
import com.imooc.mall.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.imooc.mall.enums.ResponseEnum.SHIPPING_ERROR;

@Service
@Slf4j
public class ShippingServiceImpl implements IShippingService {

    @Autowired
    private ShippingMapper shippingMapper;

    @Override
    public ResponseVo<Map<String, Integer>> add(Integer uid, ShippingForm form) {

        Shipping shipping = new Shipping();
        BeanUtils.copyProperties(form, shipping);
        shipping.setUserId(uid);

        // 根据插入影响的元组数判断是否插入成功。
        int row = shippingMapper.insertSelective(shipping);
        if (row == 0) {
            return ResponseVo.error(SHIPPING_ERROR, "新建地址失败");
        }

        // 将插入的地址写入map中。
        Map<String, Integer> map = new HashMap<>();
        map.put("shippingId", shipping.getId());
        log.info("shippingId={}", shipping.getId());

        return ResponseVo.success("新建地址成功", map);
    }

    @Override
    public ResponseVo delete(Integer uid, Integer shippingId) {

        // 根据删除影响的元组数判断是否删除成功。
        int row = shippingMapper.deleteByIdAndUid(uid, shippingId);
        if (row == 0) {
            return ResponseVo.error(SHIPPING_ERROR, "删除地址失败");
        }

        return ResponseVo.success("删除地址成功");

    }

    @Override
    public ResponseVo update(Integer uid, Integer shippingId, ShippingForm form) {

        Shipping shipping = new Shipping();
        BeanUtils.copyProperties(form, shipping);
        shipping.setUserId(uid);
        shipping.setId(shippingId);

        // 根据更新影响的元组数判断是否更新成功。
        int row = shippingMapper.updateByPrimaryKeySelective(shipping);
        if (row == 0) {
            return ResponseVo.error(SHIPPING_ERROR, "更新地址失败");
        }
        return ResponseVo.success("更新地址成功");
    }

    @Override
    public ResponseVo<PageInfo<Shipping>> list(Integer uid, Integer pageNum, Integer pageSize) {

        // 分页查找
        PageHelper.startPage(pageNum, pageSize);
        List<Shipping> shippings = shippingMapper.selectByUid(uid);
        PageInfo<Shipping> pageInfo = new PageInfo<>(shippings);
        return ResponseVo.success(pageInfo);
    }
}
