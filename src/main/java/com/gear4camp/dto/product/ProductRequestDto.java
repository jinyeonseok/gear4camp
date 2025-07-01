package com.gear4camp.dto.product;

import lombok.Data;

@Data
public class ProductRequestDto {
    private String name;          // 상품 이름
    private String description;   // 상품 설명
    private Long price;           // 상품 가격
    private Integer stock;        // 재고 수량
    private String thumbnailUrl;  // 썸네일 이미지 URL
}