package com.gear4camp.controller;

import com.gear4camp.dto.cart.CartQuantityUpdateRequest;
import com.gear4camp.dto.cart.CartRequestDto;
import com.gear4camp.dto.cart.CartResponseDto;
import com.gear4camp.service.CartService;
import com.gear4camp.service.UserService;
import com.gear4camp.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
@Tag(name = "장바구니", description = "Cart 관련 API")
@SecurityRequirement(name = "bearerAuth")
public class CartController {

    private final CartService cartService;
    private final UserService userService;

    @PostMapping
    @Operation(summary = "장바구니에 상품 추가", description = "상품 ID와 수량을 입력하여 장바구니에 상품을 추가합니다.")
    public ResponseEntity<Void> addToCart(@Valid @RequestBody CartRequestDto dto,
                                          Authentication authentication) {
        // JWT에서 userId 추출
        String userId = JwtUtil.getUserIdFromAuthentication(authentication);

        Long userDbId = userService.getUserDbId(userId); // 실제 DB PK 조회

        // 장바구니 서비스 호출
        cartService.addToCart(dto, Long.valueOf(userDbId));

        return ResponseEntity.ok().build(); // 200 OK만 반환 (body 없음)
    }

    @GetMapping
    @Operation(summary = "장바구니 조회", description = "로그인한 사용자의 장바구니 목록을 조회합니다.")
    public ResponseEntity<List<CartResponseDto>> getCart(Authentication authentication) {
        // JWT에서 userId 추출
        String userId = JwtUtil.getUserIdFromAuthentication(authentication);

        // userId로 DB의 실제 PK 조회
        Long userDbId = userService.getByUserId(userId).getId();

        // 장바구니 조회
        List<CartResponseDto> cartList = cartService.getCartListByUserId(userDbId);

        return ResponseEntity.ok(cartList);
    }

    @PutMapping("/{id}")
    @Operation(summary = "장바구니 수량 수정", description = "장바구니 상품의 수량을 수정합니다.")
    public ResponseEntity<Void> updateCartQuantity(
            @PathVariable("id") Long cartId,
            @RequestBody @Valid CartQuantityUpdateRequest dto,
            Authentication authentication
    ) {
        // JWT에서 userId 추출
        String userId = JwtUtil.getUserIdFromAuthentication(authentication);

        // 실제 DB의 user PK 조회
        Long userDbId = userService.getByUserId(userId).getId();

        System.out.println("userDbId from JWT: " + userDbId);

        // cartId가 userDbId의 소유인지 확인하는 검증 로직 추가
        // 4. 수량 업데이트
        cartService.updateCartQuantity(cartId, dto.getQuantity(), userDbId);

        return ResponseEntity.ok().build();
    }
}