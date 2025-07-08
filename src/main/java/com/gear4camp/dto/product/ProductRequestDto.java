package com.gear4camp.dto.product;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRequestDto {

    @NotBlank(message = "상품명은 필수입니다.") // null + 빈 문자열 + 공백
    private String name;

    private String description;

    @NotNull(message = "가격은 필수입니다.") // null
    @Min(value = 0, message = "가격은 0 이상이어야 합니다.")
    private Long price;

    @NotNull(message = "재고는 필수입니다.") // null
    @Min(value = 0, message = "재고는 0 이상이어야 합니다.")
    private Integer stock;

    private String thumbnailUrl;
}