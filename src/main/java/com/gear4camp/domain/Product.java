package com.gear4camp.domain;

import lombok.Data;
import org.apache.ibatis.mapping.FetchType;

import java.time.LocalDateTime;

@Data
public class Product {
    private Long id;                 // 상품 고유 ID
    private String name;            // 상품 이름
    private String description;     // 상품 설명
    private Long price;             // 상품 가격
    private Integer stock;          // 재고 수량
    private String thumbnailUrl;    // 썸네일 이미지 URL
    private LocalDateTime createdAt; // 생성일시
    private LocalDateTime updatedAt; // 수정일시
    private Long createdBy;         // 등록자 (User의 ID)
}