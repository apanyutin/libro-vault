package com.example.mapper;

import com.example.config.MapperConfig;
import com.example.dto.cart.ShoppingCartDto;
import com.example.model.ShoppingCart;
import java.util.stream.Collectors;
import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class, uses = CartItemMapper.class)
public interface ShoppingCartMapper {
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "cartItems", source = "cartItems")
    ShoppingCartDto toDto(ShoppingCart shoppingCart);

    @AfterMapping
    default void setCartItems(@MappingTarget ShoppingCartDto shoppingCartDto,
                              ShoppingCart shoppingCart,
                              @Context CartItemMapper cartItemMapper) {
        shoppingCartDto.setCartItems(shoppingCart.getCartItems().stream()
                .map(cartItemMapper::toDto)
                .collect(Collectors.toSet()));
    }
}
