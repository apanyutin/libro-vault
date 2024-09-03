package com.example.mapper;

import com.example.config.MapperConfig;
import com.example.dto.cart.CartItemDto;
import com.example.dto.cart.CreateCartItemRequestDto;
import com.example.model.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface CartItemMapper {
    @Mapping(target = "bookId", source = "book.id")
    @Mapping(target = "bookTitle", source = "book.title")
    CartItemDto toDto(CartItem cartItem);

    @Mapping(target = "shoppingCart.id", source = "shoppingCartId")
    @Mapping(target = "book.id", source = "bookId")
    CartItem toModel(CreateCartItemRequestDto requestDto);
}
