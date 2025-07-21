package com.gear4camp.mapper;

import com.gear4camp.domain.Order;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface OrderMapper {

    void insertOrder(Order order);                      // 주문 생성

    Optional<Order> findById(Long id);                  // 단건 조회(주문 상세)

    List<Order> findByUserId(Long userId);              // 마이페이지 등 사용자의 모든 주문 조회

    void updateStatus(Long id, String status);          // 주문 상태 변경

    void cancelOrder(Long orderId);

}