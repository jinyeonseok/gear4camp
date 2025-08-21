package com.gear4camp.dto.order;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Schema(description = "주문 등록 요청 DTO")
public class OrderRequestDto {

    @NotEmpty(message = "주문할 장바구니 ID 목록은 필수입니다.")
    @Schema(description = "주문할 장바구니 항목 ID 목록", example = "[3, 5, 7]")
    private List<Long> cartIdList;
}