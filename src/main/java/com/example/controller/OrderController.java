package com.example.controller;

import com.example.dto.order.CreateOrderRequestDto;
import com.example.dto.order.OrderDto;
import com.example.dto.order.OrderItemDto;
import com.example.dto.order.UpdateOrderRequestDto;
import com.example.model.User;
import com.example.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Order management", description = "Endpoints for order managing")
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @Operation(summary = "Place an order", description = "Create order by shopping cart")
    @PostMapping
    public OrderDto createOrder(
            @RequestBody @Valid CreateOrderRequestDto requestDto,
            Authentication authentication) {
        Long userId = getUserIdByAuthentication(authentication);
        return orderService.placeNewOrder(userId, requestDto);
    }

    @Operation(summary = "Get all user's orders", description = "Get all user's orders")
    @GetMapping
    public List<OrderDto> getAllUsersOrders(Authentication authentication, Pageable pageable) {
        Long userId = getUserIdByAuthentication(authentication);
        return orderService.getAllOrdersByUserId(userId, pageable);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Update order status", description = "Update order status")
    @PatchMapping("/{orderId}")
    public OrderDto updateStatus(@PathVariable Long orderId,
                                 @RequestBody @Valid UpdateOrderRequestDto requestDto) {
        return orderService.updateOrderStatus(orderId, requestDto);
    }

    @Operation(summary = "(Get orderItems by orderId", description = "Get all orderItems by order")
    @GetMapping("/{orderId}/items")
    public List<OrderItemDto> getAllOrderItemsByOrderId(@PathVariable Long orderId) {
        return orderService.getItemsByOrderId(orderId);
    }

    @Operation(summary = "(Get orderItem by orderId and itemId",
            description = "Retrieve a specific OrderItem within an order")
    @GetMapping("/{orderId}/items/{itemId}")
    public OrderItemDto getOrderItemByOrderIdAndOrderItemId(
            @PathVariable Long orderId, @PathVariable Long itemId) {
        return orderService.getItemByOrderIdAndItemId(orderId, itemId);
    }

    private Long getUserIdByAuthentication(Authentication authentication) {
        return ((User) authentication.getPrincipal()).getId();
    }
}
