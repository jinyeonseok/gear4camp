package com.gear4camp.domain;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Order {
    private Long id;               // 주문 ID (PK)
    private Long userId;           // 사용자 ID (FK)
    private String status;         // 주문 상태 (CREATED, PAID 등)
    private Long totalPrice;       // 주문 총액
    private LocalDateTime createdAt; // 생성일시
    private LocalDateTime updatedAt; // 수정일시
}