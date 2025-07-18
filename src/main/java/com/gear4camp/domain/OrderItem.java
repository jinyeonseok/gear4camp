package com.gear4camp.domain;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderItem {
    private Long id;
    private Long orderId;     // 주문 ID
    private Long productId;   // 상품 ID
    private Integer quantity; // 수량
    private Long price;       // 상품 가격
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}