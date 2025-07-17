package com.gear4camp.dto.cart;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "장바구니 상품 추가 요청 DTO")
public class CartRequestDto {

    @NotNull(message = "상품 ID는 필수입니다.")
    @Schema(description = "추가할 상품 ID", example = "1")
    private Long productId;

    @NotNull(message = "수량은 필수입니다.")
    @Min(value = 1, message = "수량은 1개 이상이어야 합니다.")
    @Schema(description = "추가할 수량", example = "2")
    private Integer quantity;
}