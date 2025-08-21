package com.gear4camp.dto.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "주문 상품 응답 DTO")
public class OrderItemDto {

    @Schema(description = "상품 ID", example = "3")
    private Long productId;

    @Schema(description = "상품명", example = "캠핑의자")
    private String productName;

    @Schema(description = "상품 가격", example = "25000")
    private Long price;

    @Schema(description = "상품 수량", example = "2")
    private Integer quantity;

    public OrderItemDto(Long productId, String productName, Long price, Integer quantity) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
    }
}