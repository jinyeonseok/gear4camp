package com.gear4camp.dto.product;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Schema(description = "상품 응답 DTO")
public class ProductResponseDto {

    @Schema(description = "상품 ID", example = "1")
    private Long id;                         // 상품 ID

    @Schema(description = "상품 이름", example = "캠핑 텐트")
    private String name;                     // 상품 이름

    @Schema(description = "상품 설명", example = "4인용 방수 텐트")
    private String description;              // 상품 설명

    @Schema(description = "상품 가격", example = "89000")
    private Long price;                      // 상품 가격

    @Schema(description = "재고 수량", example = "25")
    private Integer stock;                   // 재고 수량

    @Schema(description = "썸네일 이미지 URL", example = "https://example.com/image.jpg")
    private String thumbnailUrl;             // 썸네일 URL

    @Schema(description = "등록자 ID", example = "3")
    private Long createdBy;                  // 등록자 ID

    @Schema(description = "생성일시 (ISO-8601)", example = "2025-07-10T13:00:00")
    private String createdAt;                // 생성일시 (ISO-8601 문자열)

    @Schema(description = "수정일시 (ISO-8601)", example = "2025-07-10T13:01:00")
    private String updatedAt;                // 수정일시 (ISO-8601 문자열)
}