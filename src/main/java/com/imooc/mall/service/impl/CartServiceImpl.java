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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
    public ResponseVo<CartVo> add(Integer uid, CartAddForm form) {
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
        String redisKey = String.format(CART_REDIS_KEY_TEMPLATE, uid);
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

        return list(uid);
    }

    @Override
    public ResponseVo<CartVo> list(Integer uid) {

        // 获取购物车列表
        List<Cart> cartList = listCart(uid);

        // 获取购物车中商品id
        Set<Integer> productIdSet = cartList.stream().map(Cart::getProductId).collect(Collectors.toSet());
        List<Product> productList = productMapper.selectByProductIdSet(productIdSet);
        Map<Integer, Product> map = productList.stream().collect(Collectors.toMap(Product::getId, product -> product));

        // 返回值中的各项数据：商品总数，是否全选，总价。
        Integer totalQuantity = 0;
        boolean selectAll = true;
        BigDecimal totalPrice = BigDecimal.ZERO;

        List<CartProductVo> cartProductVoList = new ArrayList<>();
        for (Cart cart : cartList) {
            Product product = map.get(cart.getProductId());
            if (product == null) {
                return ResponseVo.error(ResponseEnum.PRODUCT_NOT_EXIST,
                        "商品不存在, productId = " + cart.getProductId());
            }
            if (product.getStock() < cart.getQuantity()) {
                return ResponseVo.error(ResponseEnum.PRODUCT_STOCK_ERROR, product.getName() + "库存不足");
            }

            cartProductVoList.add(builderCartProductVo(cart, product));

            // 没选中则不代表全选
            if (!cart.getProductSelected()) {
                selectAll = false;
            }

            // 选中才计入总金额
            if (cart.getProductSelected()) {
                totalPrice = totalPrice.add(product.getPrice().multiply(BigDecimal.valueOf(cart.getQuantity())));
            }

            totalQuantity += cart.getQuantity();
        }

        CartVo cartVo = new CartVo();
        cartVo.setCartProductVoList(cartProductVoList);
        cartVo.setCartTotalPrice(totalPrice);
        cartVo.setCartTotalQuantity(totalQuantity);
        cartVo.setSelectedAll(selectAll);

        return ResponseVo.success(cartVo);
    }

    private CartProductVo builderCartProductVo(Cart cart, Product product) {

        CartProductVo cartProductVo = new CartProductVo();
        cartProductVo.setProductId(cart.getProductId());
        cartProductVo.setQuantity(cart.getQuantity());
        cartProductVo.setProductName(product.getName());
        cartProductVo.setProductMainImage(product.getMainImage());
        cartProductVo.setProductSubtitle(product.getSubtitle());
        cartProductVo.setProductPrice(product.getPrice());
        cartProductVo.setProductStatus(product.getStatus());
        cartProductVo.setProductStock(product.getStock());
        cartProductVo.setProductTotalPrice(product.getPrice().multiply(BigDecimal.valueOf(cart.getQuantity())));
        cartProductVo.setProductSelected(cart.getProductSelected());
        return cartProductVo;
    }

    @Override
    public ResponseVo<CartVo> update(Integer uid, Integer productId, CartUpdateForm form) {
        HashOperations<String, String, String> operations = redisTemplate.opsForHash();
        String redisKey = String.format(CART_REDIS_KEY_TEMPLATE, uid);
        String value = operations.get(redisKey, String.valueOf(productId));

        // 购物车无该商品，报错。
        if (StringUtils.isEmpty(value)) {
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

        operations.put(redisKey, String.valueOf(productId), gson.toJson(cart));
        return list(uid);
    }

    @Override
    public ResponseVo<CartVo> delete(Integer uid, Integer productId) {
        HashOperations<String, String, String> operations = redisTemplate.opsForHash();
        String redisKey = String.format(CART_REDIS_KEY_TEMPLATE, uid);
        String value = operations.get(redisKey, String.valueOf(productId));

        // 购物车无该商品，报错。
        if (StringUtils.isEmpty(value)) {
            return ResponseVo.error(ResponseEnum.CART_PRODUCT_NOT_EXIST);
        }

        // 使用该指令删除一个键值对，当键值对全部删除的时候该项数据也被删除。
        operations.delete(redisKey, String.valueOf(productId));
        return list(uid);
    }

    @Override
    public ResponseVo<CartVo> selectAll(Integer uid) {
        HashOperations<String, String, String> operations = redisTemplate.opsForHash();
        String redisKey = String.format(CART_REDIS_KEY_TEMPLATE, uid);

        for (Cart cart : listCart(uid)) {
            cart.setProductSelected(true);
            operations.put(redisKey, String.valueOf(cart.getProductId()), gson.toJson(cart));
        }

        return list(uid);
    }

    @Override
    public ResponseVo<CartVo> unSelectedAll(Integer uid) {
        HashOperations<String, String, String> operations = redisTemplate.opsForHash();
        String redisKey = String.format(CART_REDIS_KEY_TEMPLATE, uid);

        for (Cart cart : listCart(uid)) {
            cart.setProductSelected(false);
            operations.put(redisKey, String.valueOf(cart.getProductId()), gson.toJson(cart));
        }

        return list(uid);
    }

    @Override
    public ResponseVo<Integer> sum(Integer uid) {

        // 使用管道对集合进行操作。
        Integer sum = listCart(uid).stream()
                .map(Cart::getQuantity)
                .reduce(0, Integer::sum);

        return ResponseVo.success(sum);
    }

    @Override
    public List<Cart> listCart(Integer uid) {
        HashOperations<String, String, String> operations = redisTemplate.opsForHash();
        String redisKey = String.format(CART_REDIS_KEY_TEMPLATE, uid);
        Set<Map.Entry<String, String>> entries = operations.entries(redisKey).entrySet();

        List<Cart> cartList = new ArrayList<>();
        for (Map.Entry<String, String> entry : entries) {
            cartList.add(gson.fromJson(entry.getValue(), Cart.class));
        }

        return cartList;
    }
}
