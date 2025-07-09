package com.gear4camp.mapper;

import com.gear4camp.domain.Cart;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface CartMapper {

    void insertCart(Cart cart);                        // 장바구니 항목 추가

    Optional<Cart> findByUserIdAndProductId(Long userId, Long productId); // 중복 확인용

    List<Cart> findByUserId(Long userId);              // 사용자 장바구니 전체 조회

    void updateQuantity(Long id, Integer quantity);    // 수량 변경

    void deleteCart(Long id);                          // 특정 장바구니 항목 삭제

    Optional<Cart> findById(Long id);                  // 단건 조회
}