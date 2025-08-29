package com.gear4camp.service;

import com.gear4camp.domain.Cart;
import com.gear4camp.dto.cart.CartRequestDto;
import com.gear4camp.dto.cart.CartResponseDto;
import com.gear4camp.exception.CustomException;
import com.gear4camp.exception.ErrorCode;
import com.gear4camp.mapper.CartMapper;
import com.gear4camp.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartMapper cartMapper;
    private final ProductMapper productMapper;

    // 장바구니 담기
    public Map<String, Object> addToCart(CartRequestDto dto, Long userId) {

        Map<String, Object> response = new HashMap<>();

        Optional<Cart> existingCart = cartMapper.findByUserIdAndProductId(userId, dto.getProductId());

        if (existingCart.isPresent()) { // 담은 상품이 존재하면 수량 증가 UPDATE

            int newQuantity = existingCart.get().getQuantity() + dto.getQuantity();
            cartMapper.updateQuantity(existingCart.get().getId(), newQuantity);

            response.put("productId", dto.getProductId());
            response.put("quantity", newQuantity);
            response.put("message", "장바구니 수량 증가 완료");

        } else { // 담은 상품이 존재하지 않으면 INSERT

//            Long price = productMapper.findPriceById(dto.getProductId());
            long price = dto.getPrice();

            System.out.println("price : " + price);

            Cart cart = new Cart();
            cart.setUserId(userId);
            cart.setProductId(dto.getProductId());
            cart.setQuantity(dto.getQuantity());
            cart.setPrice(price);

            cartMapper.insertCart(cart);

            response.put("productId", dto.getProductId());
            response.put("quantity", dto.getQuantity());
            response.put("message", "장바구니 상품 추가 성공");
        }

        return response;
    }

    // 장바구니 전체 조회
    public List<CartResponseDto> getCartListByUserId(Long userDbId) {
        List<Cart> cartList = cartMapper.findByUserId(userDbId);

        return cartList.stream()
                .map(cart -> {
                    CartResponseDto dto = new CartResponseDto();
                    dto.setId(cart.getId());
                    dto.setProductId(cart.getProductId());
                    dto.setQuantity(cart.getQuantity());
                    // todo -> 책임 분리 원식에 따라 dto쪽에서 처리해야함
                    dto.setCreatedAt(cart.getCreatedAt().toString());
                    dto.setUpdatedAt(cart.getUpdatedAt().toString());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    // 장바구니 수량 수정
    public void updateCartQuantity(Long cartId, Integer quantity, Long userDbId) {

        Cart cart = cartMapper.findById(cartId)
                        .orElseThrow(() -> new CustomException(ErrorCode.CART_NOT_FOUND));

        System.out.println("userDbId from token: " + userDbId);
        System.out.println("cart.userId in DB: " + cart.getUserId());

        if(!cart.getUserId().equals(userDbId)) {
            throw new CustomException(ErrorCode.NO_AUTHORIZATION);
        }

        cartMapper.updateQuantity(cartId, quantity);
    }

    // 장바구니 삭제
    public void deleteCart(Long cartId, Long userDbId) {
        Cart cart = cartMapper.findById(cartId)
                .orElseThrow(() -> new CustomException(ErrorCode.CART_NOT_FOUND));

        if (!cart.getUserId().equals(userDbId)) {
            throw new CustomException(ErrorCode.NO_AUTHORIZATION);
        }

        cartMapper.deleteCart(cartId);
    }

}