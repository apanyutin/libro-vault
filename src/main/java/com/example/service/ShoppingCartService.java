package com.example.service;

import com.example.dto.cart.CreateCartItemRequestDto;
import com.example.dto.cart.ShoppingCartDto;
import com.example.dto.cart.UpdateCartItemRequestDto;

public interface ShoppingCartService {
    ShoppingCartDto getCartByUserId(Long userId);

    ShoppingCartDto addCartItemToCartByUserId(CreateCartItemRequestDto requestDto, Long userId);

    ShoppingCartDto updateCartItemQuantityById(
            Long cartItemId, UpdateCartItemRequestDto requestDto, Long userId);

    void deleteCartItemById(Long cartItemId);
}
