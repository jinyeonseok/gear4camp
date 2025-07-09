package com.gear4camp.service;

import com.gear4camp.domain.Cart;
import com.gear4camp.dto.cart.CartRequestDto;
import com.gear4camp.dto.cart.CartResponseDto;
import com.gear4camp.exception.CustomException;
import com.gear4camp.exception.ErrorCode;
import com.gear4camp.mapper.CartMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartMapper cartMapper;

    // 장바구니 담기
    public void addToCart(CartRequestDto dto, Long userId) {
        // 1. 중복 여부 확인
        cartMapper.findByUserIdAndProductId(userId, dto.getProductId())
                .ifPresentOrElse(existingCart -> {
                    // 2. 이미 있으면 수량 업데이트
                    int newQuantity = existingCart.getQuantity() + dto.getQuantity();
                    cartMapper.updateQuantity(existingCart.getId(), newQuantity);
                }, () -> {
                    // 3. 없으면 새로 insert
                    Cart cart = new Cart();
                    cart.setUserId(userId);
                    cart.setProductId(dto.getProductId());
                    cart.setQuantity(dto.getQuantity());

                    cartMapper.insertCart(cart);
                });
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

}