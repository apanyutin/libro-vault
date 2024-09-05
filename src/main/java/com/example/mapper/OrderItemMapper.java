package com.example.mapper;

import com.example.config.MapperConfig;
import com.example.dto.order.OrderItemDto;
import com.example.model.OrderItem;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface OrderItemMapper {
    @Mapping(target = "bookId", source = "book.id")
    OrderItemDto toDto(OrderItem orderItem);

    List<OrderItemDto> toDto(Set<OrderItem> orderItems);
}
