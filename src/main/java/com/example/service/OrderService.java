package com.example.service;

import com.example.dto.order.CreateOrderRequestDto;
import com.example.dto.order.OrderDto;
import com.example.dto.order.OrderItemDto;
import com.example.dto.order.UpdateOrderRequestDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderDto placeNewOrder(Long userId, CreateOrderRequestDto requestDto);

    List<OrderDto> getAllOrdersByUserId(Long userId, Pageable pageable);

    OrderDto updateOrderStatus(Long orderId, UpdateOrderRequestDto requestDto);

    OrderItemDto getItemByOrderIdAndItemId(Long orderId, Long orderItemId);

    List<OrderItemDto> getItemsByOrderId(Long orderId);
}
