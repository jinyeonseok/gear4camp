package com.gear4camp.service;

import com.gear4camp.domain.Cart;
import com.gear4camp.domain.Order;
import com.gear4camp.domain.OrderItem;
import com.gear4camp.domain.Product;
import com.gear4camp.dto.order.OrderItemDto;
import com.gear4camp.dto.order.OrderResponseDto;
import com.gear4camp.exception.CustomException;
import com.gear4camp.exception.ErrorCode;
import com.gear4camp.mapper.CartMapper;
import com.gear4camp.mapper.OrderItemMapper;
import com.gear4camp.mapper.OrderMapper;
import com.gear4camp.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final CartMapper cartMapper;
    private final ProductMapper productMapper;

    // 주문 등록
    public void createOrder(List<Long> cartIdList, Long userId) {

        // 1. 장바구니 항목들 조회
        List<Cart> cartList = cartIdList.stream()
                .map(id -> cartMapper.findById(id)
                        .orElseThrow(() -> new CustomException(ErrorCode.CART_NOT_FOUND)))
                .collect(Collectors.toList());

        // 2. 장바구니 소유자 확인
        for (Cart cart : cartList) {
            if (!cart.getUserId().equals(userId)) {
                throw new CustomException(ErrorCode.NO_AUTHORIZATION);
            }
        }

        // 3. 총 금액 계산
        long totalPrice = cartList.stream()
                .mapToLong(cart -> (long) cart.getPrice() * cart.getQuantity())
                .sum();

        // 4. 주문 엔티티 생성
        Order order = new Order();

        order.setUserId(userId);
        order.setTotalPrice(totalPrice);
        order.setStatus("ORDERED");

        // 5. 주문 저장
        orderMapper.insertOrder(order);
        Long orderId = order.getId(); // 방금 저장된 주문 ID

        // 6. 주문항목 저장
        for (Cart cart : cartList) {
            OrderItem item = new OrderItem();
            item.setOrderId(orderId);
            item.setProductId(cart.getProductId());
            item.setQuantity(cart.getQuantity());
            item.setPrice(cart.getPrice());

            orderItemMapper.insertOrderItem(item);
        }

        // 7. 장바구니 비우기 (선택)
        for (Long cartId : cartIdList) {
            cartMapper.deleteCart(cartId);
        }
    }

    public OrderResponseDto getOrderById(Long orderId, Long userDbId) {

        // 1. 주문 조회
        Order order = orderMapper.findById(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        // 2. 권한 확인
        if (!order.getUserId().equals(userDbId)) {
            throw new CustomException(ErrorCode.NO_AUTHORIZATION);
        }

        // 3. 주문 아이템들 조회
        List<OrderItem> items = orderItemMapper.findByOrderId(orderId);

        // 4. DTO 변환
        List<OrderItemDto> itemDtos = items.stream()
                .map(item -> {
                    Product product = productMapper.selectProductById(item.getProductId());
                    return new OrderItemDto(
                            item.getProductId(),
                            product.getName(),
                            item.getPrice(),
                            item.getQuantity()
                    );
                })
                .collect(Collectors.toList());

        // 5. 응답 DTO 조립
        OrderResponseDto response = new OrderResponseDto();

        response.setId(order.getId());
        response.setTotalPrice(order.getTotalPrice());
        response.setStatus(order.getStatus());
        response.setCreatedAt(order.getCreatedAt().toString());
        response.setUpdatedAt(order.getUpdatedAt().toString());
        response.setOrderItems(itemDtos);

        return response;
    }

    public List<OrderResponseDto> getOrdersByUserId(Long userDbId) {

        // 1. 유저의 전체 주문 목록 조회
        List<Order> orders = orderMapper.findByUserId(userDbId);

        // 2. 각 주문에 대해 주문 아이템 조회 및 DTO 변환
        return orders.stream()
                .map(order -> {
                    // 주문에 해당하는 아이템 목록 조회
                    List<OrderItem> items = orderItemMapper.findByOrderId(order.getId());

                    // 상품 이름 포함한 DTO로 변환
                    List<OrderItemDto> itemDtos = items.stream()
                            .map(item -> {
                                Product product = productMapper.selectProductById(item.getProductId());
                                return new OrderItemDto(
                                        item.getProductId(),
                                        product.getName(),
                                        item.getPrice(),
                                        item.getQuantity()
                                );
                            })
                            .collect(Collectors.toList());

                    // Order 응답 DTO 생성
                    OrderResponseDto dto = new OrderResponseDto();
                    dto.setId(order.getId());
                    dto.setTotalPrice(order.getTotalPrice());
                    dto.setStatus(order.getStatus());
                    dto.setCreatedAt(order.getCreatedAt().toString());
                    dto.setUpdatedAt(order.getUpdatedAt().toString());
                    dto.setOrderItems(itemDtos);

                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public void cancelOrder(Long orderId, Long userDbId) {

        // 1. 주문 조회
        Order order = orderMapper.findById(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        // 2. 권한 확인
        if (!order.getUserId().equals(userDbId)) {
            throw new CustomException(ErrorCode.NO_AUTHORIZATION);
        }

        // 3. 이미 취소된 주문인지 확인
        if ("CANCELLED".equals(order.getStatus())) {
            throw new CustomException(ErrorCode.ORDER_ALREADY_CANCELLED);
        }

        // 4. 주문 상태 취소로 업데이트
        orderMapper.cancelOrder(orderId);
    }

    public void updateOrderStatus(Long orderId, Long userDbId, String status) {

        // 1. 주문 조회
        Order order = orderMapper.findById(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        // 2. 권한 검증
        if (!order.getUserId().equals(userDbId)) {
            throw new CustomException(ErrorCode.NO_AUTHORIZATION);
        }

        // 3. 상태 업데이트
        orderMapper.updateStatus(orderId, status);
    }

}