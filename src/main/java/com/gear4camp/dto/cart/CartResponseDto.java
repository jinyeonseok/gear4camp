package com.gear4camp.dto.cart;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartResponseDto {
    private Long id;
    private Long productId;
    private Integer quantity;
    private String createdAt;
    private String updatedAt;
}