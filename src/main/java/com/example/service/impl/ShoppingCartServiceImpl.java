package com.example.service.impl;

import com.example.dto.cart.ShoppingCartDto;
import com.example.exception.EntityNotFoundException;
import com.example.mapper.CartItemMapper;
import com.example.mapper.ShoppingCartMapper;
import com.example.model.ShoppingCart;
import com.example.repository.cart.ShoppingCartRepository;
import com.example.service.ShoppingCartService;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemMapper cartItemMapper;

    @Override
    public ShoppingCartDto getShoppingCartByUserId(Long id) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find shopping cart by user id = " + id));
        ShoppingCartDto shoppingCartDto = shoppingCartMapper.toDto(shoppingCart);
        shoppingCartDto.setCartItems(shoppingCart.getCartItems().stream()
                .map(cartItemMapper::toDto)
                .collect(Collectors.toSet()));
        return shoppingCartDto;
    }
}
