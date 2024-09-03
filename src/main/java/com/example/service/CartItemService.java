package com.example.service;

import com.example.dto.cart.CartItemDto;
import com.example.dto.cart.CreateCartItemRequestDto;

public interface CartItemService {
    CartItemDto save(CreateCartItemRequestDto requestDto);

    CartItemDto updateQuantityById(Long id, int quantity);

    void deleteById(Long id);
}
