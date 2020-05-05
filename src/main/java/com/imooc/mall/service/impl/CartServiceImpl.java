package com.imooc.mall.service.impl;

import com.google.gson.Gson;
import com.imooc.mall.dao.ProductMapper;
import com.imooc.mall.enums.ProductStatusEnum;
import com.imooc.mall.enums.ResponseEnum;
import com.imooc.mall.form.CartAddForm;
import com.imooc.mall.form.CartUpdateForm;
import com.imooc.mall.pojo.Cart;
import com.imooc.mall.pojo.Product;
import com.imooc.mall.service.ICartService;
import com.imooc.mall.vo.CartProductVo;
import com.imooc.mall.vo.CartVo;
import com.imooc.mall.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.*;

@Service
@Slf4j
public class CartServiceImpl implements ICartService {

    private final static String CART_REDIS_KEY_TEMPLATE = "cart_%d";
    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private Gson gson = new Gson();

    @Override
    public ResponseVo<CartVo> add(Integer uId, CartAddForm form) {
        Integer quantity = 1;
        Product product = productMapper.selectByPrimaryKey(form.getProductId());

        // 商品是否存在
        if (product == null) {
            return ResponseVo.error(ResponseEnum.PRODUCT_NOT_EXIST);
        }

        // 商品是否正常在售
        if (product.getStatus().equals(ProductStatusEnum.OFF_SALE.getCode()) || product.getStatus().equals(ProductStatusEnum.DELETE.getCode())) {
            return ResponseVo.error(ResponseEnum.PRODUCT_OFF_SLAE_OR_DELETE);
        }

        // 库存是否充足
        if (product.getStock() <= 0) {
            return ResponseVo.error(ResponseEnum.PRODUCT_STOCK_ERROR);
        }

        // 向redis写入数据
        // 使用gson将对象转为json
        HashOperations<String, String, String> operations = redisTemplate.opsForHash();
        String redisKey = String.format(CART_REDIS_KEY_TEMPLATE, uId);
        String value = operations.get(redisKey, String.valueOf(product.getId()));
        Cart cart = new Cart();
        if (StringUtils.isEmpty(value)) {
            // 没有该商品，新增
            cart = new Cart(product.getId(), quantity, form.getSelected());
        } else {
            // 已经有了该商品，数量加一
            cart = gson.fromJson(value, Cart.class);
            cart.setQuantity(cart.getQuantity() + quantity);
        }
        operations.put(redisKey, String.valueOf(product.getId()), gson.toJson(cart));

        return list(uId);
    }

    @Override
    public ResponseVo<CartVo> list(Integer uId) {
        HashOperations<String, String, String> operations = redisTemplate.opsForHash();
        String redisKey = String.format(CART_REDIS_KEY_TEMPLATE, uId);

        Map<Integer, Cart> cartMap = new HashMap<>();
        Map<String, String> entries = operations.entries(redisKey);
        Integer totalQuantity = 0;
        Boolean selectAll = true;
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (Map.Entry<String, String> stringStringEntry : entries.entrySet()) {
            Integer productId = Integer.valueOf(stringStringEntry.getKey());
            Cart cart = gson.fromJson(stringStringEntry.getValue(), Cart.class);
            cartMap.put(productId, cart);
            totalQuantity += cart.getQuantity();
        }
        CartVo cartVo = new CartVo();

        if (!cartMap.keySet().isEmpty()) {
            List<Product> products = productMapper.selectByProductIdSet(cartMap.keySet());
            List<CartProductVo> cartProductVoList = new ArrayList<>();
            for (Product product : products) {
                Cart cart = cartMap.get(product.getId());
                BigDecimal productTotalPrice = product.getPrice().multiply(BigDecimal.valueOf(cart.getQuantity()));
                CartProductVo cartProductVo = new CartProductVo(cart.getProductId(),
                        cart.getQuantity(),
                        product.getName(),
                        product.getSubtitle(),
                        product.getMainImage(),
                        product.getPrice(),
                        product.getStatus(),
                        productTotalPrice,
                        product.getStock(),
                        cart.getProductSelected());
                cartProductVoList.add(cartProductVo);

                // 没选中则不代表全选
                if (!cart.getProductSelected()) {
                    selectAll = false;
                }

                // 选中才计入总金额
                if (cart.getProductSelected()) {
                    totalPrice = totalPrice.add(productTotalPrice);
                }
            }

            cartVo.setCartProductVoList(cartProductVoList);
        }

        cartVo.setCartTotalPrice(totalPrice);
        cartVo.setCartTotalQuantity(totalQuantity);
        cartVo.setSelectedAll(selectAll);

        return ResponseVo.success(cartVo);
    }

    @Override
    public ResponseVo<CartVo> update(Integer uId, Integer productId, CartUpdateForm form) {
        HashOperations<String, String, String> operations = redisTemplate.opsForHash();
        String redisKey = String.format(CART_REDIS_KEY_TEMPLATE, uId);
        String value = operations.get(redisKey, String.valueOf(productId));
        if (StringUtils.isEmpty(value)) {
            // 购物车无该商品，报错。
            return ResponseVo.error(ResponseEnum.CART_PRODUCT_NOT_EXIST);
        }
        // 更新商品信息。
        Cart cart = gson.fromJson(value, Cart.class);
        if (form.getQuantity() != null && cart.getQuantity() >= 0) {
            cart.setQuantity(cart.getQuantity());
        }
        if (form.getSelected() != null) {
            cart.setProductSelected(form.getSelected());
        }

        return list(uId);
    }

    @Override
    public ResponseVo<CartVo> delete(Integer uId, Integer productId) {
        HashOperations<String, String, String> operations = redisTemplate.opsForHash();
        String redisKey = String.format(CART_REDIS_KEY_TEMPLATE, uId);
        String value = operations.get(redisKey, String.valueOf(productId));
        if (StringUtils.isEmpty(value)) {
            // 购物车无该商品，报错。
            return ResponseVo.error(ResponseEnum.CART_PRODUCT_NOT_EXIST);
        }

        operations.delete(redisKey, String.valueOf(productId));
        return list(uId);
    }

    @Override
    public ResponseVo<CartVo> selectAll(Integer uId) {
        HashOperations<String, String, String> operations = redisTemplate.opsForHash();
        String redisKey = String.format(CART_REDIS_KEY_TEMPLATE, uId);

        for (Cart cart : listCart(uId)) {
            cart.setProductSelected(true);
            operations.put(redisKey, String.valueOf(cart.getProductId()), gson.toJson(cart));
        }

        return list(uId);
    }

    @Override
    public ResponseVo<CartVo> unSelectedAll(Integer uId) {
        HashOperations<String, String, String> operations = redisTemplate.opsForHash();
        String redisKey = String.format(CART_REDIS_KEY_TEMPLATE, uId);

        for (Cart cart : listCart(uId)) {
            cart.setProductSelected(false);
            operations.put(redisKey, String.valueOf(cart.getProductId()), gson.toJson(cart));
        }

        return list(uId);
    }

    @Override
    public ResponseVo<Integer> sum(Integer uId) {
        Integer sum = listCart(uId).stream()
                .map(Cart::getQuantity)
                .reduce(0, Integer::sum);

        return ResponseVo.success(sum);
    }

    private List<Cart> listCart(Integer uId) {
        HashOperations<String, String, String> operations = redisTemplate.opsForHash();
        String redisKey = String.format(CART_REDIS_KEY_TEMPLATE, uId);
        Set<Map.Entry<String, String>> entries = operations.entries(redisKey).entrySet();

        List<Cart> list = new ArrayList<>();
        for (Map.Entry<String, String> entry : entries) {
            list.add(gson.fromJson(entry.getValue(), Cart.class));
        }
        return list;
    }
}
