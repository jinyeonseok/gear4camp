package com.gear4camp.controller;

import com.gear4camp.dto.order.OrderResponseDto;
import com.gear4camp.service.OrderService;
import com.gear4camp.service.UserService;
import com.gear4camp.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
@Tag(name = "주문", description = "Order 관련 API")
@SecurityRequirement(name = "bearerAuth")
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;

    @PostMapping
    @Operation(summary = "주문 생성", description = "장바구니 항목 ID 목록을 전달하여 주문을 생성합니다.")
    public ResponseEntity<Map<String, Object>> createOrder(
            @Parameter(description = "장바구니 ID 리스트", example = "[1, 2, 3]")
            @RequestBody List<Long> cartIdList,
            Authentication authentication) {
        // JWT에서 userId 추출
        String userId = JwtUtil.getUserIdFromAuthentication(authentication);

        // 실제 DB의 user PK 조회
        Long userDbId = userService.getByUserId(userId).getId();

        // 주문 생성 서비스 호출
        orderService.createOrder(cartIdList, userDbId);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "주문 생성 성공");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "주문 목록 조회", description = "로그인한 사용자의 전체 주문 내역을 조회합니다.")
    public ResponseEntity<Map<String, Object>> getOrders(Authentication authentication) {
        String userId = JwtUtil.getUserIdFromAuthentication(authentication);
        Long userDbId = userService.getByUserId(userId).getId();

        List<OrderResponseDto> orderList = orderService.getOrdersByUserId(userDbId);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "주문 목록 조회");
        response.put("orderList", orderList);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "주문 단건 조회", description = "주문 ID로 주문 정보를 조회합니다.")
    public ResponseEntity<Map<String, Object>> getOrderById(
            @Parameter(description = "주문 ID") @PathVariable("id") Long orderId, Authentication authentication) {

        String userId = JwtUtil.getUserIdFromAuthentication(authentication);
        Long userDbId = userService.getUserDbId(userId);

        OrderResponseDto dto = orderService.getOrderById(orderId, userDbId);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "주문 단건 조회");
        response.put("orderDto", dto);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{orderId}/cancel")
    @Operation(summary = "주문 취소", description = "주문 ID를 기반으로 주문을 취소합니다.")
    public ResponseEntity<Map<String, Object>> cancelOrder(@PathVariable("orderId") Long orderId, Authentication authentication) {

        String userId = JwtUtil.getUserIdFromAuthentication(authentication);
        Long userDbId = userService.getUserDbId(userId);

        orderService.cancelOrder(orderId, userDbId);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "주문 취소 성공");

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }

    @PutMapping("/{orderId}/status")
    @Operation(summary = "주문 상태 변경", description = "주문 상태를 변경합니다. 예: ORDERED, SHIPPED, DELIVERED 등") //
    public ResponseEntity<Map<String, Object>> updateOrderStatus(@PathVariable Long orderId, @RequestParam String status,
            Authentication authentication) {

        String userId = JwtUtil.getUserIdFromAuthentication(authentication);
        Long userDbId = userService.getUserDbId(userId);

        orderService.updateOrderStatus(orderId, userDbId, status);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "주문 상태 변경");

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }
}