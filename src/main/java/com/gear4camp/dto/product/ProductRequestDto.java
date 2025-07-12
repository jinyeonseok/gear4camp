package com.gear4camp.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "상품 등록/수정 요청 DTO")
public class ProductRequestDto {

    @Schema(description = "상품명", example = "캠핑 의자")
    @NotBlank(message = "상품명은 필수입니다.") // null + 빈 문자열 + 공백 전부 막음
    private String name;

    @Schema(description = "상품 설명", example = "접이식 방수 캠핑 의자")
    private String description;

    @Schema(description = "상품 가격", example = "12900")
    @NotNull(message = "가격은 필수입니다.")
    @Min(value = 0, message = "가격은 0 이상이어야 합니다.")
    private Long price;

    @Schema(description = "상품 재고 수량", example = "10")
    @NotNull(message = "재고는 필수입니다.")
    @Min(value = 0, message = "재고는 0 이상이어야 합니다.")
    private Integer stock;

    @Schema(description = "썸네일 이미지 URL", example = "https://example.com/image.jpg")
    private String thumbnailUrl;
}