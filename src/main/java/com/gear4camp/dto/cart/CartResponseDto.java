package com.gear4camp.dto.cart;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "장바구니 조회 응답 DTO")
public class CartResponseDto {

    @Schema(description = "장바구니 항목 ID", example = "3")
    private Long id;

    @Schema(description = "상품명", example = "캠핑 테이블")
    private String productName;

    @Schema(description = "상품 ID", example = "42")
    private Long productId;

    @Schema(description = "상품 수량", example = "2")
    private Integer quantity;

    @Schema(description = "상품 가격", example = "25000")
    private Long price;

    @Schema(description = "생성일시", example = "2025-07-17T10:23:45")
    private String createdAt;

    @Schema(description = "수정일시", example = "2025-07-18T08:12:00")
    private String updatedAt;
}