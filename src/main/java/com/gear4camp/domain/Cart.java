package com.gear4camp.domain;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Cart {
    private Long id;               // 장바구니 항목 ID (PK)
    private Long userId;           // 사용자 ID (FK)
    private Long productId;        // 상품 ID (FK)
    private Integer quantity;      // 담긴 수량
    private Long price;         // 상품 가격
    private LocalDateTime createdAt; // 생성일시
    private LocalDateTime updatedAt; // 수정일시
}