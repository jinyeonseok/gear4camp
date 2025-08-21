package com.gear4camp.mapper;

import com.gear4camp.domain.OrderItem;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderItemMapper {
    void insertOrderItem(OrderItem item); // 단건 저장

    List<OrderItem> findByOrderId(Long orderId); // 주문 ID로 조회
}