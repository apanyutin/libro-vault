package com.example.service.impl;

import com.example.dto.order.CreateOrderRequestDto;
import com.example.dto.order.OrderDto;
import com.example.dto.order.OrderItemDto;
import com.example.dto.order.UpdateOrderRequestDto;
import com.example.exception.EntityNotFoundException;
import com.example.mapper.OrderItemMapper;
import com.example.mapper.OrderMapper;
import com.example.model.CartItem;
import com.example.model.Order;
import com.example.model.OrderItem;
import com.example.model.ShoppingCart;
import com.example.repository.cart.ShoppingCartRepository;
import com.example.repository.order.OrderItemRepository;
import com.example.repository.order.OrderRepository;
import com.example.service.OrderService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderItemMapper orderItemMapper;

    @Override
    public OrderDto placeNewOrder(Long userId, CreateOrderRequestDto requestDto) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId).orElseThrow(
                () -> new EntityNotFoundException(
                        "Can't find shopping cart by user id = " + userId));

        Order order = new Order();
        Set<OrderItem> orderItems = createSetOfOrderItems(order, shoppingCart);

        order.setOrderItems(orderItems);
        order.setTotal(calculateTotalPrice(orderItems));
        order.setShippingAddress(requestDto.getShippingAddress());
        order.setUser(shoppingCart.getUser());
        order.setOrderDate(LocalDateTime.now());

        shoppingCart.clearCartItems();
        shoppingCartRepository.flush();

        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public List<OrderDto> getAllOrdersByUserId(Long userId, Pageable pageable) {
        return orderMapper.toDto(orderRepository.findAllOrdersByUserId(userId));

    }

    @Override
    public OrderDto updateOrderStatus(Long orderId, UpdateOrderRequestDto requestDto) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new EntityNotFoundException("Can't find order by id = " + orderId));
        orderMapper.updateOrderFromDto(requestDto, order);
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public OrderItemDto getItemByOrderIdAndItemId(Long orderId, Long orderItemId) {
        return orderItemMapper.toDto(
                orderItemRepository.findByIdAndOrderId(orderItemId, orderId).orElseThrow(
                        () -> new EntityNotFoundException(String.format(
                                "Can't find order item by order id = %s and item id = %s",
                                orderId, orderItemId))));
    }

    @Override
    public List<OrderItemDto> getItemsByOrderId(Long orderId) {
        return orderItemMapper.toDto(orderItemRepository.findAllByOrderId(orderId));
    }

    private Set<OrderItem> createSetOfOrderItems(Order order, ShoppingCart shoppingCart) {
        Set<OrderItem> orderItems = new HashSet<>();
        Set<CartItem> cartItems = shoppingCart.getCartItems();
        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setBook(cartItem.getBook());
            orderItem.setPrice(cartItem.getBook().getPrice());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setOrder(order);
            orderItems.add(orderItem);
        }
        return orderItems;
    }

    private BigDecimal calculateTotalPrice(Set<OrderItem> orderItems) {
        BigDecimal total = BigDecimal.valueOf(0);
        for (OrderItem orderItem : orderItems) {
            total = total.add(orderItem.getPrice());
        }
        return total;
    }
}
