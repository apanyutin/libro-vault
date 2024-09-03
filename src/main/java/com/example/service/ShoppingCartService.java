package com.example.service;

import com.example.dto.cart.ShoppingCartDto;

public interface ShoppingCartService {
    ShoppingCartDto getShoppingCartByUserId(Long id);
}
