package com.gear4camp.controller;

import com.gear4camp.dto.cart.CartRequestDto;
import com.gear4camp.service.CartService;
import com.gear4camp.service.UserService;
import com.gear4camp.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<Void> addToCart(@Valid @RequestBody CartRequestDto dto,
                                          Authentication authentication) {
        // JWT에서 userId 추출
        String userId = JwtUtil.getUserIdFromAuthentication(authentication);

        Long userDbId = userService.getUserDbId(userId); // 실제 DB PK 조회

        // 장바구니 서비스 호출
        cartService.addToCart(dto, Long.valueOf(userDbId));

        return ResponseEntity.ok().build(); // 200 OK만 반환 (body 없음)
    }
}