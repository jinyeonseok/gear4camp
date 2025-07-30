package com.gear4camp.dto.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Schema(description = "주문 응답 DTO")
public class OrderResponseDto {

    @Schema(description = "주문 ID", example = "1001")
    private Long id;

    @Schema(description = "총 주문 금액", example = "135000")
    private Long totalPrice;

    @Schema(description = "주문 상태", example = "ORDERED")
    private String status;

    @Schema(description = "주문 생성일시", example = "2025-07-18T10:22:30")
    private String createdAt;

    @Schema(description = "주문 수정일시", example = "2025-07-18T10:22:30")
    private String updatedAt;

    @Schema(description = "주문 상품 목록")
    private List<OrderItemDto> orderItems;
}