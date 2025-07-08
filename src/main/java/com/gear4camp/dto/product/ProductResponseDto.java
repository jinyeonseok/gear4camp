package com.gear4camp.dto.product;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductResponseDto {
    private Long id;                // 상품 ID
    private String name;            // 상품 이름
    private String description;     // 상품 설명
    private Long price;             // 상품 가격
    private Integer stock;          // 재고 수량
    private String thumbnailUrl;    // 썸네일 URL
    private Long createdBy;         // 등록자 ID
    private String createdAt;       // 생성일시 (ISO-8601 문자열)
    private String updatedAt;       // 수정일시 (ISO-8601 문자열)
}