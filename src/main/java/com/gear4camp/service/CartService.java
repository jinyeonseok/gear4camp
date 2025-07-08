package com.gear4camp.service;

import com.gear4camp.domain.Cart;
import com.gear4camp.dto.cart.CartRequestDto;
import com.gear4camp.mapper.CartMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartMapper cartMapper;

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
}