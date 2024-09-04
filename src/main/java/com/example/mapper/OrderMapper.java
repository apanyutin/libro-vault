package com.example.mapper;

import com.example.config.MapperConfig;
import com.example.dto.order.OrderDto;
import com.example.dto.order.UpdateOrderRequestDto;
import com.example.model.Order;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class, uses = OrderItemMapper.class)
public interface OrderMapper {
    Order toModel(OrderDto orderDto);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "orderItemDtos", source = "orderItems")
    OrderDto toDto(Order order);

    List<OrderDto> toDto(Set<Order> orders);

    void updateOrderFromDto(UpdateOrderRequestDto requestDto, @MappingTarget Order order);

    @AfterMapping
    default void setOrderItemDtos(@MappingTarget OrderDto orderDto,
                                  Order order,
                                  @Context OrderItemMapper orderItemMapper) {
        orderDto.setOrderItemDtos(order.getOrderItems().stream()
                .map(orderItemMapper::toDto)
                .collect(Collectors.toSet()));
    }
}
